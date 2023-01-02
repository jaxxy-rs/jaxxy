/*
 * Copyright (c) 2018-2023 The Jaxxy Authors.
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

package org.jaxxy.logging.mdc;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MdcUriInfoFilterTest extends AbstractMdcTest {

    private static final GenericType<Map<String,String>> MAP_OF_STRINGS = new GenericType<>() {

    };

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new MdcUriInfoFilter(createRichMdc()));
    }

    @Test
    void checkPath() {
        final Map<String, String> mdc = clientProxy().echoMdc();
        final UriProps props = GSON.fromJson(mdc.get("uri"), UriProps.class);
        assertThat(props.getPath()).isEqualTo("mdc");
    }

    @Test
    void checkTemplate() {
        final Map<String, String> mdc = clientProxy().echoMdc();
        final UriProps props = GSON.fromJson(mdc.get("uri"), UriProps.class);
        assertThat(props.getTemplate()).isEqualTo("/mdc");
    }

    @Test
    void checkQueryParams() {
        final Map<String, String> mdc = webTarget()
                .path("mdc")
                .queryParam("foo", "bar")
                .request(MediaType.APPLICATION_JSON)
                .get(MAP_OF_STRINGS);
        final UriProps props = GSON.fromJson(mdc.get("uri"), UriProps.class);
        assertThat(props.getQueryParams()).containsEntry("foo", List.of("bar"));
    }

    @Test
    void checkPathParams() {
        final Map<String, String> mdc = clientProxy().echoMdcWithPathVar("bar");
        final UriProps props = GSON.fromJson(mdc.get("uri"), UriProps.class);
        assertThat(props.getPathParams()).containsEntry("foo", List.of("bar"));
    }
}