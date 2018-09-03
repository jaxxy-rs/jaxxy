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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import static java.util.Optional.ofNullable;

@Provider
@Priority(Priorities.USER-200)
public class LoggingContextFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String LOGGING_CONTEXT_PROP = LoggingContextFilter.class.getCanonicalName() + ".loggingContext";

    private final List<LoggingContextDecorator> decorators;

    @Context
    private ResourceInfo resourceInfo;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public LoggingContextFilter(List<LoggingContextDecorator> decorators) {
        this.decorators = decorators;
    }

    public LoggingContextFilter(LoggingContextDecorator... decorators) {
        this(Arrays.asList(decorators));
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final DefaultLoggingContext loggingContext = DefaultLoggingContext.builder()
                .requestContext(requestContext)
                .resourceInfo(resourceInfo)
                .build();
        decorators.forEach(decorator->decorator.decorate(loggingContext));
        requestContext.setProperty(LOGGING_CONTEXT_PROP, loggingContext);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        ofNullable((DefaultLoggingContext)requestContext.getProperty(LOGGING_CONTEXT_PROP)).ifPresent(DefaultLoggingContext::cleanup);
    }
}
