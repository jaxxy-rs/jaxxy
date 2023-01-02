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

package org.jaxxy.util.reflect;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SupportedTypesPredicateTest {

    @Test
    public void emptyShouldAccept() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder().build();
        assertThat(predicate.test(String.class)).isTrue();
        assertThat(predicate.test(Object.class)).isTrue();
    }

    @Test
    public void singleWhitelistExactMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void singleWhitelistSubtypeMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Serializable.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void singleBlacklistExactMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .blacklist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void singleBlacklistSubtypeMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .blacklist(Serializable.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void whitelistedAndBlacklisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Object.class)
                .blacklist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void whitelistedAndNotBlacklisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Object.class)
                .blacklist(Date.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void notWhitelisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(String.class)
                .build();

        assertThat(predicate.test(Date.class)).isFalse();
    }

}