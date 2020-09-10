package com.truemoney.api.springheadersrelay.lib;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "header")
public class HeaderConfiguration {
    @Getter
    List<String> include = new ArrayList<>();
}
