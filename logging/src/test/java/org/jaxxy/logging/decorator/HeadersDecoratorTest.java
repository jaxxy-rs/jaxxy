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

import java.util.Arrays;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HeadersDecoratorTest extends AbstractDecoratorTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldAddSingleValuedHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.putSingle("foo", "bar");
        when(loggingContext.getRequestContext().getHeaders()).thenReturn(headers);

        final HeadersDecorator decorator = new HeadersDecorator();
        decorator.decorate(loggingContext);
        verify(loggingContext).put("headers[foo]", "bar");
    }

    @Test
    public void shouldAddMultiValuedHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("foo", Arrays.asList("bar","baz"));
        when(loggingContext.getRequestContext().getHeaders()).thenReturn(headers);

        final HeadersDecorator decorator = new HeadersDecorator();
        decorator.decorate(loggingContext);
        verify(loggingContext).put("headers[foo]", "bar, baz");
    }

    @Test
    public void shouldDoNothingWhenEmptyHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        when(loggingContext.getRequestContext().getHeaders()).thenReturn(headers);

        final HeadersDecorator decorator = new HeadersDecorator();
        decorator.decorate(loggingContext);
        verify(loggingContext, times(0)).put(anyString(), anyString());
    }
}