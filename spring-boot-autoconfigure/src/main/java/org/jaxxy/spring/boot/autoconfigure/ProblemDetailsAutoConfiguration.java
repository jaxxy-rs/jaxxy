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

package org.jaxxy.spring.boot.autoconfigure;

import com.google.gson.Gson;
import org.jaxxy.problemdetails.IllegalArgumentExceptionMapper;
import org.jaxxy.problemdetails.ProblemDetails;
import org.jaxxy.problemdetails.ProblemDetailsGsonSerializer;
import org.jaxxy.problemdetails.RootExceptionMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ProblemDetails.class)
public class ProblemDetailsAutoConfiguration {

    @Bean
    @ConditionalOnBean(Gson.class)
    public GsonBuilderCustomizer problemDetailsSerializerGsonCustomizer() {
        return builder -> builder.registerTypeAdapter(ProblemDetails.class, new ProblemDetailsGsonSerializer());
    }

    @Bean
    public RootExceptionMapper rootExceptionMapper() {
        return new RootExceptionMapper();
    }

    @Bean
    public IllegalArgumentExceptionMapper illegalArgumentExceptionMapper() {
        return new IllegalArgumentExceptionMapper();
    }
}
