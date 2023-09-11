package com.yerbo.engbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:environment.properties")
public class SpringConfig {

}
