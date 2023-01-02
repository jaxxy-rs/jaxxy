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

package org.jaxxy.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.DefaultHelloResource;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Algorithm algorithm = Algorithm.HMAC256("secret");

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        final String issuer = "jaxxy";
        return super.createJaxrsFixtureFactory()
                .withClientProvider(new ClientTokenAuthFilter(() -> JWT.create()
                        .withIssuer(issuer)
                        .sign(algorithm)))
                .withContainerProvider(new ContainerTokenAuthFilter(new JwtTokenAuthenticator(JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build())));
    }

    @Override
    protected HelloResource createServiceObject() {
        return new DefaultHelloResource();
    }

    @Test
    void testWithSignedJwtToken() {
        assertThat(clientProxy().sayHello("JWT")).isEqualTo("Hello, JWT!");
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    private static class JwtSecurityContext implements SecurityContext {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final DecodedJWT jwt;

//----------------------------------------------------------------------------------------------------------------------
// SecurityContext Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public String getAuthenticationScheme() {
            return "JWT";
        }

        @Override
        public Principal getUserPrincipal() {
            return jwt::getSubject;
        }

        @Override
        public boolean isSecure() {
            return true;
        }

        @Override
        public boolean isUserInRole(String role) {
            final Claim rolesClaim = jwt.getClaim("roles");
            return !rolesClaim.isNull() && rolesClaim.asList(String.class).contains(role);
        }
    }

    @RequiredArgsConstructor
    public static class JwtTokenAuthenticator implements TokenAuthenticator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final JWTVerifier verifier;

//----------------------------------------------------------------------------------------------------------------------
// TokenAuthenticator Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public SecurityContext authenticate(String token) {
            DecodedJWT jwt = verifier.verify(token);
            return new JwtSecurityContext(jwt);
        }
    }
}
