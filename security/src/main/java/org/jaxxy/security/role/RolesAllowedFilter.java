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

package org.jaxxy.security.role;

import java.util.stream.Stream;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import static java.util.Optional.ofNullable;

@Provider
@Priority(Priorities.AUTHORIZATION)
@Slf4j
public class RolesAllowedFilter implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Context
    private ResourceInfo resourceInfo;

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void filter(ContainerRequestContext request) {
        final String allowedRole = ofNullable(resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class))
                .flatMap(annotation -> Stream.of(annotation.value())
                        .filter(role -> request.getSecurityContext().isUserInRole(role))
                        .findFirst())
                .orElseThrow(ForbiddenException::new);
        log.debug("Proceeding with request using allowed role '{}'.", allowedRole);
    }
}
