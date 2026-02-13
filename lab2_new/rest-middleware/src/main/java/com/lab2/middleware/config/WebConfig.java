package com.lab2.middleware.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add JAXB XML converter
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        
        // Set packages to scan for JAXB annotated classes
        marshaller.setPackagesToScan("com.lab2.middleware.model");
        xmlConverter.setMarshaller(marshaller);
        xmlConverter.setUnmarshaller(marshaller);
        
        converters.add(xmlConverter);
    }
}