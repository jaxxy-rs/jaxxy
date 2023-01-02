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

package org.jaxxy.test;

import jakarta.ws.rs.client.WebTarget;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.jaxxy.test.fixture.JaxrsServiceFixture;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.fixture.JaxrsServiceFixtures;
import org.jaxxy.util.reflect.Types;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
public abstract class JaxrsTestCase<I> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private JaxrsServiceFixture<I> fixture;

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract I createServiceObject();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public I clientProxy() {
        return fixture.createClientProxy();
    }

    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return JaxrsServiceFixtures.createFactory()
                .withContainerFeature(new LoggingFeature())
                .withClientFeature(new LoggingFeature());
    }

    private Class<I> serviceInterface() {
        return Types.typeParamFromClass(getClass(), JaxrsTestCase.class, 0);
    }

    @BeforeEach
    public void startServer() {
        this.fixture = createJaxrsFixtureFactory()
                .build(serviceInterface(), createServiceObject());
    }

    @AfterEach
    public void stopServer() throws Exception {
        fixture.close();
    }

    public WebTarget webTarget() {
        return fixture.createWebTarget();
    }
}
