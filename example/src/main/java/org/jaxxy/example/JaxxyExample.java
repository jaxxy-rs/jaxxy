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

package org.jaxxy.example;

import java.util.Arrays;
import java.util.Collections;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.jaxxy.jsonb.JsonbMessageBodyProvider;
import org.jaxxy.logging.LoggingContextFilter;
import org.jaxxy.logging.RequestLogFilter;
import org.jaxxy.logging.decorator.HeadersDecorator;
import org.jaxxy.logging.decorator.ResourceDecorator;
import org.jaxxy.logging.decorator.UriDecorator;

public class JaxxyExample {
    public static void main(String[] args) throws Exception {
        final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setResourceClasses(HelloResource.class);
        factory.setResourceProvider(HelloResource.class, new SingletonResourceProvider(new DefaultHelloResource(), true));
        factory.setAddress("http://localhost:9000");
        factory.setProviders(Arrays.asList(
                new JsonbMessageBodyProvider(),
                new LoggingContextFilter(
                        new HeadersDecorator(),
                        new UriDecorator(),
                        new ResourceDecorator()
                ),
                RequestLogFilter.builder().build())
        );
        factory.setFeatures(Collections.singletonList(
                new LoggingFeature()
        ));
        final Server server = factory.create();
        System.in.read();
        server.stop();
        server.destroy();
    }
}
