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

package org.jaxxy.cache;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.jaxxy.cache.CacheControlFilter.httpDateFormat;
import static org.jaxxy.cache.CacheControlFilter.quoted;


class CacheControlFilterTest extends JaxrsTestCase<CacheableResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withContainerProvider(new PreconditionsResolver())
                .withContainerProvider(new CacheControlFilter(() -> {
                    CacheControl cc = new CacheControl();
                    cc.setMaxAge(6000);
                    cc.setNoTransform(true);
                    return cc;
                }));
    }

    @Override
    protected CacheableResource createServiceObject() {
        return new DefaultCacheableResource();
    }

    @Test
    void should304WhenEtagMatches() {
        final Response response = webTarget().path("eTag")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_NONE_MATCH, quoted(DefaultCacheableResource.RESPONSE.hashCode()))
                .get();
        assertThat(response.getStatus()).isEqualTo(304);
        assertThat(response.getHeaderString(HttpHeaders.ETAG)).isEqualTo(quoted(DefaultCacheableResource.E_TAG.getValue()));
    }

    @Test
    void should304WhenNotModifiedSince() {
        final String lastMod = CacheControlFilter.httpDateFormat(DefaultCacheableResource.LAST_MODIFIED_INSTANT);
        final Response response = webTarget().path("lastModified")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_MODIFIED_SINCE, lastMod)
                .get();
        assertThat(response.getStatus()).isEqualTo(304);
        assertThat(response.getHeaderString(HttpHeaders.LAST_MODIFIED)).isNull();
    }

    @Test
    void should304WhenNotModifiedSinceOrEtagMatches() {
        final String lastModHeader = CacheControlFilter.httpDateFormat(DefaultCacheableResource.LAST_MODIFIED_INSTANT);
        final String etagHeader = quoted(DefaultCacheableResource.RESPONSE.hashCode());
        final Response response = webTarget().path("eTagAndLastModified")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_MODIFIED_SINCE, lastModHeader)
                .header(HttpHeaders.IF_NONE_MATCH, etagHeader)
                .get();
        assertThat(response.getStatus()).isEqualTo(304);
        assertThat(response.getHeaderString(HttpHeaders.LAST_MODIFIED)).isNull();
        assertThat(response.getHeaderString(HttpHeaders.ETAG)).isEqualTo(etagHeader);
    }

    @Test
    void should412WhenIfModifiedSinceOnPut() {
        final Response response = webTarget().path("messages")
                .request()
                .header(HttpHeaders.IF_MATCH, quoted("*"))
                .put(Entity.entity("foo", MediaType.TEXT_PLAIN_TYPE));
        assertThat(response.getStatus()).isEqualTo(412);
    }

    @Test
    void shouldPutSuccessfully() {
        try {
            clientProxy().putNewMessage("foo");
        }catch(Exception e) {
            fail("Should successfully put the value");
        }
    }

    @Test
    void shouldReturnWhenEtagNoneMatch() {
        final Response response = webTarget().path("eTag")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_NONE_MATCH, quoted("bogus"))
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaderString(HttpHeaders.ETAG)).isEqualTo(quoted(DefaultCacheableResource.E_TAG.getValue()));
    }

    @Test
    void shouldReturnWhenEtagNoneMatchAndModifiedSince() {
        final String lastMod = CacheControlFilter.httpDateFormat(DefaultCacheableResource.LAST_MODIFIED_INSTANT.minus(1, ChronoUnit.DAYS));
        final Response response = webTarget().path("eTagAndLastModified")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_NONE_MATCH, quoted("bogus"))
                .header(HttpHeaders.IF_MODIFIED_SINCE, lastMod)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaderString(HttpHeaders.ETAG)).isEqualTo(quoted(DefaultCacheableResource.E_TAG.getValue()));
    }

    @Test
    void shouldReturnWhenModifiedSince() {
        final String lastMod = CacheControlFilter.httpDateFormat(DefaultCacheableResource.LAST_MODIFIED_INSTANT.minus(1, ChronoUnit.DAYS));
        final Response response = webTarget().path("lastModified")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.IF_MODIFIED_SINCE, lastMod)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaderString(HttpHeaders.LAST_MODIFIED)).isEqualTo(httpDateFormat(DefaultCacheableResource.LAST_MODIFIED_INSTANT));
    }

    @Test
    void shouldReturnWithNoPreconditions() {
        assertThat(clientProxy().noPreconditions()).isEqualTo(DefaultCacheableResource.RESPONSE);
    }
}