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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ProblemDetailsGsonSerializer implements JsonSerializer<ProblemDetails> {
    private static final String TYPE_FIELD = "type";
    private static final String TITLE_FIELD = "title";
    private static final String STATUS_FIELD = "status";
    private static final String DETAIL_FIELD = "detail";
    private static final String INSTANCE_FIELD = "instance";

    @Override
    public JsonElement serialize(ProblemDetails details, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add(TYPE_FIELD, context.serialize(details.getType()));
        json.addProperty(TITLE_FIELD, details.getTitle());
        json.addProperty(STATUS_FIELD, details.getStatus().getStatusCode());
        json.addProperty(DETAIL_FIELD, details.getDetail());
        json.add(INSTANCE_FIELD, context.serialize(details.getInstance()));
        details.getExtensions().forEach((name, value) -> json.add(name, context.serialize(value)));
        return json;
    }
}
