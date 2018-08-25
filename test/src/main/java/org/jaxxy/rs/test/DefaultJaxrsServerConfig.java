package org.jaxxy.rs.test;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@Getter
public class DefaultJaxrsServerConfig implements JaxrsServerConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<Object> providers  = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerConfig Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServerConfig withProvider(Object provider) {
        providers.add(provider);
        return this;
    }
}
