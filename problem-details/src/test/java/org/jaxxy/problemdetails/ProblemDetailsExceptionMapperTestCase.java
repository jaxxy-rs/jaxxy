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

package org.jaxxy.problemdetails;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jaxxy.gson.GsonMessageBodyProvider;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.HelloResource;

public abstract class ProblemDetailsExceptionMapperTestCase<E extends Exception> extends JaxrsTestCase<HelloResource> {
    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ProblemDetails.class, new ProblemDetailsGsonSerializer())
                .create();
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new GsonMessageBodyProvider(gson))
                .withClientProvider(new GsonMessageBodyProvider(gson))
                .withContainerProvider(createExceptionMapper());
    }

    protected abstract ExceptionMapper<E> createExceptionMapper();
}
