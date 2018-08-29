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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;


@Provider
@PreMatching
@Getter
@Builder
@RequiredArgsConstructor
@Slf4j
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String PREFLIGHT_FLAG_PROP = "CorsFilter.preflightFlag";
    private static final Set<String> SIMPLE_RESPONSE_HEADERS = new HashSet<>(Arrays.asList(
            "CACHE-CONTROL",
            "CONTENT-LANGUAGE",
            "CONTENT-TYPE",
            "EXPIRES",
            "LAST-MODIFIED",
            "PRAGMA"
    ));

    @Singular
    private final Set<String> allowedOrigins;
    @Singular
    private final Set<String> allowedMethods;
    @Singular
    private final Set<String> allowedHeaders;
    @Singular
    private final Set<String> exposedHeaders;
    private final boolean allowCredentials;
    private final long maxAge;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * A factory method to return a filter configured with sensible defaults:
     * <ul>
     * <li>allowedOrigins = {}</li>
     * <li>allowedMethods = {"GET", "POST", "PUT", "DELETE", "HEAD"}</li>
     * <li>allowCredentials = false</li>
     * <li>allowedHeaders = {}</li>
     * <li>exposedHeaders = {}</li>
     * <li>maxAge = 1 day (in seconds)</li>
     * </ul>
     *
     * @return the filter
     */
    public static CorsFilter.CorsFilterBuilder defaultFilter() {
        return builder()
                .allowedOrigins(Collections.emptySet())
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.HEAD)
                .allowCredentials(false)
                .allowedHeaders(Collections.emptySet())
                .exposedHeaders(Collections.emptySet())
                .maxAge(TimeUnit.DAYS.toSeconds(1));
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        if (AccessControlHeaders.isPreflight(request)) {
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
            final MultivaluedMap<String, Object> headers = response.getHeaders();
            final String origin = request.getHeaderString(AccessControlHeaders.ORIGIN);
            if (origin != null && isAllowedOrigin(origin)) {
                log.debug("Handling simple CORS request: {} {}", request.getMethod(), request.getUriInfo().getPath());
                exposedHeaders.forEach(header -> headers.add(AccessControlHeaders.EXPOSE_HEADERS, header));
                headers.add(AccessControlHeaders.ALLOW_ORIGIN, origin);
                headers.add(AccessControlHeaders.ALLOW_CREDENTIALS, String.valueOf(allowCredentials));
            }
            headers.add(HttpHeaders.VARY, AccessControlHeaders.ORIGIN);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Response handlePreflight(ContainerRequestContext request) {
        final String origin = request.getHeaderString(AccessControlHeaders.ORIGIN);
        if (!isAllowedOrigin(origin)) {
            log.warn("CORS pre-flight failed: origin \"{}\".", origin);
            return failedPreflight();
        }
        final String method = request.getHeaderString(AccessControlHeaders.REQUEST_METHOD);
        if (!isWhitelisted(allowedMethods, method)) {
            log.warn("CORS pre-flight failed: method \"{}\".", method);
            return failedPreflight();
        }
        final List<String> requestHeaders = request.getHeaders().getOrDefault(AccessControlHeaders.REQUEST_HEADERS, new LinkedList<>()).stream().filter(header -> !SIMPLE_RESPONSE_HEADERS.contains(header)).collect(Collectors.toList());
        final Optional<String> invalidHeader = requestHeaders.stream().filter(requestHeader -> !isWhitelisted(allowedHeaders, requestHeader.toUpperCase())).findFirst();
        if (invalidHeader.isPresent()) {
            log.warn("CORS pre-flight failed: header \"{}\".", invalidHeader.get());
            return failedPreflight();
        }
        final Response.ResponseBuilder builder = Response.noContent();
        builder.header(HttpHeaders.VARY, AccessControlHeaders.ORIGIN);
        builder.header(AccessControlHeaders.ALLOW_ORIGIN, origin);
        builder.header(AccessControlHeaders.ALLOW_CREDENTIALS, String.valueOf(allowCredentials));
        builder.header(AccessControlHeaders.MAX_AGE, String.valueOf(maxAge));
        allowedMethods.forEach(allowedMethod -> builder.header(AccessControlHeaders.ALLOW_METHODS, allowedMethod));
        requestHeaders.forEach(requestHeader -> builder.header(AccessControlHeaders.ALLOW_HEADERS, requestHeader));
        return builder.build();
    }

    private boolean isAllowedOrigin(String origin) {
        return isWhitelisted(allowedOrigins, origin);
    }

    static boolean isWhitelisted(Set<String> acceptedValues, String value) {
        return acceptedValues == null ||
                acceptedValues.isEmpty() ||
                acceptedValues.contains(value);
    }

    private Response failedPreflight() {
        return Response.noContent().header(HttpHeaders.VARY, AccessControlHeaders.ORIGIN).build();
    }

}