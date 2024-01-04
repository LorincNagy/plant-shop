package com.ThreeTree.config;


import com.ThreeTree.dao.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final PersonRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//A UserDetailsService interfész alapvetően a Spring Security része, és a Spring biztosít egy alapértelmezett implementációt, amely használható az alkalmazásban. Az alapértelmezett implementáció általában az alkalmazás saját adatbázisából vagy adatforrásából kinyeri a felhasználói adatokat és azokat felhasználja az autentikációhoz.

//Azáltal, hogy létrehozol egy saját UserDetailsService bean-t, felülírod az alapértelmezett implementációt, és azt mondod meg a Spring Securitynek, hogy használja a saját UserDetailsService-edet az autentikációhoz. A példában szereplő kód az UserDetailsService-et implementálja úgy, hogy egy adott felhasználói e-mail alapján keresse meg a felhasználót a repository segítségével.

//Tehát amikor a kód használja ezt a UserDetailsService-t az autentikáció során, a Spring Security a te implementációd fogja használni annak érdekében, hogy megtalálja a felhasználót az e-mail alapján, majd ezt az információt felhasználja az autentikáció során.

//public interface PersonRepository extends JpaRepository<Person, Long> {
//
//    Optional<Person> findByEmail(String email);
//}
