package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "person_id_sequence",
            sequenceName = "person_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "person_id_sequence"
    )


    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;


    @JsonManagedReference
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


//Az, hogy melyik osztálynál van a kulcs (pl. person_id vagy cart_id), az attól függ, hogy melyik osztályhoz tartozik az adott kapcsolat. A jelenlegi megvalósítás szerint egy Person objektumhoz tartozik egy Cart objektum, tehát a Cart osztálynál van a kulcs, amely hivatkozik a Person-re.
