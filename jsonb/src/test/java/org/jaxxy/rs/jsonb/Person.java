package org.jaxxy.rs.jsonb;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Person {
    private final String id;
    private final String firstName;
    private final String lastName;

    @JsonbCreator
    public Person(@JsonbProperty("id") String id,
                  @JsonbProperty("firstName") String firstName,
                  @JsonbProperty("lastName") String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
