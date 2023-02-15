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

package org.jaxxy.io;

import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterMessageBodyProviderTest extends JaxrsTestCase<LocalDateResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private LocalDateResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new LocalDateMessageBodyProvider())
                .withClientProvider(new LocalDateMessageBodyProvider());
    }

    @Override
    protected LocalDateResource createServiceObject() {
        return resource;
    }

    @Test
    void shouldSerializeSuccessfully() {
        final LocalDate expected = LocalDate.now();
        when(resource.now()).thenReturn(expected);
        final LocalDate actual = clientProxy().now();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldSerializeNullsSuccessfully() {
        final LocalDate actual = clientProxy().now();
        assertThat(actual).isNull();
    }
}