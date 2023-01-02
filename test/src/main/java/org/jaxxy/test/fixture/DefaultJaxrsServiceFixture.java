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

package org.jaxxy.test.fixture;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configurable;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.jaxxy.test.fixture.DefaultJaxrsServiceFixtureFactory.SPLIT_HEADERS_PROP;

@RequiredArgsConstructor
class DefaultJaxrsServiceFixture<T> implements JaxrsServiceFixture<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<T> serviceInterface;
    private final Server server;
    private final List<Object> clientProviders;
    private final List<Feature> clientFeatures;

//----------------------------------------------------------------------------------------------------------------------
// AutoCloseable Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void close() {
        server.destroy();
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceFixture Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String baseUrl() {
        return server.getEndpoint().getEndpointInfo().getAddress();
    }

    @Override
    public T createClientProxy() {
        try {
            return configure(RestClientBuilder.newBuilder())
                    .baseUrl(new URL(baseUrl()))
                    .build(serviceInterface);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Bad base URL.", e);
        }
    }

    @Override
    public WebTarget createWebTarget() {
        return configure(ClientBuilder.newBuilder())
                .build()
                .target(baseUrl());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <C extends Configurable<C>> C configure(C configurable) {
        clientFeatures.forEach(configurable::register);
        clientProviders.forEach(configurable::register);
        return configurable
                .property(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE)
                .property(SPLIT_HEADERS_PROP, true);
    }
}
