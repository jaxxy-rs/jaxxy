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

package org.jaxxy.logging.mdc;

import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MdcResourceInfoFilterTest extends AbstractMdcTest {

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new MdcResourceInfoFilter(createRichMdc()));
    }

    @Test
    void checkResourceInfo() {
        final Map<String, String> mdc = clientProxy().echoMdc();
        assertThat(mdc)
                .containsEntry("resourceMethod", "\"echoMdc\"")
                .containsEntry("resourceType", String.format("\"%s\"", DefaultEchoMdcResource.class.getCanonicalName()));
    }
}