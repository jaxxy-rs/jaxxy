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

package org.jaxxy.security.role;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jaxxy.security.token.ClientTokenAuthFilter;
import org.jaxxy.security.token.ContainerTokenAuthFilter;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;

class RolesAllowedFilterTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new RolesAllowedDynamicFeature())
                .withContainerProvider(new ContainerTokenAuthFilter(token-> new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> "fake";
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return token.equals(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return true;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "bogus";
                    }
                }));
    }

    @Override
    protected HelloResource createServiceObject() {
        return new ProtectedHelloResource();
    }

    @Test
    void should403WhenNotInRole() {
        final Response response = webTarget()
                .register(new ClientTokenAuthFilter(() -> "notarole"))
                .path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void shouldSucceedWhenInFirstRole() {
        final Response response = webTarget()
                .register(new ClientTokenAuthFilter(() -> "foo"))
                .path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void shouldSucceedWhenInLastRole() {
        final Response response = webTarget()
                .register(new ClientTokenAuthFilter(() -> "baz"))
                .path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }
}