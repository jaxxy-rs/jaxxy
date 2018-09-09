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

import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.jaxxy.cors.CorsFilter;
import org.jaxxy.cors.ResourceSharingPolicy;
import org.jaxxy.example.service.DefaultHelloService;
import org.jaxxy.example.service.HelloService;
import org.jaxxy.jsonb.JsonbMessageBodyProvider;
import org.jaxxy.logging.LoggingContextDecorator;
import org.jaxxy.logging.LoggingContextFilter;
import org.jaxxy.logging.decorator.HeadersDecorator;
import org.jaxxy.logging.decorator.ResourceDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxxyExampleConfiguration {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(ResourceSharingPolicy.defaultPolicy());
    }

    @Bean
    public GZIPFeature gzipFeature() {
        return new GZIPFeature();
    }

    @Bean
    public HeadersDecorator headersDecorator() {
        return new HeadersDecorator();
    }

    @Bean
    public HelloService helloService() {
        return new DefaultHelloService();
    }

    @Bean
    public JsonbMessageBodyProvider jsonbMessageBodyProvider() {
        return new JsonbMessageBodyProvider();
    }

//    @Bean
//    public GsonMessageBodyProvider gsonMessageBodyProvider() {
//        return new GsonMessageBodyProvider();
//    }

    @Bean
    public LoggingContextFilter loggingContextFilter(List<LoggingContextDecorator> decorators) {
        return new LoggingContextFilter(decorators);
    }

    @Bean
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

//    @Bean
//    public RequestLogFilter requestLogFilter() {
//        return RequestLogFilter.builder().build();
//    }

    @Bean
    public ResourceDecorator resourceDecorator() {
        return new ResourceDecorator();
    }

    @Bean
    public Swagger2Feature swagger2Feature() {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setScanAllResources(true);
        return feature;
    }
}
