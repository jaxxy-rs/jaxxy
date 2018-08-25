package org.jaxxy.rs.jsonb;

import org.jaxxy.rs.test.JaxrsClientConfig;
import org.jaxxy.rs.test.JaxrsServerConfig;
import org.jaxxy.rs.test.JaxrsTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonbJaxrsProviderTest extends JaxrsTestCase<PersonsResource> {

    @Mock
    private PersonsResource resource;

    @Override
    protected void configureServer(JaxrsServerConfig config) {
        config.withProvider(new JsonbJaxrsProvider());
    }

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        config.withProvider(new JsonbJaxrsProvider());
    }

    @Override
    protected PersonsResource createServiceObject() {
        return resource;
    }

    @Test
    public void testWrite() {
        when(resource.get("1")).thenReturn(Person.builder()
                .id("1")
                .firstName("Slappy")
                .lastName("White")
                .build());
        final Person person = clientProxy().get("1");
        assertThat(person.getFirstName()).isEqualTo("Slappy");
    }
}