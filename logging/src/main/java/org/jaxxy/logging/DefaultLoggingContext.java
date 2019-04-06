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

package org.jaxxy.logging;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@Builder
@Getter
public class DefaultLoggingContext implements LoggingContext {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<MDC.MDCCloseable> closeables = new LinkedList<>();

    private final ResourceInfo resourceInfo;
    private final ContainerRequestContext requestContext;
    private final LoggingContextSerializer serializer;

//----------------------------------------------------------------------------------------------------------------------
// LoggingContext Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void put(String key, Object value) {
        if (value != null) {
            closeables.add(MDC.putCloseable(key, serializer.apply(value)));
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void cleanup() {
        closeables.forEach(MDC.MDCCloseable::close);
    }
}
