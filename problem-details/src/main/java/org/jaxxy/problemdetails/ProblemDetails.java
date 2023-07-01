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

import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.net.URI;
import java.util.Map;

@Value
@Builder
public class ProblemDetails {

// ------------------------------ FIELDS ------------------------------

    public static final String PROBLEM_JSON_TYPE = "application/problem+json";

    URI type;
    String title;
    String detail;
    @Builder.Default
    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    URI instance;
    @Singular
    Map<String, Object> extensions;

}
