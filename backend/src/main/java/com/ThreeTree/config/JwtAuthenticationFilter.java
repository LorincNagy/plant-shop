package com.ThreeTree.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

//A SecurityContextHolder.getContext().getAuthentication() kifejezés arra szolgál, hogy ellenőrizze, hogy az autentikációs objektum már létezik-e a SecurityContextHolder-ben. Ha a felhasználó bejelentkezésnél autentikált, de az autentikációs objektum nincs eltárolva a SecurityContextHolder-ben, akkor az alkalmazás újból autentikálja a felhasználót a JWT alapján.
//UserDetails egy interfész a Spring Security keretrendszerben, amelyet a felhasználói adatok reprezentálására és kezelésére használnak. Az implementációk feladata az, hogy a felhasználói adatokat biztosítsák a keretrendszer számára. Ezáltal az alkalmazás fejlesztőjének feladata egy UserDetailsService implementáció elkészítése, amely a konkrét adatbázisból vagy adatforrásból szolgáltatja a UserDetails objektumokat.
//
//A UserDetailsService egy interfész, amelynek egyetlen metódusa van: loadUserByUsername(String username). Az implementációk ezt a metódust valósítják meg úgy, hogy visszaadják a megadott felhasználónévhez (username) tartozó UserDetails objektumot. Az implementáció felelős az adatbázisból vagy egyéb adatforrásból való adatok lekérdezéséért és azoknak a UserDetails objektumba történő konvertálásáért.
//
//A Spring Security konfigurációjában, mint például a JwtAuthenticationFilter-ben látható, a UserDetailsService-t használják fel a felhasználók betöltéséhez és az autentikációs folyamat során. Ebben a példában a loadUserByUsername metódust hívják meg, hogy betöltsék a felhasználót az e-mail cím alapján.
//
//Az UserDetails interfész a felhasználói adatokat reprezentálja, de az implementációtól függően ezek az adatok a valós adatbázisból vagy más adatforrásból származhatnak. Az implementáció a fejlesztő feladata, és az alkalmazás saját adatforrásához igazodik. Az implementáció a valós adatbázis lekérdezéseket vagy egyéb adatforrás hozzáférést használhat, hogy megszerezze a szükséges adatokat a felhasználókról.
//A jwtService.isTokenValid(jwt, userDetails) függvénynek kellene ellenőriznie, hogy a JWT token érvényes-e és megegyezik-e a felhasználó adataival az adatbázisban.
//
//A jwtService.isTokenValid(...) függvénynek az alábbiakat kellene végrehajtania:
//
//Ellenőrizze, hogy a JWT token érvényes-e (érvényességi idő, aláírás stb. ellenőrzése).
//Ellenőrizze, hogy a tokenben található felhasználói azonosító (pl. email) létezik-e az adatbázisban.
//Ellenőrizze, hogy a tokenben található jogosultságok megegyeznek-e a felhasználó adataival az adatbázisban.
//Ha a jwtService.isTokenValid(...) függvény sikeresen ellenőrzi az összes feltételt, akkor a token érvényesnek tekinthető, és az autentikált felhasználó adatai az authToken objektumban már az SecurityContextHolder-ban lesznek elérhetők.

//A hagyományos felhasználónév-jelszó alapú autentikációnál az AuthenticationManager automatikusan elvégzi az autentikációt és az autentikált felhasználó adatainak elhelyezését a SecurityContextHolder-ben.
//
//JWT alapú autentikáció esetén nincs szükség a hagyományos felhasználónév-jelszó párra, és az AuthenticationManager nem használható a token alapú autentikációhoz. A JWT autentikáció során a felhasználót a token tartalmának alapján az alkalmazásnak kell autentikálnia, és az autentikált állapotot a SecurityContextHolder-be manuálisan kell beállítani, ahogyan azt a bemutatott kódrészletben láthatod.

//A kód egy JWT-t kivon a kérés fejlécéből (authHeader.substring(7)) és kinyeri a felhasználói azonosítót (userEmail = jwtService.extractUserName(jwt)). Majd ellenőrzi, hogy nincs-e már autentikált felhasználó (SecurityContextHolder.getContext().getAuthentication() == null), és ha nincs, akkor egy UsernamePasswordAuthenticationToken objektumot hoz létre a felhasználó adataival és hozzáadja a SecurityContextHolder-hoz.
//
//Ez a megközelítés hasznos lehet abban az esetben, amikor a felhasználó adatait egy külső forrásból, például egy JWT-ből nyerjük ki, és azt szeretnénk használni az alkalmazásban. Ezen kívül, ha a felhasználó azonosítása és autentikációja nem a hagyományos felhasználónév-jelszó párossal történik, hanem egyéb módokon, akkor a manuális hozzáadás lehet a megfelelő megoldás.
//
//Az SecurityContextHolder manuális használata hasznos lehet speciális esetekben, de az alapvető feladatok esetén a Spring Security általában automatikusan kezeli az autentikált felhasználók kezelését és a SecurityContextHolder-t.
//loadUserByUsername metódus a felhasználót az email cím alapján keresi ki az adatbázisból vagy bármely más felhasználói adatforrásból, amit a UserDetailsService implementáció használ. A UserDetails objektum tartalmazza a felhasználó adatait, például a felhasználónevet, a jelszót, a jogosultságokat stb. Az autentikációs logika felhasználja ezt az objektumot az autentikáció során a felhasználó hitelesítéséhez és azonosításához.

