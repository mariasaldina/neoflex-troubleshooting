package com.troubleshooting.user.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "masking")
public record MaskingConfig(
        List<MaskingObj> replaceList
) {}