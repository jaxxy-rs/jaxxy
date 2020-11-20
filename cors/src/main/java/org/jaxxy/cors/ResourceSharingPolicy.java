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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.HttpMethod;

public class ResourceSharingPolicy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ALLOW_ALL = UUID.randomUUID().toString();

    private static final long ONE_DAY = TimeUnit.DAYS.toMinutes(1);

    private final Set<String> allowedOrigins = new HashSet<>();
    private final Set<String> allowedHeaders = new TreeSet<>(String::compareToIgnoreCase);
    private final Set<String> allowedMethods = new HashSet<>();
    private final Set<String> exposedHeaders = new HashSet<>();
    private boolean allowCredentials = false;
    private long maxAge = ONE_DAY;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static ResourceSharingPolicy defaultPolicy() {
        return new ResourceSharingPolicy()
                .allowAllHeaders()
                .allowMethods(HttpMethod.OPTIONS, HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                .allowAllOrigins()
                .withMaxAge(1, TimeUnit.DAYS);
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public Set<String> getAllowedMethods() {
        return allowedMethods;
    }

    public Set<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public ResourceSharingPolicy allowAllHeaders() {
        return allowHeaders(ALLOW_ALL);
    }

    public ResourceSharingPolicy allowAllOrigins() {
        return allowOrigins(ALLOW_ALL);
    }

    public ResourceSharingPolicy allowCredentials() {
        this.allowCredentials = true;
        return this;
    }

    public ResourceSharingPolicy allowHeaders(String... headers) {
        allowedHeaders.addAll(Arrays.asList(headers));
        return this;
    }

    public ResourceSharingPolicy allowMethods(String... methods) {
        allowedMethods.addAll(Arrays.asList(methods));
        return this;
    }

    public ResourceSharingPolicy allowOrigins(String... origins) {
        allowedOrigins.addAll(Arrays.asList(origins));
        return this;
    }

    public boolean allowsAllHeaders() {
        return allowedHeaders.contains(ALLOW_ALL);
    }

    public boolean allowsAllOrigins() {
        return allowedOrigins.contains(ALLOW_ALL);
    }

    public ResourceSharingPolicy exposeHeaders(String... headers) {
        exposedHeaders.addAll(Arrays.asList(headers));
        return this;
    }

    public boolean headersAllowed(List<String> headers) {
        return allowedHeaders.containsAll(headers);
    }

    public boolean isAllowedMethod(String method) {
        return isWhitelisted(allowedMethods, method);
    }

    private boolean isWhitelisted(Set<String> allowedValues, String value) {
        return value != null && (allowedValues.contains(value) || allowedValues.contains(ALLOW_ALL));
    }

    public boolean isAllowedOrigin(String origin) {
        return isWhitelisted(allowedOrigins, origin);
    }

    public ResourceSharingPolicy withMaxAge(long duration, TimeUnit unit) {
        this.maxAge = unit.toMinutes(duration);
        return this;
    }
}
