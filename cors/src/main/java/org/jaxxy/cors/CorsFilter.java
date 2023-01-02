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

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;
import static org.jaxxy.cors.AccessControlHeaders.ALLOW_CREDENTIALS;
import static org.jaxxy.cors.AccessControlHeaders.ALLOW_HEADERS;
import static org.jaxxy.cors.AccessControlHeaders.ALLOW_METHODS;
import static org.jaxxy.cors.AccessControlHeaders.ALLOW_ORIGIN;
import static org.jaxxy.cors.AccessControlHeaders.EXPOSE_HEADERS;
import static org.jaxxy.cors.AccessControlHeaders.MAX_AGE;
import static org.jaxxy.cors.AccessControlHeaders.ORIGIN;
import static org.jaxxy.cors.AccessControlHeaders.REQUEST_HEADERS;
import static org.jaxxy.cors.AccessControlHeaders.REQUEST_METHOD;
import static org.jaxxy.cors.AccessControlHeaders.failedPreflight;
import static org.jaxxy.cors.AccessControlHeaders.isPreflight;
import static org.jaxxy.cors.AccessControlHeaders.isSimpleHeader;
import static org.jaxxy.cors.AccessControlHeaders.isSimpleMethod;

@Provider
@PreMatching
@RequiredArgsConstructor
@Slf4j
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String PREFLIGHT_FLAG_PROP = "CorsFilter.preflightFlag";

    private final ResourceSharingPolicy policy;

    @Context
    private HttpHeaders headers;

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        if (isPreflight(request)) {
            log.debug("Handling pre-flight CORS request: {} {}", request.getMethod(), request.getUriInfo().getPath());
            request.setProperty(PREFLIGHT_FLAG_PROP, TRUE);
            request.abortWith(handlePreflight());
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        if (!TRUE.equals(request.getProperty(PREFLIGHT_FLAG_PROP))) {
            final MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
            final String origin = headers.getHeaderString(ORIGIN);
            responseHeaders.add(HttpHeaders.VARY, ORIGIN);
            if (policy.isAllowedOrigin(origin)) {
                log.debug("Handling simple CORS request: {} {}", request.getMethod(), request.getUriInfo().getPath());
                policy.getExposedHeaders().forEach(header -> responseHeaders.add(EXPOSE_HEADERS, header));
                responseHeaders.add(ALLOW_ORIGIN, origin);
                if (policy.isAllowCredentials()) {
                    responseHeaders.add(ALLOW_CREDENTIALS, true);
                }
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Response handlePreflight() {
        final String origin = headers.getHeaderString(ORIGIN);
        if (!policy.isAllowedOrigin(origin)) {
            return failedPreflight();
        }

        final String method = headers.getHeaderString(REQUEST_METHOD);
        if (!policy.isAllowedMethod(method)) {
            return failedPreflight();
        }

        final List<String> requestHeaders = Optional.ofNullable(headers.getRequestHeader(REQUEST_HEADERS)).orElse(Collections.emptyList());
        if (!policy.headersAllowed(requestHeaders)) {
            return failedPreflight();
        }

        final Response.ResponseBuilder builder = Response.noContent();
        builder.header(HttpHeaders.VARY, ORIGIN);
        builder.header(ALLOW_ORIGIN, origin);
        if (policy.isAllowCredentials()) {
            builder.header(ALLOW_CREDENTIALS, true);
        }
        builder.header(MAX_AGE, policy.getMaxAge());

        policy.getAllowedMethods().stream()
                .filter(m -> !isSimpleMethod(m))
                .forEach(m -> builder.header(ALLOW_METHODS, m));

        requestHeaders.stream()
                .filter(h -> !isSimpleHeader(h))
                .forEach(h -> builder.header(ALLOW_HEADERS, h));

        return builder.build();
    }
}