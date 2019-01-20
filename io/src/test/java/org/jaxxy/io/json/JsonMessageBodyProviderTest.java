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

package org.jaxxy.io.json;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.ext.multipart.InputStreamDataSource;
import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsServerConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonMessageBodyProviderTest extends JaxrsTestCase<JsonLocalDateResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private JsonLocalDateResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(new JsonLocalDateMessageBodyProvider());
    }

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        super.configureServer(config);
        config.withProvider(new JsonLocalDateMessageBodyProvider());
    }

    @Override
    protected JsonLocalDateResource createServiceObject() {
        return resource;
    }

    @Test
    public void shouldIgnoreReader() {
        assertIgnored(date -> new StringReader(toJsonString(date)));
    }

    @Test
    public void shouldIgnoreString() {
        assertIgnored(this::toJsonString);
    }

    @Test
    public void shouldIgnoreByteArray() {
        assertIgnored(date -> toJsonString(date).getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void shouldIgnoreInputStream() {
        assertIgnored(date -> new ByteArrayInputStream(toJsonString(date).getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void shouldIgnoreStreamingOutput() {
        assertIgnored(date -> (StreamingOutput) output -> output.write(toJsonString(date).getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void shouldIgnoreJafDataSource() {
        assertIgnored(date -> new InputStreamDataSource(new ByteArrayInputStream(toJsonString(date).getBytes(StandardCharsets.UTF_8)), MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldIgnoreFile() {
        assertIgnored(date -> {
            try {
                final File file = File.createTempFile("response", ".json");
                FileUtils.writeStringToFile(file, toJsonString(date), StandardCharsets.UTF_8.displayName());
                return file;
            } catch (IOException e) {
                throw new InternalServerErrorException("Couldn't create response file.", e);
            }
        });
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldIgnoreUnsupportedTypes() {
        when(resource.unsupported()).thenReturn(new Date());
        clientProxy().unsupported();
    }

    private String toJsonString(LocalDate date) {
        return String.format("\"%s\"", date);
    }

    private <T> void assertIgnored(Function<LocalDate, T> fn) {
        final LocalDate expected = LocalDate.now();
        final T entity = fn.apply(expected);
        when(resource.blacklisted()).thenReturn(Response.ok(entity, MediaType.APPLICATION_JSON).build());
        final LocalDate actual = clientProxy().blacklisted().readEntity(LocalDate.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeApplicationJson() {
        final LocalDate expected = LocalDate.now();
        when(resource.nowJson()).thenReturn(expected);
        final LocalDate actual = clientProxy().nowJson();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeNullApplicationJson() {
        final LocalDate actual = clientProxy().nowJson();
        assertThat(actual).isNull();
    }

    @Test
    public void shouldSerializeVendorJson() {
        final LocalDate expected = LocalDate.now();
        when(resource.nowVendorJson()).thenReturn(expected);
        final LocalDate actual = clientProxy().nowVendorJson();
        assertThat(actual).isEqualTo(expected);
    }
}