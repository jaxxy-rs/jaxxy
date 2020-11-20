/*
 * Copyright (c) 2020 The Jaxxy Authors.
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

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;

import static java.util.Optional.ofNullable;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Builder
@AllArgsConstructor
public class ContainerTokenAuthFilter implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String BEARER_SCHEME = "Bearer";
    private static final String SPACE = " ";
    private static final String HEADER_PREFIX = BEARER_SCHEME + SPACE;
    private static final int HEADER_PREFIX_LENGTH = HEADER_PREFIX.length();

    private final TokenAuthenticator authenticator;

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        final SecurityContext securityContext = ofNullable(request.getHeaderString(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(HEADER_PREFIX))
                .map(header -> header.substring(HEADER_PREFIX_LENGTH))
                .map(authenticator::authenticate)
                .orElseThrow(() -> new NotAuthorizedException(BEARER_SCHEME));

        request.setSecurityContext(securityContext);
    }
}
