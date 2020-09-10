package com.truemoney.api.springheadersrelay.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestService {
    @Autowired
    @Qualifier("sampleRestTemplate")
    private RestTemplate restTemplate;

    public String getRestCall() {
        log.info("start get rest call");
        return restTemplate.getForObject("https://www.google.com/", String.class);
    }

    public String postRestCall() {
        log.info("start post rest call");
        log.info("start get rest call");
        return restTemplate.postForObject("https://www.google.com/", "request", String.class);
    }

}
