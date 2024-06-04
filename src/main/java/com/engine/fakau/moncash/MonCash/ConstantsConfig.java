package com.engine.fakau.moncash.MonCash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantsConfig {

    @Value("${moncash.client.id}")
    public String clientId;

    @Value("${moncash.client.secret}")
    public String clientSecret;

}
