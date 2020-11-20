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

package org.jaxxy.cors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

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

    //
    // Response Headers
    //
    static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    static final String MAX_AGE = "Access-Control-Max-Age";
    static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
    static final String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";


    private static final Set<String> SIMPLE_METHODS = new HashSet<>(Arrays.asList(
       HttpMethod.GET,
       HttpMethod.HEAD,
       HttpMethod.POST
    ));

    private static final Set<String> SIMPLE_HEADERS = Stream.of(
            HttpHeaders.ACCEPT,
            HttpHeaders.ACCEPT_LANGUAGE,
            HttpHeaders.CONTENT_LANGUAGE
    ).collect(Collectors.toCollection(() -> new TreeSet<>(String::compareToIgnoreCase)));

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    static boolean isPreflight(ContainerRequestContext request) {
        return HttpMethod.OPTIONS.equals(request.getMethod()) &&
                request.getHeaderString(ORIGIN) != null &&
                request.getHeaderString(REQUEST_METHOD) != null;
    }

    static Response failedPreflight() {
        return Response.noContent().header(HttpHeaders.VARY, ORIGIN).build();
    }

    static boolean isSimpleMethod(String method) {
        return SIMPLE_METHODS.contains(method);
    }

    static boolean isSimpleHeader(String header) {
        return SIMPLE_HEADERS.contains(header);
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private AccessControlHeaders() {
    }
}
