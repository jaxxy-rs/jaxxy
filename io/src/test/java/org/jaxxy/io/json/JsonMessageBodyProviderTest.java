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

package org.jaxxy.io.json;

import java.time.LocalDate;

import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsServerConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonMessageBodyProviderTest extends JaxrsTestCase<JsonLocalDateResource> {

    @Mock
    private JsonLocalDateResource resource;

    @Override
    protected JsonLocalDateResource createServiceObject() {
        return resource;
    }

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(new JsonLocalDateMessageBodyProvider());
    }

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        super.configureServer(config);
        config.withProvider(new JsonLocalDateMessageBodyProvider());
    }

    @Test
    public void shouldSerializeApplicationJson() {
        final LocalDate expected = LocalDate.now();
        when(resource.nowJson()).thenReturn(expected);
        final LocalDate actual = clientProxy().nowJson();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeNullApplicationJson() {
        final LocalDate actual = clientProxy().nowJson();
        assertThat(actual).isNull();
    }

    @Test
    public void shouldSerializeVendorJson() {
        final LocalDate expected = LocalDate.now();
        when(resource.nowVendorJson()).thenReturn(expected);
        final LocalDate actual = clientProxy().nowVendorJson();
        assertThat(actual).isEqualTo(expected);
    }
}