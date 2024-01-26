package com.ThreeTree.auth;

import com.ThreeTree.config.JwtService;
import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Role;
import com.ThreeTree.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder; // TODO: has to be final to be picked up by constructor
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CartRepository cartRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        Person existingPerson = personRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingPerson != null) {
            throw new DataIntegrityViolationException("This email address is already exists.");
        }

        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());

        // Kódoljuk a jelszót a passwordEncoder segítségével
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        person.setPassword(encodedPassword);

        person.setEmail(request.getEmail());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setAddress(request.getAddress());
        person.setRole(Role.USER);

        Cart cart = new Cart();
        person.setCart(cart);
        cart.setPerson(person);
        personRepository.save(person); // Itt mented a Person objektumot, amihez a Cart is hozzá lesz rendelve
        cartRepository.save(cart); // Cart objektum mentése az adatbázisba


        var jwtToken = jwtService.generateToken(person);
        sendRegistrationConfirmationEmail(person);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    private void sendRegistrationConfirmationEmail(Person person) {
        // Elküldjük a sikeres regisztrációt visszaigazoló e-mailt
        emailService.sendSimpleEmail(person.getEmail(), "Successful Registration", "Dear " + person.getFirstName() + "! You have successfully registered in our application.");
    }

    //authenticationManager.authenticate hívás nem ad vissza közvetlenül egy választ, hanem egy kivételt dob, ha az autentikáció sikertelen
    //AuthenticationManager ellenőrzi a felhasználói adatok helyességét,
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var person = personRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }
}
//Jelszó Összehasonlítás: Az AuthenticationManager a konfigurált jelszókódolót használja, hogy azonosítsa, hogy a felhasználó által megadott jelszó, miután azt kódolja, megegyezik-e a már adatbázisban tárolt kódolt jelszóval. Fontos megjegyezni, hogy a kódolt jelszót nem lehet visszafejteni az eredeti formájára; ehelyett a bejelentkezéskor megadott jelszót kódolják, és ezt hasonlítják össze a már kódolt jelszóval.
//
//Hitelesítés Eredménye: Ha a kódolt jelszavak megegyeznek, a felhasználó sikeresen hitelesítve van. Ellenkező esetben a hitelesítési folyamat sikertelen lesz.
//
//Összefoglalva, a Spring Security nem "visszafejti" a kódolt jelszavakat. Ehelyett a bejelentkezéskor megadott jelszót kódolja, és ezt hasonlítja össze az adatbázisban már tárolt kódolt jelszóval. Ez egy biztonságos módszer a jelszavak kezelésére, mivel a szöveges formában lévő jelszavakat soha nem tároljuk vagy továbbítjuk.
//A felhasználó megadja az e-mail címét és jelszavát a bejelentkezési űrlapon.
//
//Az authenticationManager.authenticate(...) hívás során az alkalmazás ellenőrzi a megadott e-mail címet és jelszót a regisztrált felhasználók között. Ha a bejelentkezési adatok megfelelnek, akkor a felhasználó sikeresen autentikálódik.
//
//Miután a felhasználó sikeresen bejelentkezett, az alkalmazás megkeresi a felhasználót az e-mail cím alapján az adatbázisban a personRepository.findByEmail(...) hívással.
//
//Miután a felhasználót megtalálta, a kód létrehozza egy JWT-t (jwtToken) a jwtService.generateToken(...) hívással. A JWT tartalmazza az azonosítási információkat, például a felhasználó azonosítóját és jogosultságait.
//
//A kód létrehoz egy AuthenticationResponse objektumot, amely tartalmazza a generált JWT-t, majd visszaadja ezt az objektumot a bejelentkezett felhasználónak.
//
//A JWT (JSON Web Token) létrejöttével az alkalmazás nem tárolja az autentikációs állapotot a szerver oldalon. A kliens kapja meg a JWT-t, és a későbbi kéréseiben használja, hogy igazolja az azonosítását. Az alkalmazás nem "jegyez meg" semmilyen autentikációs állapotot a felhasználó számára a szerveren, mivel a JWT tartalmazza az azonosítási információkat.
//
//Amikor a kliens egy védett erőforráshoz vagy útvonalhoz próbál hozzáférni, a JWT-t az "Authorization" fejlécben küldi a kérésben. Az alkalmazás a JWT-t ellenőrzi az autentikáció során, és ha a JWT érvényes és a felhasználó adatai megegyeznek, akkor az alkalmazás tudja, hogy a felhasználó bejelentkezett és autentikált, és hozzáférést engedélyez a védett erőforráshoz. Az autentikációs állapotot nem kell külön tárolni a szerveren, mivel a JWT tartalmazza az autentikációs adatokat.
//Az AuthenticationManager a UsernamePasswordAuthenticationToken használja az autentikációhoz, amikor bejelentkezést kezel. Ez az autentikáció a felhasználó által megadott felhasználónév és jelszó alapján történik. Az AuthenticationProvider, amelyet a .authenticationProvider(authenticationProvider) részben beállítottál a konfigurációdban, felelős az autentikáció végrehajtásáért ezen az úton.
//
//Amikor védett útra érkezik egy kérés, akkor a JwtAuthenticationFilter lép működésbe, és a JWT token alapján megpróbálja azonosítani a felhasználót. Ha a JWT token érvényes és megfelelő, akkor egy UsernamePasswordAuthenticationToken-t hoz létre az autentikációhoz, amely tartalmazza a felhasználót és a szerepköröket. Ezek az adatok kerülnek átadásra az AuthenticationManager-nek a .authenticationProvider(authenticationProvider) részben beállított AuthenticationProvider-en keresztül a további autentikációs ellenőrzésekhez.
//
//Ezután az AuthenticationManager további autentikációs ellenőrzéseket végez el, ha szükséges, és eldönti, hogy az autentikáció sikeres volt-e vagy sem. Ha sikeres volt, akkor a felhasználó bejelentkezett, és a biztonsági kontextben (SecurityContextHolder) elhelyez egy megfelelő Authentication objektumot, amely tartalmazza a felhasználó adatait.
//
//Tehát a JwtAuthenticationFilter csak az autentikáció előkészítését végzi el a JWT token alapján, majd az AuthenticationProvider-ek végeznek további autentikációs ellenőrzéseket az Authentication objektum alapján, amelyet a JwtAuthenticationFilter helyezett a biztonsági kontextbe.

