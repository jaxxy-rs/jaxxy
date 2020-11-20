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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class PreconditionsResolver implements ContextResolver<Preconditions> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private Request request;

//----------------------------------------------------------------------------------------------------------------------
// ContextResolver Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Preconditions getContext(Class<?> type) {
        return new DefaultPreconditions(servletRequest, request);
    }
}
