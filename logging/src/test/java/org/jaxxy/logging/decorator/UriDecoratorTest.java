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

import javax.ws.rs.core.MultivaluedHashMap;

import org.jaxxy.test.hello.DefaultHelloResource;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UriDecoratorTest extends AbstractDecoratorTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void decorateShouldAddTemplate() throws Exception {
        when(loggingContext.getResourceInfo().getResourceMethod()).thenReturn(DefaultHelloResource.class.getMethod("sayHello", String.class));
        final MultivaluedHashMap<String, String> pathParams = new MultivaluedHashMap<>();
        pathParams.putSingle("name", "Jaxxy");
        when(loggingContext.getRequestContext().getUriInfo().getPathParameters()).thenReturn(pathParams);
        final MultivaluedHashMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.putSingle("foo", "bar");
        when(loggingContext.getRequestContext().getUriInfo().getQueryParameters()).thenReturn(queryParameters);

        final UriDecorator decorator = new UriDecorator();
        decorator.decorate(loggingContext);
        verify(loggingContext).put("uri.template", "/hello/{name}");
        verify(loggingContext).put("uri.pathParams[name]", "Jaxxy");
        verify(loggingContext).put("uri.queryParams[foo]", "bar");
    }
}