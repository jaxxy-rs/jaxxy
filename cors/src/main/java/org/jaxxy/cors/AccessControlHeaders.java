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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

class AccessControlHeaders {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    //
    // Request Headers
    //
    static final String ORIGIN = "Origin";
    static final String REQUEST_METHOD = "Access-Control-Request-Method";
    static final String REQUEST_HEADERS = "Access-Control-Request-Headers";
    static final String PRAGMA = "Pragma";

    //
    // Response Headers
    //
    static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    static final String MAX_AGE = "Access-Control-Max-Age";
    static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
    static final String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";


    private static final Set<String> SIMPLE_RESPONSE_HEADERS = new HashSet<>(Arrays.asList(
            HttpHeaders.CACHE_CONTROL,
            HttpHeaders.CONTENT_LANGUAGE,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.EXPIRES,
            HttpHeaders.LAST_MODIFIED,
            PRAGMA
    ));

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    static List<String> requestHeadersOf(ContainerRequestContext request) {
        return request.getHeaders().getOrDefault(REQUEST_HEADERS, new LinkedList<>()).stream().filter(header -> !SIMPLE_RESPONSE_HEADERS.contains(header)).collect(Collectors.toList());
    }

    static boolean isPreflight(ContainerRequestContext request) {
        return HttpMethod.OPTIONS.equals(request.getMethod()) &&
                request.getHeaderString(ORIGIN) != null &&
                request.getHeaderString(REQUEST_METHOD) != null;
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private AccessControlHeaders() {
    }
}
