/*
 * Copyright (c) 2018 The Jaxxy Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jaxxy.example.config;

import java.util.List;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.smile.JacksonSmileProvider;
import com.fasterxml.jackson.jaxrs.yaml.JacksonYAMLProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.jaxxy.cors.CorsFilter;
import org.jaxxy.cors.ResourceSharingPolicy;
import org.jaxxy.example.service.DefaultHelloService;
import org.jaxxy.example.service.HelloService;
import org.jaxxy.gson.GsonMessageBodyProvider;
import org.jaxxy.jsonb.JsonbMessageBodyProvider;
import org.jaxxy.logging.LoggingContextDecorator;
import org.jaxxy.logging.LoggingContextFilter;
import org.jaxxy.logging.RequestLogFilter;
import org.jaxxy.logging.decorator.HeadersDecorator;
import org.jaxxy.logging.decorator.ResourceDecorator;
import org.jaxxy.protobuf.ProtobufMessageBodyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxxyExampleConfiguration {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Bean
    @ConditionalOnProperty(name = "jaxxy.cors.enabled", matchIfMissing = true, havingValue = "true")
    public CorsFilter corsFilter() {
        return new CorsFilter(ResourceSharingPolicy.defaultPolicy());
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.gzipFeature.enabled", matchIfMissing = true, havingValue = "true")
    public GZIPFeature gzipFeature() {
        return new GZIPFeature();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.loggingContext.headers.enabled", matchIfMissing = true, havingValue = "true")
    public HeadersDecorator headersDecorator() {
        return new HeadersDecorator();
    }

    @Bean
    public ProtobufMessageBodyProvider protobufMessageBodyProvider() {
        return new ProtobufMessageBodyProvider();
    }

    @Bean
    public JacksonSmileProvider jacksonSmileProvider() {
        return new JacksonSmileProvider();
    }

    @Bean
    public JacksonYAMLProvider jacksonYAMLProvider() {
        return new JacksonYAMLProvider();
    }

    @Bean
    public HelloService helloService() {
        return new DefaultHelloService();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.json.provider", havingValue = "jsonb")
    public JsonbMessageBodyProvider jsonbMessageBodyProvider() {
        return new JsonbMessageBodyProvider();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.json.provider", matchIfMissing = true, havingValue = "gson")
    public GsonMessageBodyProvider gsonMessageBodyProvider() {
        return new GsonMessageBodyProvider();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.json.provider", havingValue = "jackson")
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JacksonJsonProvider();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.loggingContext.enabled", matchIfMissing = true, havingValue = "true")
    public LoggingContextFilter loggingContextFilter(List<LoggingContextDecorator> decorators) {
        return new LoggingContextFilter(decorators);
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.loggingFeature.enabled", matchIfMissing = true, havingValue = "true")
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.requestLog.enabled", matchIfMissing = true, havingValue = "true")
    public RequestLogFilter requestLogFilter() {
        return RequestLogFilter.builder().build();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.loggingContext.resource.enabled", matchIfMissing = true, havingValue = "true")
    public ResourceDecorator resourceDecorator() {
        return new ResourceDecorator();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.swagger.enabled", matchIfMissing = true, havingValue = "true")
    public Swagger2Feature swagger2Feature() {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setScanAllResources(true);
        return feature;
    }
}
