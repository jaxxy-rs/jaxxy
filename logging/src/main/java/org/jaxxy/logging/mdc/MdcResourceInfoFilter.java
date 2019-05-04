package org.jaxxy.logging.mdc;

import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@RequiredArgsConstructor
public class MdcResourceInfoFilter implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final RichMdc mdc;

    @Context
    private ResourceInfo resourceInfo;

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) {
        ofNullable(resourceInfo.getResourceMethod())
                .map(Method::getName)
                .ifPresent(name -> mdc.put("resourceMethod", name));

        ofNullable(resourceInfo.getResourceClass())
                .map(Class::getCanonicalName)
                .ifPresent(name->mdc.put("resourceType", name));
    }
}
