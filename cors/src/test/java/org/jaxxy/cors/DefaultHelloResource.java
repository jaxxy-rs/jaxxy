package org.jaxxy.cors;

public class DefaultHelloResource implements HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// HelloResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s!", name);
    }
}
