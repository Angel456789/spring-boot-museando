package com.museando.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/logos/**")
                .addResourceLocations("file:c:/museando/img-museos/");

        registry.addResourceHandler("/galeria/**")
                .addResourceLocations("file:c:/museando/img-galeria/");
        
        registry.addResourceHandler("/obras/**")  
        .addResourceLocations("file:c:/museando/img-obras/");
        
        registry.addResourceHandler("/voluntarios/**")
        .addResourceLocations("file:c:/museando/voluntariado-archivos/");
    }

}