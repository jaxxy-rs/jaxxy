package org.jaxxy.rs.test;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@Getter
public class DefaultJaxrsClientConfig implements JaxrsClientConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<Object> providers = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// JaxrsClientConfig Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public <P> JaxrsClientConfig withProvider(P provider) {
        providers.add(provider);
        return this;
    }
}
