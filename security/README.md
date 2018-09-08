# Jaxxy Security

The Jaxxy Security module contains useful providers for:

- Adding security credentials to client requests
- Verifying security credentials on container requests
- Enforcing role-based security

## Token (Bearer) Authentication

#### Verifying a Token

Suppose you want to verify a JWT token using the [Java JWT](https://github.com/auth0/java-jwt) library from Auth0. First, you'll need a JWT-based SecurityContext implementation:


```java
public  class JwtSecurityContext implements SecurityContext {

    private final DecodedJWT jwt;

    public JwtSecurityContext(DecodedJWT jwt) {
        this.jwt = jwt;
    }

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
```

Then, you need to implement a JWT-based TokenAuthenticator, which creates the JwtSecurityContext objects for a given
JWT token header value:

```java
public class class JwtTokenAuthenticator implements TokenAuthenticator {

    private final JWTVerifier verifier;

    public JwtTokenAuthenticator(JWTVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public SecurityContext authenticate(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return new JwtSecurityContext(jwt);
    }
}
```

Now, you just need to instantiate a ContainerTokenAuthFilter and register it:

```java
@ApplicationPath("/secure-app")
public class SecureApplication extends Application {
    
    public Set<Object> getSingletons() {
        final JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("jaxxy")
                        .build(); 
        final JwtTokenAuthenticator authenticator = new JwtTokenAuthenticator(verifier);
        return Collections.singleton(new ContainerTokenAuthFilter(authenticator));
    }
    
    public Set<Class<?>> getClasses() {
        return Collections.singleton(SecureResource.class);
    }
}
```