package com.example.memorygame.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    // Mantemos APENAS a configuração para servir as imagens da pasta uploads
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:uploads/")
    }

    // REMOVEMOS O addCorsMappings DAQUI POIS O SECURITYCONFIG JÁ ESTÁ FAZENDO ISSO.
}