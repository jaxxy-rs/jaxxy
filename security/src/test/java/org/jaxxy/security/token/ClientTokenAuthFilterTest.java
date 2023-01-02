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

import jakarta.ws.rs.core.HttpHeaders;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.echo.DefaultEchoHeaderResource;
import org.jaxxy.test.echo.EchoHeaderResource;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTokenAuthFilterTest extends JaxrsTestCase<EchoHeaderResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withClientProvider(ClientTokenAuthFilter.builder()
                        .tokenSupplier(() -> "FooBarBaz")
                        .build());
    }

    @Override
    protected EchoHeaderResource createServiceObject() {
        return new DefaultEchoHeaderResource(HttpHeaders.AUTHORIZATION);
    }

    @Test
    void testFilter() {
        assertThat(clientProxy().echo()).isEqualTo("Bearer FooBarBaz");
    }
}