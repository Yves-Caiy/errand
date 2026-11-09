package com.axin.errand.controller;

import com.axin.common.config.RuoYiConfig;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.file.FileUploadUtils;
import com.axin.common.utils.file.MimeTypeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序统一上传：头像、图片、文件全部上传到服务器本地磁盘，并返回 http 格式的访问地址。
 * 头像和打印文件等场景共用这一个接口。
 *
 * @author axin
 */
@Tag(name = "小程序统一上传", description = "头像 / 图片 / 文件统一上传到本地并返回 http 地址")
@RestController
@RequestMapping("/miniapp/upload")
public class MiniappUploadController
{
    private static final Logger log = LoggerFactory.getLogger(MiniappUploadController.class);

    /**
     * 统一上传入口（本地存储）
     *
     * @param file 上传的文件（字段名固定为 file）
     * @return url：完整的 http 地址；fileName：服务器相对路径
     */
    @Operation(summary = "统一上传（本地存储，返回 http 地址）")
    @PostMapping
    public AjaxResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request)
    {
        try
        {
            if (file == null || file.isEmpty())
            {
                return AjaxResult.error("上传文件不能为空");
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null)
            {
                return AjaxResult.error("文件名不能为空");
            }

            // 保存到本地磁盘：ruoyi.profile/upload/日期目录/UUID.后缀
            String pathFileName = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file,
                    MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, true);

            // 拼接可直接访问的 http 地址（域名取当前请求的 host，本地即 http://localhost:8080）
            String url = getBaseUrl(request) + pathFileName;

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", pathFileName);
            ajax.put("originalFilename", originalFilename);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 取当前请求的服务地址，例如 http://localhost:8080
     */
    private String getBaseUrl(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length())
                .append(contextPath).toString();
    }
}
