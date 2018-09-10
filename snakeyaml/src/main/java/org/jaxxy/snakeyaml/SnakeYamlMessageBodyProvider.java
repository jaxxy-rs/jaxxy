/*
 * Copyright (c) 2018 The Jaxxy Authors.
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

package org.jaxxy.snakeyaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jaxxy.io.CharacterMessageBodyProvider;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

@Produces("application/yaml")
@Consumes("application/yaml")
public class SnakeYamlMessageBodyProvider extends CharacterMessageBodyProvider<Object> {

    @Override
    protected Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, Reader reader) throws IOException {
        final Yaml yaml = new Yaml();
        return yaml.loadAs(reader, type);
    }

    @Override
    protected void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, Writer writer) throws IOException {
        final Yaml yaml = new Yaml();
        writer.write(yaml.dumpAs(o, Tag.MAP, null));
    }
}
