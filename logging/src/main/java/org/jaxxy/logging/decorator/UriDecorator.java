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

package org.jaxxy.logging.decorator;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jaxxy.logging.LoggingContext;
import org.jaxxy.logging.LoggingContextDecorator;

public class UriDecorator implements LoggingContextDecorator {
//----------------------------------------------------------------------------------------------------------------------
// LoggingContextDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(LoggingContext context) {
        final Path pathAnnotation = MethodUtils.getAnnotation(context.getResourceInfo().getResourceMethod(), Path.class, true, false);
        context.put("uri.template", pathAnnotation.value());
        final UriInfo uriInfo = context.getRequestContext().getUriInfo();
        uriInfo.getPathParameters().forEach((paramName, values) -> context.put("uri.pathParams[" + paramName + "]", StringUtils.join(values, ", ")));
        uriInfo.getQueryParameters().forEach((paramName, values) -> context.put("uri.queryParams[" + paramName + "]", StringUtils.join(values, ", ")));
    }
}
