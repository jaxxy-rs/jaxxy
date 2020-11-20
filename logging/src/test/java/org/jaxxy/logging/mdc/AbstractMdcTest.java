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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jaxxy.gson.GsonMessageBodyProvider;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;

public abstract class AbstractMdcTest extends JaxrsTestCase<EchoMdcResource> {

//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new MdcCleanupFilter())
                .withContainerProvider(new GsonMessageBodyProvider())
                .withClientProvider(new GsonMessageBodyProvider());
    }

    protected RichMdc createRichMdc() {
        return new DefaultRichMdc(GSON::toJson);
    }

    @Override
    protected EchoMdcResource createServiceObject() {
        return new DefaultEchoMdcResource();
    }
}