//@Transactional annotációval ellátott authenticate metódusban a authenticationManager.authenticate hívás során a UsernamePasswordAuthenticationToken segítségével összehasonlítják a kért e-mailt és jelszót az adatbázisban tárolt adatokkal. Az authenticationManager ezen keresztül használja az authenticationProvider-t, hogy az összehasonlítást elvégezze. Ha a megadott e-mail és jelszó megfelelő, akkor az autentikáció sikeres lesz, és ezt követően a felhasználóhoz JWT tokent generálnak, amit a válaszban visszaküldenek (AuthenticationResponse objektumban).

//Amikor egy kérést elküldesz a /api/authenticate végpontra, először a SecurityConfiguration osztályban található securityFilterChain metódus fut le. Ez a metódus beállítja a biztonsági konfigurációt, és meghatározza, hogy mely végpontokhoz kell hozzáférni a bejelentkezés nélküli hozzáféréssel, és melyekhez csak bejelentkezett felhasználók férhetnek hozzá. Ebben az esetben a /api/register és /api/authenticate végpontokhoz van engedélyezve a hozzáférés a bejelentkezés nélküli felhasználók számára.
//
//Ha a kérésed a /api/authenticate végpontra érkezik, és a securityFilterChain alapján engedélyezett, akkor a SecurityConfiguration beállítja a biztonságot a beépített Spring Security szűrők segítségével. Ezek között van a UsernamePasswordAuthenticationFilter, ami felelős a felhasználói azonosításért és autentikációért.
//
//Ezután a kérésed továbbításra kerül az /api/authenticate végpontot kezelő authenticate metódushoz a AuthenticationController osztályban. Itt az adott felhasználói kérés (AuthenticationRequest) autentikációja történik meg a authenticationManager.authenticate során. Ha az autentikáció sikeres, akkor a personRepository segítségével megkeresi az adott felhasználót az e-mail cím alapján, majd létrehozza a JWT token-t, amit visszaad a kliensnek a válaszként (AuthenticationResponse). Ha az autentikáció nem sikerül, akkor egy kivétel keletkezik, és a válasz hibás lesz.
//
//Röviden: A kérésed először áthalad a biztonsági konfiguráción és az autentikáció szűrőin (UsernamePasswordAuthenticationFilter), majd a AuthenticationController kezeli az autentikációt és a válasz generálását.

// Az /api/register és /api/authenticate végpontok esetén a JWT token nem jön még képbe az autentikáció során, mivel ezek a végpontok valószínűleg a felhasználó regisztrációjához és az első belépéshez szükségesek. Az autentikáció itt a authenticationManager.authenticate(...) hívás segítségével történik, ahol a felhasználó e-mailjét és jelszavát használják fel.
//
//A JwtAuthenticationFilter és az authenticationProvider beállítások akkor jönnek képbe, amikor olyan végpontokhoz szeretnél hozzáférést biztosítani, amelyek védettek JWT token segítségével. Ekkor az authenticationProvider az autentikáció alapját biztosítja, és a JwtAuthenticationFilter kinyeri a JWT tokent a kérésből, majd az authenticationProvider segítségével hajtja végre az autentikációt. Ezután a felhasználó autentikált állapotba kerül, és hozzáférést kap a védett végpontokhoz.
//
//Szóval a JWT token és az ezeket kezelő komponensek akkor lépnek képbe, amikor védett végpontokhoz szeretnél hozzáférést biztosítani és JWT-t használni az autentikációhoz. Az /api/register és /api/authenticate végpontok az autentikáció kezdeti lépéseit jelentik, és nem szükséges itt a JWT token.
