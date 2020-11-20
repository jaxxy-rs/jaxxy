/*
 * Copyright (c) 2020 The Jaxxy Authors.
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

package org.jaxxy.protobuf;

import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProtobufMessageBodyProviderTest extends JaxrsTestCase<PersonResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private PersonResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new ProtobufMessageBodyProvider())
                .withClientProvider(new ProtobufMessageBodyProvider());
    }

    @Override
    protected PersonResource createServiceObject() {
        return resource;
    }

    @Test
    public void shouldWrite() {
        final PersonProtos.Person expected = PersonProtos.Person.newBuilder()
                .setFirstName("Slappy")
                .setLastName("White")
                .setId("1")
                .build();
        when(resource.getPerson("1")).thenReturn(expected);

        final PersonProtos.Person actual = clientProxy().getPerson("1");
        assertThat(actual).isEqualTo(expected);
    }
}