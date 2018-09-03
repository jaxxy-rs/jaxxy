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

import org.jaxxy.test.JaxrsServerConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.hello.DefaultHelloResource;
import org.jaxxy.test.hello.HelloResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LoggingContextFilterTest extends JaxrsTestCase<HelloResource> {

    @Mock
    private LoggingContextDecorator decorator1;

    @Mock
    private LoggingContextDecorator decorator2;


    @Captor
    private ArgumentCaptor<LoggingContext> contextCaptor;

    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        config.withProvider(new LoggingContextFilter(decorator1, decorator2));
    }

    @Test
    public void testDecorators() {
        clientProxy().sayHello("Jaxxy");
        verify(decorator1).decorate(contextCaptor.capture());
        verify(decorator2).decorate(contextCaptor.capture());
        verifyNoMoreInteractions(decorator1, decorator2);
    }
}