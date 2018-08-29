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

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jaxxy.cors.AccessControlHeaders.PRAGMA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlHeadersTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Test
    public void requestHeadersShouldBeEmptyIfMissing() {
        when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>());

        assertThat(AccessControlHeaders.requestHeadersOf(requestContext)).isEmpty();
    }

    @Test
    public void requestHeadersShouldFilterOutSimpleHeaders() {
        final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put(AccessControlHeaders.REQUEST_HEADERS, Arrays.asList(
                "Foo",
                "Bar",
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.CONTENT_LANGUAGE,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.EXPIRES,
                HttpHeaders.LAST_MODIFIED,
                PRAGMA));
        when(requestContext.getHeaders()).thenReturn(headers);

        assertThat(AccessControlHeaders.requestHeadersOf(requestContext)).isEqualTo(Arrays.asList("Foo", "Bar"));
    }

    @Test
    public void testIsPreFlight() {
        when(requestContext.getMethod()).thenReturn(HttpMethod.DELETE);
        assertThat(AccessControlHeaders.isPreflight(requestContext)).isFalse();

        when(requestContext.getMethod()).thenReturn(HttpMethod.OPTIONS);
        assertThat(AccessControlHeaders.isPreflight(requestContext)).isFalse();

        when(requestContext.getHeaderString(AccessControlHeaders.ORIGIN)).thenReturn("http://bogushost.com");
        assertThat(AccessControlHeaders.isPreflight(requestContext)).isFalse();

        when(requestContext.getHeaderString(AccessControlHeaders.REQUEST_METHOD)).thenReturn(HttpMethod.GET);
        assertThat(AccessControlHeaders.isPreflight(requestContext)).isTrue();
    }
}