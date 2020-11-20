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

package org.jaxxy.logging.mdc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.Priority;
import javax.ws.rs.Path;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@RequiredArgsConstructor
@Slf4j
public class MdcUriInfoFilter implements ContainerRequestFilter {

//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final RichMdc mdc;

    @Context
    private UriInfo uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    private final ConcurrentMap<Method, String> templateCache = new ConcurrentHashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        final String template = extractTemplate();
        mdc.put("uri", UriProps.builder()
                .path(uriInfo.getPath())
                .template(template)
                .pathParams(uriInfo.getPathParameters())
                .queryParams(uriInfo.getQueryParameters())
                .build());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private String extractTemplate() {
        return templateCache.computeIfAbsent(resourceInfo.getResourceMethod(), resourceMethod -> {
            log.info("Calculating URI template for method {}.", resourceMethod.toGenericString());
            final List<Method> overrides = new ArrayList<>(MethodUtils.getOverrideHierarchy(resourceMethod, ClassUtils.Interfaces.INCLUDE));
            final Method annotated = overrides.get(overrides.size() - 1);
            return Optional.ofNullable(annotated.getAnnotation(Path.class))
                    .map(annot -> UriBuilder.fromMethod(annotated.getDeclaringClass(), annotated.getName()).toTemplate())
                    .orElseGet(() -> UriBuilder.fromResource(annotated.getDeclaringClass()).toTemplate());
        });
    }
}
