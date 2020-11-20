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

package org.jaxxy.security.token;

import java.util.function.Supplier;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@Builder
@AllArgsConstructor
public class ClientTokenAuthFilter implements ClientRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Supplier<String> tokenSupplier;

//----------------------------------------------------------------------------------------------------------------------
// ClientRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext request) {
        request.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenSupplier.get()));
    }
}
