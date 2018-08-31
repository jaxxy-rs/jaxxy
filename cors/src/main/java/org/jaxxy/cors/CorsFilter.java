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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            request.abortWith(handlePreflight(request));
            request.setProperty(PREFLIGHT_FLAG_PROP, Boolean.TRUE);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        if (!Boolean.TRUE.equals(request.getProperty(PREFLIGHT_FLAG_PROP))) {
            final MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
            final String origin = request.getHeaderString(AccessControlHeaders.ORIGIN);
            responseHeaders.add(HttpHeaders.VARY, AccessControlHeaders.ORIGIN);
            if (policy.isAllowedOrigin(origin)) {
                log.debug("Handling simple CORS request: {} {}", request.getMethod(), request.getUriInfo().getPath());
                policy.getExposedHeaders().forEach(header -> responseHeaders.add(AccessControlHeaders.EXPOSE_HEADERS, header));
                responseHeaders.add(AccessControlHeaders.ALLOW_ORIGIN, origin);
                if (policy.isAllowCredentials()) {
                    responseHeaders.add(AccessControlHeaders.ALLOW_CREDENTIALS, true);
                }
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Response handlePreflight(ContainerRequestContext request) {
        final String origin = headers.getHeaderString(AccessControlHeaders.ORIGIN);
        if (!policy.isAllowedOrigin(origin)) {
            return failedPreflight();
        }

        final String method = headers.getHeaderString(AccessControlHeaders.REQUEST_METHOD);
        if (!policy.isAllowedMethod(method)) {
            return failedPreflight();
        }

        final List<String> requestHeaders = Optional.ofNullable(headers.getRequestHeader(AccessControlHeaders.REQUEST_HEADERS)).orElse(Collections.emptyList());
        if (!policy.headersAllowed(requestHeaders)) {
            return failedPreflight();
        }

        final Response.ResponseBuilder builder = Response.noContent();
        builder.header(HttpHeaders.VARY, AccessControlHeaders.ORIGIN);
        builder.header(AccessControlHeaders.ALLOW_ORIGIN, origin);
        if (policy.isAllowCredentials()) {
            builder.header(AccessControlHeaders.ALLOW_CREDENTIALS, true);
        }
        builder.header(AccessControlHeaders.MAX_AGE, policy.getMaxAge());

        policy.getAllowedMethods().stream()
                .filter(m -> !isSimpleMethod(m))
                .forEach(m -> builder.header(AccessControlHeaders.ALLOW_METHODS, m));

        requestHeaders.stream()
                .filter(h -> !isSimpleHeader(h))
                .forEach(h -> builder.header(AccessControlHeaders.ALLOW_HEADERS, h));

        return builder.build();
    }
}