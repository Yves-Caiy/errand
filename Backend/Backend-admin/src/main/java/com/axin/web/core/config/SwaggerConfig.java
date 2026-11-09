package com.axin.web.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.axin.common.config.RuoYiConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger2的接口配置
 * 
 * @author axin
 */
@Configuration
public class SwaggerConfig
{
    /** 系统基础配置 */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    /** 作者邮箱（可选） */
    @Value("${ruoyi.author.email:9856241@qq.com}")
    private String authorEmail;

    /** 作者名称（可选） */
    @Value("${ruoyi.author.name:Axin}")
    private String authorName;

    /** 接口文档简介（可选） */
    @Value("${ruoyi.api.description:描述：后台管理系统接口测试文档.}")
    private String apiDescription;
    
    /**
     * 自定义的 OpenAPI 对象
     */
    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI().components(new Components()
            // 设置认证的请求头
            .addSecuritySchemes("apikey", securityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("apikey"))
            .info(getApiInfo());
    }
    
    @Bean
    public SecurityScheme securityScheme()
    {
        return new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer");
    }
    
    /**
     * 添加摘要信息
     */
    public Info getApiInfo()
    {
        return new Info()
            // 标题
            .title(ruoyiConfig.getName() + " 接口文档")
            // 描述
            .description(apiDescription)
            // 作者信息（名称优先使用 ruoyi.author.name，否则回退为系统名称）
            .contact(new Contact().name(
                (authorName != null && !authorName.isEmpty()) ? authorName : ruoyiConfig.getName()
            ).email(authorEmail))
            // 版本（纯版本号）
            .version(ruoyiConfig.getVersion());
    }
}
