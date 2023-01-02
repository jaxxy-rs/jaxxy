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

package org.jaxxy.cors;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.DefaultHelloResource;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jaxxy.cors.AccessControlHeaders.ORIGIN;
import static org.jaxxy.cors.AccessControlHeaders.REQUEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CorsFilterTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ALLOWED_ORIGIN = "http://localhost/";
    private ResourceSharingPolicy policy;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        policy = new ResourceSharingPolicy()
                .allowOrigins(ALLOWED_ORIGIN)
                .allowMethods("GET", "PUT", "POST")
                .exposeHeaders("Jaxxy-Foo", "Jaxxy-Bar")
                .allowHeaders("Jaxxy-Foo", HttpHeaders.CONTENT_LANGUAGE);
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new CorsFilter(policy));
    }

    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Test
    void testPreflightCorsRequest() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header(ORIGIN, ALLOWED_ORIGIN)
                .header(REQUEST_METHOD, HttpMethod.GET)
                .header(AccessControlHeaders.REQUEST_HEADERS, "Jaxxy-Foo")
                .header(AccessControlHeaders.REQUEST_HEADERS, HttpHeaders.CONTENT_LANGUAGE)
                .options();
        assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
        assertEquals("Jaxxy-Foo", response.getHeaderString(AccessControlHeaders.ALLOW_HEADERS));
    }

    @Test
    void testPreflightCorsRequestWithSimpleHeader() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header(ORIGIN, ALLOWED_ORIGIN)
                .header(REQUEST_METHOD, HttpMethod.GET)
                .header(AccessControlHeaders.REQUEST_HEADERS, HttpHeaders.CONTENT_LANGUAGE)
                .options();
        assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
        assertNull(response.getHeaderString(AccessControlHeaders.ALLOW_HEADERS));
    }

    @Test
    void testPreflightCorsRequestWithInvalidHeader() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .header("Access-Control-Request-Headers", "Jaxxy-Bar")
                .options();

        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    void testPreflightCorsRequestWithInvalidMethod() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.HEAD)
                .options();
        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    void testPreflightCorsRequestWithInvalidOrigin() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", "http://somebogusorigin/")
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    void testPreflightCorsRequestWithNoOrigin() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    void testSimpleCorsRequest() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
        assertThat(response.getStringHeaders().get(AccessControlHeaders.EXPOSE_HEADERS)).hasSize(2).contains("Jaxxy-Foo").contains("Jaxxy-Bar");
    }

    @Test
    void testSimpleCorsRequestWithAllowsCredentials() {
        policy.allowCredentials();
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        assertThat(response.getHeaderString(AccessControlHeaders.ALLOW_ORIGIN)).isEqualTo(ALLOWED_ORIGIN);
        assertThat(response.getHeaderString(HttpHeaders.VARY)).isEqualTo(AccessControlHeaders.ORIGIN);
        assertThat(response.getStringHeaders().get(AccessControlHeaders.EXPOSE_HEADERS)).hasSize(2).contains("Jaxxy-Foo").contains("Jaxxy-Bar");
        assertThat(response.getHeaderString(AccessControlHeaders.ALLOW_CREDENTIALS)).isEqualTo("true");
    }

    @Test
    void testPreflightCorsRequestWithAllowsCredentials() {
        policy.allowCredentials();
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header(ORIGIN, ALLOWED_ORIGIN)
                .header(REQUEST_METHOD, HttpMethod.GET)
                .header(AccessControlHeaders.REQUEST_HEADERS, "Jaxxy-Foo")
                .header(AccessControlHeaders.REQUEST_HEADERS, HttpHeaders.CONTENT_LANGUAGE)
                .options();

        assertThat(response.getHeaderString(AccessControlHeaders.ALLOW_ORIGIN)).isEqualTo(ALLOWED_ORIGIN);
        assertThat(response.getHeaderString(HttpHeaders.VARY)).isEqualTo(AccessControlHeaders.ORIGIN);
        assertThat(response.getHeaderString(AccessControlHeaders.ALLOW_HEADERS)).isEqualTo("Jaxxy-Foo");
    }
}