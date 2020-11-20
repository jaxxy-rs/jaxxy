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

package org.jaxxy.cache;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CacheControlFilter implements ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    static final String ETAG_PROPERTY = CacheControlFilter.class.getCanonicalName() + ".eTag";
    static final String LAST_MODIFIED_PROPERTY = CacheControlFilter.class.getCanonicalName() + ".lastModified";

    private final Supplier<CacheControl> cacheControlSupplier;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    static String httpDateFormat(Instant date) {
        return date.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    static String quoted(Object value) {
        return String.format("\"%s\"", value);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        if(Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            setHeader(response, HttpHeaders.ETAG, (EntityTag)request.getProperty(ETAG_PROPERTY), etag -> quoted(etag.getValue()));
            setHeader(response, HttpHeaders.LAST_MODIFIED, (Instant)request.getProperty(LAST_MODIFIED_PROPERTY), CacheControlFilter::httpDateFormat);
        }
        setHeader(response, HttpHeaders.CACHE_CONTROL, cacheControlSupplier.get(), CacheControl::toString);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> void setHeader(ContainerResponseContext response, String headerName, T value, Function<T,String> formatter) {
        ofNullable(value).ifPresent(v -> response.getHeaders().putSingle(headerName, formatter.apply(v)));
    }
}
