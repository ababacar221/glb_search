package com.opengeode.dev.glb_search.config;

import com.opengeode.dev.glb_search.model.MessageStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public MessageStorage customerStorage() {
        return new MessageStorage();
    }
}
