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

package org.jaxxy.security.token;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jaxxy.security.basic.ClientBasicAuthFilter;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.DefaultHelloResource;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContainerTokenAuthFilterTest extends JaxrsTestCase<HelloResource> {
    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(ContainerTokenAuthFilter.builder()
                        .authenticator((token) -> {
                            if ("ABC123".equals(token)) {
                                return new SimpleSecurityContext("Jaxxy");
                            } else {
                                return null;
                            }
                        })
                        .build());
    }

    @Test
    void should200WhenValidToken() {
        final Response response = webTarget().path("hello").path("Jaxxy")
                .register(new ClientTokenAuthFilter(() -> "ABC123"))
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("Hello, Jaxxy!");
    }

    @Test
    void should401WhenAuthenticationHeaderMissing() {
        final Response response = webTarget().path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeaderString("WWW-Authenticate")).isEqualTo("Bearer");
    }

    @Test
    void should401WhenWrongAuthenticationType() {
        final Response response = webTarget().path("hello").path("Jaxxy")
                .register(new ClientBasicAuthFilter("user1", "password1"))
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeaderString("WWW-Authenticate")).isEqualTo("Bearer");
    }

    @Test
    void should401WhenInvalidToken() {
        final Response response = webTarget().path("hello").path("Jaxxy")
                .register(new ClientTokenAuthFilter(() -> "BADTOKEN"))
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeaderString("WWW-Authenticate")).isEqualTo("Bearer");
    }
}