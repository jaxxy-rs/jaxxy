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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.ext.ContextResolver;

public class DefaultCacheableResource implements CacheableResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    static final Instant LAST_MODIFIED_INSTANT = Instant.from(OffsetDateTime.of(LocalDate.of(2015, 10, 20), LocalTime.of(7, 28, 20), ZoneOffset.UTC));

    static final String RESPONSE = "SUCCESS";
    static final EntityTag E_TAG = EntityTag.valueOf(String.valueOf(RESPONSE.hashCode()));
    @Context
    private ContextResolver<Preconditions> preconditionsResolver;

//----------------------------------------------------------------------------------------------------------------------
// CacheableResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String eTag() {
        preconditions().evaluate(E_TAG);
        return RESPONSE;
    }

    @Override
    public String eTagAndLastModified() {
        preconditions().evaluate(LAST_MODIFIED_INSTANT, E_TAG);
        return RESPONSE;
    }

    @Override
    public String lastModified() {
        preconditions().evaluate(LAST_MODIFIED_INSTANT);
        return RESPONSE;
    }

    @Override
    public String noPreconditions() {
        return RESPONSE;
    }

    @Override
    public void putNewMessage(String value) {
       preconditions().evaluate();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Preconditions preconditions() {
        return preconditionsResolver.getContext(getClass());
    }
}
