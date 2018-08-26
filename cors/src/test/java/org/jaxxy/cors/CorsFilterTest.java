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

package org.jaxxy.cors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jaxxy.rs.test.JaxrsClientConfig;
import org.jaxxy.rs.test.JaxrsServerConfig;
import org.jaxxy.rs.test.JaxrsTestCase;
import org.jaxxy.rs.test.hello.DefaultHelloResource;
import org.jaxxy.rs.test.hello.HelloResource;
import org.junit.Assert;
import org.junit.Test;

public class CorsFilterTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ALLOWED_ORIGIN = "http://localhost/";

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureClient(JaxrsClientConfig config) {

    }

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        config.withProvider(CorsFilter.allowAll()
                .allowedOrigins(Collections.singleton(ALLOWED_ORIGIN))
                .allowedMethods(new HashSet<>(Arrays.asList("GET", "PUT", "POST")))
                .allowedHeaders(Collections.singleton("Jaxxy-Foo"))
                .build());
    }

    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Test
    public void testPreflightCorsRequest() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        Assert.assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidHeader() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .header("Access-Control-Request-Headers", "Jaxxy-Bar")
                .options();

        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidMethod() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.HEAD)
                .options();
        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidOrigin() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", "http://somebogusorigin/")
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testSimpleCorsRequest() {
        final WebTarget target = webTarget();
        final Response response = target.path("hello").path("Jaxxy")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        Assert.assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }
}