// az AuthenticationProvider és a SecurityContextHolder között egyfajta együttműködés van: az AuthenticationProvider hajtja végre az autentikációt és állítja elő az autentikációs objektumot, amelyet a SecurityContextHolder tárol, hogy a későbbi szűrők és komponensek hozzáférhessenek hozzá
//AuthenticationProvider interfész a UsernamePasswordAuthenticationToken objektumokat vagy más típusú Authentication objektumokat kap az autentikációhoz. Az autentikációs logika az AuthenticationProvider implementációjában van, és az ellenőrzés alapján dönti el, hogy az adott autentikáció sikeres vagy sikertelen.
//
//Ha az autentikáció sikeres, az AuthenticationProvider visszaad egy teljesen autentikált Authentication objektumot, amely tartalmazza a felhasználói adatokat és jogosultságokat. Ez az objektum továbbítódik a szűrőláncon keresztül, és elérhető lesz a biztonsági kontextusban a kérés további részeinél.
//
//Tehát az AuthenticationProvider végül az autentikációs folyamatot befejezi, és az autentikált Authentication objektumot adja vissza a további használatra a rendszerben. Az AuthenticationProvider az autentikáció sikerességének függvényében dönt arról, hogy a kérés folytatódik-e vagy sem.

// kódrészletben létrejön egy UsernamePasswordAuthenticationToken objektum (authToken), amelyet a SecurityContextHolder beállít, majd továbbít a további szűrőknek és az AuthenticationProvider-nek is. Ebben a kódrészletben a UsernamePasswordAuthenticationToken objektumot a JWT tokenből kinyert felhasználói adatokkal és az érvényes UserDetails objektummal hozza létre. Ezt az objektumot használja az autentikációhoz, és a beállítások után átadja az autentikációs folyamatnak.
//
//Az AuthenticationProvider a kapott UsernamePasswordAuthenticationToken objektumot használja az autentikációhoz, és ellenőrzi az abban található felhasználói információkat, például a felhasználónév és a jogosultságok alapján. Ha az autentikáció sikeres, akkor az AuthenticationProvider visszaadja az autentikált Authentication objektumot, amely tartalmazza a sikeres autentikációs adatokat.
//A UsernamePasswordAuthenticationFilter és a JWT (JSON Web Token) autentikáció két különálló autentikációs mechanizmus a Spring Security keretrendszerben, és külön-külön működnek.
//
//A UsernamePasswordAuthenticationFilter felhasználónév és jelszó alapú autentikációt kezel. Amikor egy kérés érkezik, és a felhasználó feladja a felhasználónevet és a jelszót, a szűrő létrehoz egy UsernamePasswordAuthenticationToken objektumot, amely tartalmazza ezeket az adatokat, majd az AuthenticationProvider segítségével ellenőrzi a hitelességüket.
//
//A JWT autentikáció a token alapú autentikációt jelenti. Ebben az esetben a kliens egy előre kiadott JWT tokent küld a kérés során. A JWTAuthFilter segítségével ellenőrzöd a tokent, és ha érvényes, létrehozol egy UsernamePasswordAuthenticationToken objektumot a felhasználó adataival, majd a hagyományos autentikációs folyamatot végzed el.
//
//Ezért mindkét mechanizmus saját autentikációs tokeneket használ, de különböző módokon ellenőrzi a felhasználó azonosítását. Nem feltétlenül hoznak létre ugyanazt a típusú token-t, de mindkettő az AuthenticationProvider-en keresztül ellenőrzi a felhasználói azonosítást a saját módján. A UsernamePasswordAuthenticationFilter a hagyományos felhasználónév-jelszó párosát használja, míg a JWT autentikáció egy előre kiadott JWT tokent használ a felhasználó azonosítására.
//Az osztály felelős az HTTP kéréseket ellenőrzéséért, és megnézi, hogy tartalmaznak-e érvényes JWT tokent az "Authorization" fejlécben. Ha nincs JWT token, vagy nem a megfelelő formában szerepel az "Authorization" fejlécben, akkor a kérés továbbítódik a láncban.
//
//Ha az "Authorization" fejléc tartalmaz érvényes JWT tokent, akkor kibővíti a tokent, hogy meghatározza a felhasználó e-mail címét (vagy más azonosítót, amit a token tartalmaz).
//
//Ezután ellenőrzi, hogy a felhasználói azonosító alapján található-e felhasználó az alkalmazásban. Ehhez meghívja a userDetailsService által biztosított loadUserByUsername metódust, ami visszaadja a felhasználó adatait (pl. UserDetails objektumot).
//
//Ha az érvényes JWT és talált felhasználó adatai, akkor létrehoz egy UsernamePasswordAuthenticationToken objektumot a felhasználói azonosítóval, a jogosultságokkal és a kérés részleteivel. Ezután beállítja az autentikációt a SecurityContextHolder segítségével.
//
//Végül folytatja a szűrőláncot, hogy továbbítsa a kérést a következő szűrőnek vagy a végpontnak.
//
//Ez a szűrő tehát az autentikációt végzi el a JWT token alapján, és az autentikációs információkat hozzáadja a SecurityContextHolder-hoz, így a felhasználó bejelentkezettnek lesz tekintve a továbbiakban a kérelem során. A userDetailsService segítségével lehetőség van a felhasználói adatokat betölteni az adatbázisból vagy más forrásból.


