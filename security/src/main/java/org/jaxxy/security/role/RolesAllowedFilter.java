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

package org.jaxxy.security.role;

import java.util.Set;

import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Provider
@Priority(Priorities.AUTHORIZATION)
@Slf4j
@RequiredArgsConstructor
public class RolesAllowedFilter implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Set<String> rolesAllowed;

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        final String allowedRole = rolesAllowed.stream()
                .filter(role -> request.getSecurityContext().isUserInRole(role))
                .findFirst()
                .orElseThrow(ForbiddenException::new);
        log.debug("Proceeding with request using allowed role '{}'.", allowedRole);
    }
}
