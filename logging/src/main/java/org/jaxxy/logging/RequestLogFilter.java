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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Optional.ofNullable;

@Provider
@Priority(Priorities.USER - 100)
@Builder
public class RequestLogFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_ELAPSED_TIME_HEADER = "X-Elapsed-Time";
    private static final String BEGIN_TS_PROP = RequestLogFilter.class.getCanonicalName() + ".beginTs";

    @Builder.Default
    private final Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);

    @Builder.Default
    private String elapsedTimeHeader = DEFAULT_ELAPSED_TIME_HEADER;

    @Builder.Default
    private TimeUnit elapsedTimeUnit = TimeUnit.SECONDS;

    @Getter(value= AccessLevel.PRIVATE, lazy=true)
    private final double divisor = TimeUnit.NANOSECONDS.convert(1, elapsedTimeUnit);

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) {
        requestContext.setProperty(BEGIN_TS_PROP, System.nanoTime());
        if (logger.isInfoEnabled()) {
            logger.info("BEGIN {} {}", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().getPath());
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        ofNullable((Long) requestContext.getProperty(BEGIN_TS_PROP)).ifPresent(beginTs->{
            final long elapsedNanos = System.nanoTime() - beginTs;
            final Response.StatusType statusInfo = responseContext.getStatusInfo();
            final String elapsedText = String.format("%1.3f %s", elapsedNanos / getDivisor(), elapsedTimeUnit.name().toLowerCase());
            responseContext.getHeaders().putSingle(elapsedTimeHeader, elapsedText);
            if (logger.isInfoEnabled()) {
                logger.info("END   {} {} - {} {} ({})", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().getPath(), statusInfo.getStatusCode(), statusInfo.getReasonPhrase(), elapsedText);
            }
        });
    }
}
