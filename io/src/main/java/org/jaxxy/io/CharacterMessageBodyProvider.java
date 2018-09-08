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

package org.jaxxy.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * A character stream-based message body provider.  This provider takes care of creating
 * {@link Reader}s and {@link Writer}s with the proper charset.
 *
 * @param <T> the type supported by this provider
 */
@Provider
public abstract class CharacterMessageBodyProvider<T> extends MessageBodyProvider<T> {
//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Reads the HTTP entity from a {@link Reader}.  This is the character stream equivalent to
     * {@link javax.ws.rs.ext.MessageBodyReader#readFrom(Class, Type, Annotation[], MediaType, MultivaluedMap, InputStream)}.
     *
     * @param type        the type that is to be read.
     * @param genericType the type of instance to be produced.
     * @param annotations an array of the annotations on the declaration of the
     *                    artifact that will be initialized with the produced instance.
     * @param mediaType   the media type of the HTTP entity.
     * @param httpHeaders the read-only HTTP headers associated with HTTP entity.
     * @param reader      the {@link Reader} for the HTTP entity.  The implementation should <em>NOT</em> close this reader!
     * @return the entity
     * @see javax.ws.rs.ext.MessageBodyReader#readFrom(Class, Type, Annotation[], MediaType, MultivaluedMap, InputStream)
     */
    protected abstract T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, Reader reader) throws IOException;

    /**
     * Writes the HTTP entity to a {@link Writer}.  This is the character stream equivalent to
     * {@link javax.ws.rs.ext.MessageBodyWriter#writeTo(Object, Class, Type, Annotation[], MediaType, MultivaluedMap, OutputStream)}.
     *
     * @param t           the instance to write.
     * @param type        the class of instance that is to be written.
     * @param genericType the type of instance to be written.
     * @param annotations an array of the annotations attached to the message entity instance.
     * @param mediaType   the media type of the HTTP entity.
     * @param httpHeaders a mutable map of the HTTP message headers.
     * @param writer      the {@link Writer} for the HTTP entity. The implementation should <em>NOT</em> close this writer!
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(Object, Class, Type, Annotation[], MediaType, MultivaluedMap, OutputStream)
     */
    protected abstract void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, Writer writer) throws IOException;

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyReader Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public final T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        final Reader reader = new BufferedReader(new InputStreamReader(entityStream, extractCharset(mediaType)));
        return readFrom(type, genericType, annotations, mediaType, httpHeaders, reader);
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyWriter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public final void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        final Charset charset = extractCharset(mediaType);
        final Writer writer = new BufferedWriter(new OutputStreamWriter(entityStream, charset));
        writeTo(t, type, genericType, annotations, mediaType, httpHeaders, writer);
        writer.flush();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the default charset to be used by this provider if no other charset is specified.  This default
     * implementation returns {@link StandardCharsets#UTF_8}.
     *
     * @return the default charset (UTF-8)
     */
    protected Charset defaultCharset() {
        return StandardCharsets.UTF_8;
    }

    private Charset extractCharset(MediaType mediaType) {
        return Charset.forName(mediaType.getParameters().getOrDefault(MediaType.CHARSET_PARAMETER, defaultCharset().name()));
    }
}
