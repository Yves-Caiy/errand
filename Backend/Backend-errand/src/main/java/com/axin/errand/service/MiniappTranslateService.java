package com.axin.errand.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.axin.common.core.redis.RedisCache;
import com.axin.common.utils.StringUtils;
import com.axin.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 小程序翻译服务
 *
 * 由后端代理调用免密钥公共翻译接口（默认 MyMemory）完成中韩文本翻译，
 * 译文写入 Redis 缓存，避免相同文案重复请求外部接口。
 * 翻译失败时一律返回原文，保证前端不因翻译异常而报错。
 */
@Service
public class MiniappTranslateService {

    private static final Logger log = LoggerFactory.getLogger(MiniappTranslateService.class);

    /** 译文缓存 key 前缀 */
    private static final String CACHE_PREFIX = "MINIAPP_TRANSLATE:";
    /** 译文缓存天数 */
    private static final long CACHE_DAYS = 7L;
    /** 单条文本切分长度上限（公共接口对 q 长度有限制） */
    private static final int MAX_SEGMENT = 450;
    /** 是否包含中文字符 */
    private static final Pattern CN_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");

    /** 翻译开关 */
    @Value("${translate.enabled:true}")
    private boolean enabled;

    /** 公共翻译接口地址（免密钥） */
    @Value("${translate.provider:https://api.mymemory.translated.net/get}")
    private String provider;

    @Autowired
    private RedisCache redisCache;

    /**
     * 翻译单条文本。失败时返回原文。
     *
     * @param text 待翻译文本
     * @param from 源语言（zh / ko / en，缺省按 zh 处理）
     * @param to   目标语言（缺省按 ko 处理）
     */
    public String translate(String text, String from, String to) {
        if (StringUtils.isEmpty(text) || !enabled) {
            return text;
        }
        String src = normalizeLang(from, "zh-CN");
        String dst = normalizeLang(to, "ko");
        if (src.equals(dst)) {
            return text;
        }
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return text;
        }
        // 源语言是中文、而文本本身不含中文（纯数字/符号/英文）时无需翻译
        if (src.startsWith("zh") && !CN_PATTERN.matcher(trimmed).find()) {
            return text;
        }

        String cacheKey = CACHE_PREFIX + src + ":" + dst + ":" + md5(trimmed);
        String cached = redisCache.getCacheObject(cacheKey);
        if (StringUtils.isNotEmpty(cached)) {
            return cached;
        }

        String translated = translateChunks(trimmed, src, dst);
        if (StringUtils.isEmpty(translated)) {
            // 外部接口不可用，返回原文且不写缓存，便于后续重试
            return text;
        }
        redisCache.setCacheObject(cacheKey, translated, (int) (CACHE_DAYS * 24 * 3600), TimeUnit.SECONDS);
        return translated;
    }

    /**
     * 批量翻译，逐条调用，失败项返回原文。
     */
    public List<String> translateBatch(List<String> texts, String from, String to) {
        List<String> result = new ArrayList<>();
        if (texts == null) {
            return result;
        }
        for (String text : texts) {
            result.add(translate(text, from, to));
        }
        return result;
    }

    /**
     * 长文本按标点切分为多段后逐段翻译再拼接，规避公共接口的长度限制。
     */
    private String translateChunks(String text, String src, String dst) {
        List<String> segments = splitText(text, MAX_SEGMENT);
        StringBuilder sb = new StringBuilder();
        for (String segment : segments) {
            if (segment.trim().isEmpty()) {
                sb.append(segment);
                continue;
            }
            String part = callProvider(segment, src, dst);
            sb.append(StringUtils.isNotEmpty(part) ? part : segment);
        }
        String translated = sb.toString();
        // 与原文本一致说明没有任何一段翻译成功，视为失败
        return translated.equals(text) ? null : translated;
    }

    /**
     * 按句末标点切分长文本，尽量保持语义完整。
     */
    private List<String> splitText(String text, int maxLen) {
        List<String> result = new ArrayList<>();
        if (text.length() <= maxLen) {
            result.add(text);
            return result;
        }
        int start = 0;
        int length = text.length();
        while (start < length) {
            int end = Math.min(start + maxLen, length);
            if (end < length) {
                int cut = -1;
                for (int i = end; i > start + maxLen / 2; i--) {
                    char c = text.charAt(i - 1);
                    if (c == '\n' || c == '\r' || c == '。' || c == '！' || c == '？' || c == '；'
                            || c == '.' || c == '!' || c == '?' || c == ';') {
                        cut = i;
                        break;
                    }
                }
                if (cut > start) {
                    end = cut;
                }
            }
            result.add(text.substring(start, end));
            start = end;
        }
        return result;
    }

    /**
     * 调用公共翻译接口。返回 null 表示本次翻译失败。
     */
    private String callProvider(String text, String src, String dst) {
        try {
            String params = "q=" + URLEncoder.encode(text, StandardCharsets.UTF_8)
                    + "&langpair=" + URLEncoder.encode(src + "|" + dst, StandardCharsets.UTF_8);
            String resp = HttpUtils.sendGet(provider, params);
            if (StringUtils.isEmpty(resp)) {
                return null;
            }
            JSONObject obj = JSON.parseObject(resp);
            if (obj == null) {
                return null;
            }
            JSONObject data = obj.getJSONObject("responseData");
            if (data == null) {
                return null;
            }
            String result = data.getString("translatedText");
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            String lower = result.toLowerCase();
            // 公共接口在配额耗尽或参数异常时会直接返回英文提示，视为失败
            if (lower.contains("invalid") || lower.contains("quota")
                    || lower.contains("mymemory warning") || lower.contains("please select two distinct")
                    || lower.contains("no query specified")) {
                log.warn("翻译接口返回异常内容: {}", result);
                return null;
            }
            String decoded = unescapeHtml(result).trim();
            return decoded.isEmpty() ? null : decoded;
        } catch (Exception e) {
            log.warn("调用翻译接口异常: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 统一语言标识，兼容 zh / zh-CN / cn / 中文 等写法。
     */
    private String normalizeLang(String lang, String defaultLang) {
        if (StringUtils.isEmpty(lang)) {
            return defaultLang;
        }
        String l = lang.trim().toLowerCase();
        if ("zh".equals(l) || "zh-cn".equals(l) || "zh_cn".equals(l) || "cn".equals(l) || "chinese".equals(l)) {
            return "zh-CN";
        }
        if ("ko".equals(l) || "kr".equals(l) || "ko-kr".equals(l) || "korean".equals(l)) {
            return "ko";
        }
        if ("en".equals(l) || "en-us".equals(l) || "english".equals(l)) {
            return "en";
        }
        return l;
    }

    /**
     * 简单反转义公共接口返回的 HTML 实体。
     */
    private String unescapeHtml(String text) {
        return text.replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&");
    }

    private String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(text.hashCode());
        }
    }
}
