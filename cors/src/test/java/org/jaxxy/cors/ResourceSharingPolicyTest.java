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

package org.jaxxy.cors;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.HttpMethod;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ResourceSharingPolicyTest {

    @Test
    public void testDefaultPolicy() {
        final ResourceSharingPolicy policy = ResourceSharingPolicy.defaultPolicy();
        assertThat(policy.allowsAllHeaders()).isTrue();
        assertThat(policy.getAllowedMethods()).hasSize(5)
                .contains("OPTIONS")
                .contains("GET")
                .contains("POST")
                .contains("PUT")
                .contains("DELETE");
        assertThat(policy.allowsAllOrigins()).isTrue();
        assertThat(policy.getMaxAge()).isEqualTo(TimeUnit.DAYS.toMinutes(1));
        assertThat(policy.isAllowCredentials()).isFalse();
        assertThat(policy.getExposedHeaders()).isEmpty();
    }

    @Test
    public void shouldExposeHeaders() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.getExposedHeaders()).isEmpty();
        policy.exposeHeaders("Foo", "Bar");
        assertThat(policy.getExposedHeaders()).hasSize(2).contains("Foo", "Bar");
    }

    @Test
    public void shouldAllowCredentials() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.isAllowCredentials()).isFalse();
        policy.allowCredentials();
        assertThat(policy.isAllowCredentials()).isTrue();
    }

    @Test
    public void shouldAllowMethod() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.isAllowedMethod("GET")).isFalse();
        policy.allowMethods("GET");
        assertThat(policy.isAllowedMethod("GET")).isTrue();
    }

    @Test
    public void nullValuesShouldNeverBeWhitelisted() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy()
                .allowAllOrigins();
        assertThat(policy.isAllowedOrigin(null)).isFalse();
    }

    @Test
    public void headersAllowedShouldBeTrueForSublists() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();

        policy.allowHeaders("Foo", "Bar", "Baz");
        assertThat(policy.headersAllowed(Collections.singletonList("Foo"))).isTrue();
        assertThat(policy.headersAllowed(Collections.singletonList("Bar"))).isTrue();
        assertThat(policy.headersAllowed(Collections.singletonList("Baz"))).isTrue();
        assertThat(policy.headersAllowed(Arrays.asList("Foo", "Bar", "Baz"))).isTrue();
        assertThat(policy.headersAllowed(Collections.emptyList())).isTrue();
        assertThat(policy.headersAllowed(Arrays.asList("Foo", "Bar", "Bat"))).isFalse();
    }

    @Test
    public void allowedHeadersIsCaseInsensitive() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();

        policy.allowHeaders("Foo", "Bar", "Baz");
        assertThat(policy.headersAllowed(Arrays.asList("foo", "bar", "baz"))).isTrue();
    }

    @Test
    public void headersAllowedShouldBeTrueForEmptyListWhenEmpty() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.headersAllowed(Collections.emptyList())).isTrue();
    }

    @Test
    public void shouldAllowAllHeaders() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.allowsAllHeaders()).isFalse();
        policy.allowCredentials();
        assertThat(policy.isAllowCredentials()).isTrue();
    }

    @Test
    public void shouldAllowAllOrigins() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.allowsAllOrigins()).isFalse();
        policy.allowAllOrigins();
        assertThat(policy.allowsAllOrigins()).isTrue();
        assertThat(policy.isAllowedOrigin("foo")).isTrue();
        assertThat(policy.isAllowedOrigin("bar")).isTrue();
    }

    @Test
    public void shouldAllowOrigins() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.isAllowedOrigin("foo")).isFalse();
        policy.allowOrigins("foo");
        assertThat(policy.isAllowedOrigin("foo")).isTrue();
    }

    @Test
    public void shoulNotAllowNullOrigin() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy();
        assertThat(policy.isAllowedOrigin(null)).isFalse();
        policy.allowOrigins("foo");
        assertThat(policy.isAllowedOrigin(null)).isFalse();
        policy.allowAllOrigins();
        assertThat(policy.isAllowedOrigin(null)).isFalse();
    }

    @Test
    public void allowedOriginShouldBeCaseSensitive() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy()
                .allowOrigins("foo");
        assertThat(policy.isAllowedOrigin("foo")).isTrue();
        assertThat(policy.isAllowedOrigin("Foo")).isFalse();
        assertThat(policy.isAllowedOrigin("FOO")).isFalse();
    }

    @Test
    public void allowedMethodShouldBeCaseSensitive() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy()
                .allowMethods(HttpMethod.GET);
        assertThat(policy.isAllowedMethod("GET")).isTrue();
        assertThat(policy.isAllowedMethod("get")).isFalse();
        assertThat(policy.isAllowedMethod("Get")).isFalse();
    }

    @Test
    public void specificValuesShouldBeWhitelisted() {
        final ResourceSharingPolicy policy = new ResourceSharingPolicy()
                .allowOrigins("foo", "bar");

        assertThat(policy.isAllowedOrigin("foo")).isTrue();
        assertThat(policy.isAllowedOrigin("bar")).isTrue();
        assertThat(policy.isAllowedOrigin("baz")).isFalse();
    }

}