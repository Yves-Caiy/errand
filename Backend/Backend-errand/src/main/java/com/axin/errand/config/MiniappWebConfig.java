package com.axin.errand.config;

import com.axin.errand.interceptor.MiniappAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MiniappWebConfig implements WebMvcConfigurer {
    @Autowired
    private MiniappAuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/miniapp/**")
                .excludePathPatterns("/miniapp/auth/login",
                                     "/miniapp/auth/phone",
                                     "/miniapp/community/list",
                                     "/miniapp/convenienceProduct/list",
                                     "/miniapp/productType/list",
                                     "/miniapp/product/list",
                                     "/miniapp/servicePackage/list",
                                     "/miniapp/takeawayStore/list",
                                     "/miniapp/takeawayProduct/list",
                                     "/miniapp/config/carousel",
                                     "/miniapp/config/privacyPolicy",
                                     "/miniapp/config/withdrawRate",
                                     "/miniapp/translate",
                                     "/miniapp/order/list");
    }
}
