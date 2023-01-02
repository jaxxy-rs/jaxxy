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

package org.jaxxy.logging.mdc;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Provider
@Priority(1)
public class MdcCleanupFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ORIGINAL_MDC_PROP = String.format("%s.ORIGINAL_MDC", MdcCleanupFilter.class.getCanonicalName());

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        final Map<String, String> original = ofNullable(MDC.getCopyOfContextMap()).orElseGet(Collections::emptyMap);
        request.setProperty(ORIGINAL_MDC_PROP, original);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        final Map<String, String> original = ofNullable((Map<String, String>) request.getProperty(ORIGINAL_MDC_PROP)).orElseGet(Collections::emptyMap);
        MDC.setContextMap(original);
    }
}
