package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.StringUtils;
import com.axin.errand.service.MiniappTranslateService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序翻译接口
 *
 * 供前端在切换语言时翻译后端动态文案（公告、隐私政策、店铺名、商品名等）。
 * 由后端代理发起外部请求，规避小程序 request 合法域名白名单限制。
 */
@Tag(name = "小程序翻译接口")
@ApiSupport(author = "AXIN", order = 60)
@RestController("miniappApiTranslateController")
@RequestMapping("/miniapp/translate")
public class MiniappTranslateController extends BaseController {

    /** 批量翻译最大条数 */
    private static final int MAX_BATCH = 50;

    @Autowired
    private MiniappTranslateService translateService;

    @Operation(summary = "文本翻译")
    @ApiOperationSupport(order = 1)
    @Anonymous
    @PostMapping
    public AjaxResult translate(@RequestBody TranslateRequest req) {
        if (req == null) {
            return error("参数不能为空");
        }
        String from = StringUtils.isNotEmpty(req.getFrom()) ? req.getFrom() : "zh";
        String to = StringUtils.isNotEmpty(req.getTo()) ? req.getTo() : "ko";
        // 注意：译文是 String 时若直接传 success(text) 会命中 success(String msg) 重载，
        // 导致文案被写进 msg、data 为空，因此这里显式写入 data
        if (req.getTexts() != null && !req.getTexts().isEmpty()) {
            if (req.getTexts().size() > MAX_BATCH) {
                return error("批量翻译数量不能超过" + MAX_BATCH + "条");
            }
            return AjaxResult.success().put("data", translateService.translateBatch(req.getTexts(), from, to));
        }
        if (StringUtils.isEmpty(req.getText())) {
            return error("待翻译文本不能为空");
        }
        return AjaxResult.success().put("data", translateService.translate(req.getText(), from, to));
    }

    /**
     * 翻译请求参数
     */
    public static class TranslateRequest {
        /** 单条文本，与 texts 二选一 */
        private String text;
        /** 批量文本 */
        private List<String> texts;
        /** 源语言，默认 zh */
        private String from;
        /** 目标语言，默认 ko */
        private String to;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getTexts() {
            return texts;
        }

        public void setTexts(List<String> texts) {
            this.texts = texts;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
