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

package org.jaxxy.cache;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
public class DefaultPreconditions implements Preconditions {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final HttpServletRequest servletRequest;
    private final Request request;

//----------------------------------------------------------------------------------------------------------------------
// Preconditions Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void evaluate() {
        throwOnPrecondition(request.evaluatePreconditions());
    }

    @Override
    public void evaluate(EntityTag entityTag) {
        setEtagAttribute(entityTag);
        throwOnPrecondition(request.evaluatePreconditions(entityTag));
    }

    @Override
    public void evaluate(Instant lastModified) {
        setLastModifiedAttribute(lastModified);
        throwOnPrecondition(request.evaluatePreconditions(Date.from(lastModified)));
    }

    private void setLastModifiedAttribute(Instant lastModified) {
        servletRequest.setAttribute(CacheControlFilter.LAST_MODIFIED_PROPERTY, lastModified);
    }

    @Override
    public void evaluate(Instant lastModified, EntityTag entityTag) {
        setEtagAttribute(entityTag);
        setLastModifiedAttribute(lastModified);
        throwOnPrecondition(request.evaluatePreconditions(Date.from(lastModified), entityTag));
    }

    private void setEtagAttribute(EntityTag entityTag) {
        servletRequest.setAttribute(CacheControlFilter.ETAG_PROPERTY, entityTag);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private void throwOnPrecondition(Response.ResponseBuilder builder) {
        if (builder != null) {
            throw new WebApplicationException(builder.build());
        }
    }
}
