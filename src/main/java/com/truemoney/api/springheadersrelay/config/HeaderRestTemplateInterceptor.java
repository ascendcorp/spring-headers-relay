package com.truemoney.api.springheadersrelay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class HeaderRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    HeaderRestTemplateInterceptor() {
    }

    // mock header name,
    // TODO next get header from config file.
    private static final String HEADER_X_LOCATION = "X-location";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().forEach((k, v) ->
                log.info("before key: {}  value: {}", k, v)
        );

        if (RequestContextHolder.getRequestAttributes() != null) {
            // Get httpServletRequest from dispatcherServlet
            HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // inject http headers to rest template request.
            this.prepareHeaders(request.getHeaders(), curRequest);
        } else {
            log.error("out of request scope DispatcherServlet.");
        }
        log.info("hello world auto interceptor.");

        request.getHeaders().forEach((k, v) ->
                log.info("key: {}  value: {}", k, v)
        );


        return execution.execute(request, body);
    }

    private void prepareHeaders(HttpHeaders requestHeader, HttpServletRequest curRequest) {
        Enumeration<String> c = curRequest.getHeaderNames();
        while (c.hasMoreElements()) {
            String header = c.nextElement();
            if (HEADER_X_LOCATION.equalsIgnoreCase(header)) {
                requestHeader.add(HEADER_X_LOCATION, curRequest.getHeader(header));
            }
            log.info("cur request {} {}", header, curRequest.getHeader(header));
        }
    }
}