//A kérés érkezik az alkalmazásba.
//
//Az HttpSecurity konfigurációban meghatározzák a biztonsági beállításokat, ideértve a UsernamePasswordAuthenticationFilter és a jwtAuthFilter szűrők hozzáadását, valamint az authenticationProvider beállítását.
//
//Amikor egy kérés érkezik, az HttpSecurity beállításoknak megfelelően az alkalmazás először elindítja a jwtAuthFilter szűrőt.
//
//A jwtAuthFilter megpróbálja kinyerni a JWT tokent az Authorization fejlécből. Ha a token érvényes és megtalálható, akkor tovább megyünk a következő lépésre. Ha nincs token vagy érvénytelen, a szűrő egyszerűen átengedi a vezérlést a következő szűrőnek (UsernamePasswordAuthenticationFilter).
//
//Ha van érvényes JWT token, akkor a jwtAuthFilter ellenőrzi a felhasználót a token alapján. Ha a felhasználó érvényes és az autentikáció sikeres, létrehozza az authToken-t, ami egy UsernamePasswordAuthenticationToken példány, és beállítja a felhasználói információkat.
//
//Az authToken-t hozzáadják a SecurityContextHolder-hoz, és itt lesz elérhető a további folyamatokban.
//
//A UsernamePasswordAuthenticationFilter szűrő hozzáfér az authToken-hez a SecurityContextHolder-ból, de mivel a JWT autentikáció már megtörtént, a UsernamePasswordAuthenticationFilter-nek nincs teendője, és egyszerűen továbbadja a vezérlést a következő szűrőnek.
//
//A további szűrők futnak, ha vannak, de nem fognak autentikációt elvégezni, mivel az már megtörtént a jwtAuthFilter-rel.
//
//Ha minden szűrő lefutott, a kérés továbbhalad az alkalmazásodon belül.
//
//Az AuthenticationProvider szolgáltatók közül az alkalmazás a megfelelő szolgáltatót megtalálja, és az ott definiált autentikációs logika végrehajtódik. Az AuthenticationProvider az authToken alapján azonosítja a felhasználót, és megerősíti vagy elutasítja az autentikációt.
//
//Ez a folyamat egy sorrendben zajlik le, és a SecurityContextHolder segít az autentikációs objektum átadásában a megfelelő AuthenticationProvider szolgáltatónak. Az autentikáció után az alkalmazás dönti el, hogy továbbengedi-e a kérést, vagy hibát ad vissza a hibás autentikáció esetén.


//A JwtAuthenticationFilter szűrő valójában az autentikációs folyamat egy részét végzi el, azaz ellenőrzi a JWT tokent és az abból kinyert adatok alapján létrehozza az authToken-t a SecurityContextHolder-ban. Tehát valamennyi autentikációt elvégez, ami a JWT token alapján szükséges.
//Ezután a további autentikációs folyamat, például a felhasználónév és jelszó ellenőrzése, az AuthenticationProvider szolgáltató által végrehajtott autentikációt is magában foglalhatja, ha szükség van rá. Az AuthenticationProvider ellenőrzi az authToken-t és megerősíti az autentikációt, például az adatbázisban tárolt felhasználói adatok alapján.
//Tehát a JwtAuthenticationFilter szűrő előkészíti az autentikációt, és az autentikáció részletes ellenőrzése az AuthenticationProvider-nél történik meg.
