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

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.DefaultHelloResource;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestLogFilterTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private Logger log;

    @Captor
    private ArgumentCaptor<String> elapsedTimeCaptor;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(RequestLogFilter.builder()
                        .elapsedTimeUnit(TimeUnit.MILLISECONDS)
                        .logger(log)
                        .build());
    }

    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Test
    public void testElapsedTimeHeader() {
        final Response response = webTarget().path("hello").path("Jaxxy").request(MediaType.TEXT_PLAIN_TYPE).get();
        assertThat(response.getHeaderString("X-Elapsed-Time")).isNotNull();
    }

    @Test
    public void testLoggingWhenDisabled() {
        when(log.isInfoEnabled()).thenReturn(false);
        clientProxy().sayHello("Jaxxy");
        verify(log, times(2)).isInfoEnabled();
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testLoggingWhenEnabled() {
        when(log.isInfoEnabled()).thenReturn(true);
        clientProxy().sayHello("Jaxxy");
        verify(log, times(2)).isInfoEnabled();
        verify(log).info("BEGIN {} {}", "GET", "/hello/Jaxxy");
        verify(log).info(eq("END   {} {} - {} {} ({})"), eq("GET"), eq("/hello/Jaxxy"), eq(200), eq("OK"), elapsedTimeCaptor.capture());
        assertThat(elapsedTimeCaptor.getValue()).matches("\\d+\\.\\d\\d\\d milliseconds");
        verifyNoMoreInteractions(log);
    }
}