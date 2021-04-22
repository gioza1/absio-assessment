package com.sample.configuration.beans;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(
        basePackages = {
                "com.sample"
        }
)
@PropertySources({
        @PropertySource("classpath:/properties/sample-service.properties"),
        @PropertySource(value = "classpath:/properties/sample-service-local.properties", ignoreResourceNotFound = true)
})
public class CoreBeansConfiguration {
    @Bean
    @Primary
    public ModelMapper defaultModelMapper() {
        return new ModelMapper();
    }

    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
