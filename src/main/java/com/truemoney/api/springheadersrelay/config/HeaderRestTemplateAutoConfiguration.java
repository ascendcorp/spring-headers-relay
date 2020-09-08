package com.truemoney.api.springheadersrelay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(RestTemplate.class)
@AutoConfigureBefore(HttpClientConfiguration.class)
@Slf4j
public class HeaderRestTemplateAutoConfiguration {
    @Configuration
    protected class HeaderInterceptorConfiguration {
        @Autowired
        private BeanFactory beanFactory;

        @Bean
        HeaderRestTemplateBeanPostProcessor headerRestTemplateBeanPostProcessor(
                ListableBeanFactory beanFactory) {
            return new HeaderRestTemplateBeanPostProcessor();
        }

        @Bean
        @Order
        RestTemplateCustomizer traceRestTemplateCustomizer() {
            return new HeaderRestTemplateCustomizer(new HeaderRestTemplateInterceptor());
        }
    }

    class HeaderRestTemplateCustomizer implements RestTemplateCustomizer {

        private final ClientHttpRequestInterceptor interceptor;

        HeaderRestTemplateCustomizer(ClientHttpRequestInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public void customize(RestTemplate restTemplate) {
            new RestTemplateInterceptorInjector(this.interceptor).inject(restTemplate);
        }

    }

    class HeaderRestTemplateBeanPostProcessor implements BeanPostProcessor {
        HeaderRestTemplateBeanPostProcessor(){

        }
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName)
                throws BeansException {
            if (bean instanceof RestTemplate) {
                RestTemplate rt = (RestTemplate) bean;
                new RestTemplateInterceptorInjector(interceptor()).inject(rt);
            }
            return bean;
        }

        private HeaderRestTemplateInterceptor interceptor() {
            return new HeaderRestTemplateInterceptor();
        }

    }

    class RestTemplateInterceptorInjector {

        private final ClientHttpRequestInterceptor interceptor;

        RestTemplateInterceptorInjector(ClientHttpRequestInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        void inject(RestTemplate restTemplate) {
            if (hasHeaderInterceptor(restTemplate)) {
                return;
            }
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(
                    restTemplate.getInterceptors());
            interceptors.add(0, this.interceptor);
            restTemplate.setInterceptors(interceptors);
        }

        private boolean hasHeaderInterceptor(RestTemplate restTemplate) {
            for (ClientHttpRequestInterceptor interceptor : restTemplate.getInterceptors()) {
                if (interceptor instanceof HeaderRestTemplateInterceptor) {
                    return true;
                }
            }
            return false;
        }
    }
}
