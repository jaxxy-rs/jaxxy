/*
 * Copyright (c) 2018-2023 The Jaxxy Authors.
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

import com.fasterxml.jackson.jakarta.rs.cbor.JacksonCBORProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jakarta.rs.smile.JacksonSmileProvider;
import com.fasterxml.jackson.jakarta.rs.yaml.JacksonYAMLProvider;
import com.google.gson.Gson;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.jaxxy.cors.CorsFilter;
import org.jaxxy.cors.ResourceSharingPolicy;
import org.jaxxy.example.service.DefaultHelloService;
import org.jaxxy.example.service.HelloService;
import org.jaxxy.gson.GsonMessageBodyProvider;
import org.jaxxy.jsonb.JsonbMessageBodyProvider;
import org.jaxxy.logging.RequestLogFilter;
import org.jaxxy.logging.mdc.DefaultRichMdc;
import org.jaxxy.logging.mdc.MdcCleanupFilter;
import org.jaxxy.logging.mdc.MdcHeadersFilter;
import org.jaxxy.logging.mdc.MdcResourceInfoFilter;
import org.jaxxy.logging.mdc.MdcUriInfoFilter;
import org.jaxxy.logging.mdc.MdcValueEncoder;
import org.jaxxy.logging.mdc.RichMdc;
import org.jaxxy.protobuf.ProtobufMessageBodyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public CorsFilter corsFilter(ResourceSharingPolicy policy) {
        return new CorsFilter(policy);
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.json.provider", matchIfMissing = true, havingValue = "gson")
    public GsonMessageBodyProvider gsonMessageBodyProvider(Gson gson) {
        return new GsonMessageBodyProvider(gson);
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.gzipFeature.enabled", matchIfMissing = true, havingValue = "true")
    public GZIPFeature gzipFeature() {
        return new GZIPFeature();
    }

    @Bean
    public HelloService helloService() {
        return new DefaultHelloService();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.json.provider", havingValue = "jackson")
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JacksonJsonProvider();
    }

    @Bean
    public JacksonCBORProvider jacksonCborProvider() {
        return new JacksonCBORProvider();
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
    @ConditionalOnProperty(name = "jaxxy.json.provider", havingValue = "jsonb")
    public JsonbMessageBodyProvider jsonbMessageBodyProvider() {
        return new JsonbMessageBodyProvider();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.loggingFeature.enabled", matchIfMissing = true, havingValue = "true")
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

    @Bean
    public MdcCleanupFilter mdcCleanupFilter() {
        return new MdcCleanupFilter();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.mdc.headers.enabled", matchIfMissing = true, havingValue = "true")
    public MdcHeadersFilter mdcHeadersFilter(RichMdc mdc) {
        return new MdcHeadersFilter(mdc);
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.mdc.resourceInfo.enabled", matchIfMissing = true, havingValue = "true")
    public MdcResourceInfoFilter mdcResourceInfoFilter(RichMdc mdc) {
        return new MdcResourceInfoFilter(mdc);
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.mdc.uriInfo.enabled", matchIfMissing = true, havingValue = "true")
    public MdcUriInfoFilter mdcUriInfoFilter(RichMdc mdc) {
        return new MdcUriInfoFilter(mdc);
    }

    @Bean
    @ConditionalOnMissingBean
    public MdcValueEncoder mdcValueEncoder(Gson gson) {
        return gson::toJson;
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.openapi.enabled", matchIfMissing = true, havingValue = "true")
    public OpenApiFeature openApiFeature() {
        return new OpenApiFeature();
    }

    @Bean
    public ProtobufMessageBodyProvider protobufMessageBodyProvider() {
        return new ProtobufMessageBodyProvider();
    }

    @Bean
    @ConditionalOnProperty(name = "jaxxy.requestLog.enabled", matchIfMissing = true, havingValue = "true")
    public RequestLogFilter requestLogFilter() {
        return RequestLogFilter.builder()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceSharingPolicy resourceSharingPolicy() {
        return ResourceSharingPolicy.defaultPolicy();
    }

    @Bean
    public RichMdc richMdc(MdcValueEncoder encoder) {
        return new DefaultRichMdc(encoder);
    }
}
