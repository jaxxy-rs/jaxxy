package org.jaxxy.logging.mdc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

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

    private final ConcurrentMap<Method,String> templateCache = new ConcurrentHashMap<>();

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
                return UriBuilder.fromResource(annotated.getDeclaringClass()).path(annotated).toTemplate();
            });
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @Builder
    @Value
    private static class UriProps {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String path;
        private final String template;
        private final MultivaluedMap<String, String> pathParams;
        private final MultivaluedMap<String, String> queryParams;
    }
}
