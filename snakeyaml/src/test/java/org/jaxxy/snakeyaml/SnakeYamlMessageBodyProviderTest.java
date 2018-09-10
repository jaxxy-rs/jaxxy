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

package org.jaxxy.snakeyaml;

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
public class SnakeYamlMessageBodyProviderTest extends JaxrsTestCase<YamlPersonService> {

    @Mock
    private YamlPersonService service;

    @Override
    protected YamlPersonService createServiceObject() {
        return service;
    }

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(new SnakeYamlMessageBodyProvider());
    }

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        super.configureServer(config);
        config.withProvider(new SnakeYamlMessageBodyProvider());
    }

    @Test
    public void shouldSerializeOne() {
        final Person expected = Person.builder().id("1").firstName("Slappy").lastName("White").build();
        when(service.getPerson("1")).thenReturn(expected);
        final Person actual = clientProxy().getPerson("1");
        assertThat(actual).isEqualTo(expected);
    }
}