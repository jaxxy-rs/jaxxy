/*
 * Copyright (c) 2020 The Jaxxy Authors.
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

import java.util.List;
import java.util.function.Predicate;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Builder
@RequiredArgsConstructor
public class SupportedTypesPredicate implements Predicate<Class<?>> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Singular
    private final List<Class<?>> whitelists;

    @Singular
    private final List<Class<?>> blacklists;

//----------------------------------------------------------------------------------------------------------------------
// Predicate Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean test(Class<?> type) {
        return isWhitelisted(type) && isNotBlacklisted(type);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private boolean isNotBlacklisted(Class<?> type) {
        return blacklists.isEmpty() || blacklists.stream().noneMatch(isAssignable(type));
    }

    private Predicate<Class<?>> isAssignable(Class<?> type) {
        return t -> t.isAssignableFrom(type);
    }

    private boolean isWhitelisted(Class<?> type) {
        return whitelists.isEmpty() || whitelists.stream().anyMatch(isAssignable(type));
    }
}
