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

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessControlHeadersTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Test
    void testIsPreFlight() {
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