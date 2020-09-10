package com.truemoney.api.springheadersrelay.lib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
public class HeaderRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private final List<String> headerInclude;

    HeaderRestTemplateInterceptor(List<String> headerInclude) {
        this.headerInclude = headerInclude;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        if (RequestContextHolder.getRequestAttributes() != null) {
            // Get httpServletRequest from dispatcherServlet
            HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // inject http headers to rest template request.
            this.prepareHeaders(request.getHeaders(), curRequest);
        } else {
            log.error("out of request scope dispatcherServlet, not work with netty(non blocking) or async.");
        }

        request.getHeaders().forEach((k, v) ->
                log.info("Result request header key: {}  value: {}", k, v)
        );

        return execution.execute(request, body);
    }

    private void prepareHeaders(HttpHeaders requestHeader, HttpServletRequest curRequest) {
        for (String headerKey : headerInclude) {
            if (!StringUtils.isEmpty(curRequest.getHeader(headerKey))) {
                requestHeader.add(headerKey, curRequest.getHeader(headerKey));
                log.info("Added header k :{}, v :{}", headerKey, curRequest.getHeader(headerKey));
            }
        }
    }
}
