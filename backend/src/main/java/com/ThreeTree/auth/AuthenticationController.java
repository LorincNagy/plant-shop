package com.ThreeTree.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/api/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/api/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            System.out.println("Received authentication request: " + request.toString());
            return ResponseEntity.ok(service.authenticate(request));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

//A BadCredentialsException általában arra utal, hogy valamilyen hibás hitelesítési adatot adtak meg, például rossz jelszót vagy felhasználónevet. Ez a kivételáltalában nem tartalmazza a konkrét okot, hogy mi okozta a hibát. Tehát nem lehet direkt módon kinyerni belőle az okot.
//
//A BadCredentialsException-t általában úgy használják, hogy általános hibát jelentsen az autentikációs folyamat során, anélkül hogy részletezné, hogy mi volt a hiba pontos ok. Ezért általában célszerű előzetesen ellenőrizni a felhasználó létezését vagy más hitelesítési adatokat, és csak akkor hívni az AuthenticationManager-t, hogy pontosan meg tudjuk határozni a hiba okát, és azután megfelelően kezelni azt.
