package org.jaxxy.logging.mdc;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

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
