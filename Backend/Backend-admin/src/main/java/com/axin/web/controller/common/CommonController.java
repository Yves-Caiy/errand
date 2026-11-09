package com.axin.web.controller.common;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.axin.common.config.RuoYiConfig;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.StringUtils;
import com.axin.common.utils.file.FileUploadUtils;
import com.axin.common.utils.file.FileUtils;
import com.axin.common.utils.file.MimeTypeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 通用请求处理
 * 
 * @author axin
 */
@Tag(name = "通用上传下载", description = "文件上传、下载接口")
@RestController
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @Operation(summary = "通用文件下载")
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @Operation(summary = "通用单文件上传")
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file, HttpServletRequest request) throws Exception
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
            // 保存到服务器本地磁盘：ruoyi.profile/upload/日期/UUID.后缀
            String pathFileName = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file,
                    MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, true);
            // 拼出可直接访问的 http 地址
            String url = getBaseUrl(request) + pathFileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", url);
            ajax.put("newFileName", FileUtils.getName(pathFileName));
            ajax.put("originalFilename", originalFilename);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return AjaxResult.error("上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @Operation(summary = "通用多文件上传")
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files, HttpServletRequest request) throws Exception
    {
        try
        {
            List<String> urls = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
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
                // 保存到服务器本地磁盘
                String pathFileName = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file,
                        MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, true);
                urls.add(getBaseUrl(request) + pathFileName);
                newFileNames.add(FileUtils.getName(pathFileName));
                originalFilenames.add(originalFilename);
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
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


    /**
     * 本地资源通用下载
     */
    @Operation(summary = "本地资源通用下载")
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + FileUtils.stripPrefix(resource);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
