package com.truemoney.api.springheadersrelay.demo.controller;

import com.truemoney.api.springheadersrelay.demo.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    RestService restService;

    @GetMapping("/v1/demo")
    public String getDemo() {
        return restService.getRestCall();
    }

    @PostMapping("/v1/demo")
    public String postDemo() {
        return restService.getRestCall();
    }
}
