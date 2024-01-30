package com.ThreeTree.dto;

import com.ThreeTree.model.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPersonResponse {
    private Long id;
    private String firstName;
    private String lastName;


    public NewPersonResponse(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }
}
