package com.ThreeTree.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/register", "/api/authenticate")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


//authenticationManager.authenticate hívás a UsernamePasswordAuthenticationToken-t használja az autentikáció végrehajtásához. Az AuthenticationManager felelős az autentikáció végrehajtásáért, és az AuthenticationProvider-eket használja fel az autentikációs ellenőrzések végrehajtására. A UsernamePasswordAuthenticationToken a felhasználó által megadott email cím és jelszó alapján létrehozott Authentication objektumot tartalmazza, amelyet az AuthenticationManager feldolgoz és ellenőriz.
//
//A UsernamePasswordAuthenticationToken a Principal és a Credentials (jelszó) információkat tartalmazza, és a Credentials-t ellenőrzi az adatbázisban tárolt adatokkal. Ha a jelszó helyes, és az AuthenticationManager sikeresnek találja az autentikációt, akkor a felhasználót bejelentkezteti, és létrehoz egy sikeres Authentication objektumot. Ezt az objektumot a JWT token generálásához használják.
//
//Az AuthenticationProvider példányok segítenek az AuthenticationManager-nek az autentikáció végrehajtásában. A DaoAuthenticationProvider egy gyakran használt AuthenticationProvider implementáció, amely az UserDetailsService-t és a PasswordEncoder-t használja az autentikációhoz. Az AuthenticationProvider-eket a Spring Security konfigurációjában lehet definiálni és beállítani a megfelelő autentikációs ellenőrzésekkel és az adatbázis eléréssel.

//SecurityContextHolder.getContext().setAuthentication(authToken) során beállított authToken tartalmazza a felhasználó összes adatát, ideértve a jelszót is (hashelt formában). Ezek az adatok kerülnek továbbításra az AuthenticationProvider számára az autentikációs ellenőrzés végrehajtásához.
//
//Az AuthenticationProvider összehasonlítja a kapott adatokat a rendszerben tárolt adatokkal, például az adatbázisban tárolt hashelt jelszóval. Ha az összehasonlítás sikeres, az AuthenticationProvider sikeres autentikációnak tekinti a kérést, és létrehoz egy teljes autentikációs objektumot (Authentication), amely tartalmazza a felhasználóhoz kapcsolódó jogosultságokat és egyéb információkat. Ezt az autentikációs objektumot továbbítja a biztonsági rendszernek az engedélyezési döntések meghozatalához.
//
//Így a SecurityContextHolder a JwtAuthenticationFilter segítségével előkészíti az autentikációs objektumot, amelyet az AuthenticationProvider használ a tényleges autentikációhoz és az engedélyezési folyamathoz.

//A JwtAuthenticationFilter előkészíti az autentikációs objektumot a JWT token alapján, majd a SecurityContextHolder-be helyezi. Azután az AuthenticationProvider kerül képbe, amely további autentikációs ellenőrzéseket végez, és eldönti, hogy a felhasználó hitelesített-e vagy sem. Ha az autentikáció sikeres, az AuthenticationProvider létrehozza és visszaadja a megfelelő Authentication objektumot, amely tartalmazza a felhasználó adatait és jogosultságait.

//A JWT token egy adatszállító eszköz, amely azonosító adatokat szállít a kérésben, és a JwtAuthenticationFilter előszűri a kérést, kinyeri a JWT tokenből az azonosító információkat, majd az SecurityContextHolder-be helyezi az autentikációs objektumot. Az AuthenticationProvider a SecurityContextHolder-ből kinyeri az autentikációs objektumot, és további autentikációs ellenőrzéseket végez, hogy meghatározza, hogy az autentikáció sikeres volt-e vagy sem.

// JWT szűrő (jwtAuthFilter) felelős a JWT tokenek érvényességének ellenőrzéséért és a tartalmuk kinyeréséért. A szűrők általában az Authentication objektumokat hozzák létre és állítják be, majd továbbítják azokat az AuthenticationProvider-nek az autentikáció végső ellenőrzésére.

//mind a JWT (JSON Web Token) ellenőrző szűrő (pl. jwtAuthFilter), mind a hagyományos felhasználóneves/jelszavas autentikáció szűrője (pl. UsernamePasswordAuthenticationFilter.class) előkészíti az adatokat az autentikációhoz, majd átadja azokat az AuthenticationProvider-nek. Az AuthenticationProvider felelős a tényleges autentikációs folyamat végrehajtásáért, például a felhasználónév és jelszó ellenőrzéséért vagy a JWT érvényességének ellenőrzéséért.
//
//Ez a szűrők és AuthenticationProvider-ek kombinációja lehetővé teszi az alkalmazásnak, hogy támogassa mind a hagyományos felhasználóneves/jelszavas autentikációt, mind a token alapú autentikációt, és rugalmasan döntsön az autentikáció módjáról az adott kérés alapján.

//A UsernamePasswordAuthenticationFilter létrehozza a UsernamePasswordAuthenticationToken objektumot a felhasználónevével és jelszavával, majd végrehajtja az autentikációt azzal, hogy az autentikációs objektumot átadja az AuthenticationProvider-nek. Tehát a tényleges autentikációt az AuthenticationProvider végzi el.
//Az AuthenticationProvider felelős a felhasználó azonosításának és hitelesítésének végrehajtásáért. Ha a UsernamePasswordAuthenticationToken érvényes felhasználói adatokkal rendelkezik, az AuthenticationProvider visszaad egy új Authentication objektumot, amely tartalmazza az autentikált felhasználót és az ő jogosultságait. Ha az autentikáció sikertelen, az AuthenticationProvider hibát jelez.
//Tehát a UsernamePasswordAuthenticationFilter csak előkészíti az autentikációs adatokat, és átadja azokat az AuthenticationProvider-nek, amely végzi el az autentikációt és dönt a hitelességről.

//A addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) pedig azt mondja meg a Spring Security-nek, hogy helyezze el a saját jwtAuthFilter szűrődet a UsernamePasswordAuthenticationFilter előtt a szűrőláncban. Ez azért szükséges, hogy a JWT token alapú autentikációt ellenőrizhesd a felhasználónév-jelszó alapú autentikáció előtt.
// Ha a JWT token alapú autentikáció sikeresen lefut a jwtAuthFilter által, akkor a UsernamePasswordAuthenticationFilter nem fog lefutni, mivel a felhasználó már autentikálva van a JWT token alapján. Tehát a UsernamePasswordAuthenticationFilter szűrőnek nincs szerepe a további autentikációban.
//
//Az authenticationProvider(authenticationProvider) konfiguráció azokra az esetekre vonatkozik, amikor a kérés tartalmaz felhasználónév-jelszó párost, és az alkalmazásnak ilyen alapú autentikációt kell végeznie. Ha a kérés tartalmaz egy JWT tokent, és a jwtAuthFilter sikeresen azonosítja a felhasználót a tokennel, akkor az authenticationProvider által biztosított autentikáció nem fog lefutni a kérés során.
//
//Az authenticationProvider-ek általában azokra a kérésekre vonatkoznak, amelyek felhasználónév-jelszó párost tartalmaznak, és azokat a UsernamePasswordAuthenticationFilter vagy hasonló szűrők kezelik. Ha nincs ilyen típusú autentikáció a kérésben, akkor az authenticationProvider konfiguráció nem fog érintett lenni.

//Először a JwtAuthenticationFilter fut le a JWT token ellenőrzésére és autentikációjára.
//Azután a UsernamePasswordAuthenticationFilter fut le a hagyományos autentikációhoz, ha szükséges.
//Az AuthenticationProvider-ek nem a szűrőlánc részei, hanem az autentikációs logika részei, és az autentikáció során hívódnak meg az általuk támogatott típusú Authentication objektumokkal.
//Az általad felsorolt konfigurációs rész csak akkor kerül érvényre, amikor a kérések a megadott útvonalakon kívül érkeznek. Tehát ha a kérés útvonala az "/api/register" vagy "/api/authenticate" alá tartozik, akkor a .permitAll() beállításnak köszönhetően nincs szükség az azonnali autentikációra, és a kérések engedélyezettek. Ez hasznos lehet például az új felhasználók regisztrációjához vagy a bejelentkezéshez.
//
//Viszont ha a kérés útvonala az ezeken kívül esik, akkor az .anyRequest().authenticated() rész aktiválódik, és az autentikációra van szükség a kérés folytatásához. Az autentikáció elvégzésekor az authenticationProvider és a JWT token ellenőrzése is megtörténik, mielőtt a kérés tovább feldolgozásra kerülne. Ezzel biztosítva van a biztonságos hozzáférés a védett erőforrásokhoz.
// Ez lehetővé teszi számodra, hogy egyedi ellenőrzéseket vagy műveleteket hajts végre a kérés előtt, például JWT token ellenőrzést, és csak azt követően engedélyezze vagy elutasítsa az autentikációt az authenticationProvider használatával.
//
//Ez a sorrend lehetővé teszi, hogy részletesen ellenőrizd a kéréseket, mielőtt a hivatalos autentikációra kerülne sor.
//JWT token ellenőrzése történik először a JwtAuthenticationFilter segítségével. Ez a filter az első, amely megnézi a kérésben található JWT tokent, és ellenőrzi annak érvényességét és tartalmát.
//
//Ha a JWT token érvényes és megfelelő, akkor a felhasználó autentikált, és csak ekkor kerül sor az autentikációt végző authenticationProvider használatára. Az authenticationProvider a felhasználó azonosítását a tokenben található információk alapján végzi el.
//
//Ez a sorrend biztosítja, hogy csak a helyes JWT tokennal rendelkező kérések juthatnak tovább az autentikációig, és csak az autentikált felhasználók kaphatnak hozzáférést a védett erőforrásokhoz.
//.csrf().disable(): Azáltal kikapcsolod a CSRF védelmet. CSRF védelmi intézkedéseket használhatsz, ha az alkalmazásod igényli, de ebben az esetben úgy döntöttél, hogy kikapcsolod.
//
//.authorizeHttpRequests(): Itt állítod be, hogy mely URL-eknek milyen hozzáférési engedélyekkel kell rendelkezniük. A /api/register és /api/authenticate URL-eket engedélyezted a permitAll() metódussal, tehát ezek elérhetők bárki számára az autentikáció nélkül. Az anyRequest().authenticated() azt jelenti, hogy minden más URL csak autentikált felhasználók számára érhető el.
//
//.sessionManagement(): Itt állítod be a munkamenedzsmentet, amit állítottál SessionCreationPolicy.STATELESS-re, ami azt jelenti, hogy nem hoz létre munkameneteket a kérésekhez.
//
//.authenticationProvider(authenticationProvider): Ezzel a beállítással megadod az általad létrehozott authenticationProvider bean-t, ami felelős az autentikáció kezeléséért.
//
//.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class): Itt hozzáadsz egy saját jwtAuthFilter-t a filter lánc elejére, ahol ellenőrzi a JWT tokeneket az autentikációhoz.
//@Bean-ként definiált SecurityFilterChain létrehozásával a SecurityFilterChain konfigurálod a biztonságosságot az alkalmazásodban
//Az authenticationProvider és a JwtAuthFilter különböző funkciókat látnak el:
//
//Az authenticationProvider az autentikáció ellenőrzéséért felelős, és általában a hagyományos felhasználónév és jelszó alapú bejelentkezéseknél használják. Ez ellenőrzi, hogy a megadott felhasználónév és jelszó megfelel-e a tárolt adatoknak.
//
//A JwtAuthFilter az autentikáció és az autorizáció egy részét végzi, de a JWT alapú tokenek érvényességének ellenőrzésére szolgál. Ezzel a szűrővel ellenőrizheted, hogy a kérésben található JWT érvényes-e, és az abban tárolt információk alapján azonosítani lehet-e a felhasználót.
//
//A kettő kombinálása lehetővé teszi a rendszer számára, hogy először az autentikációt végezze el (az authenticationProvider segítségével), majd az autorizációt hajtsa végre (a JwtAuthFilter használatával), hogy eldöntse, hogy a felhasználó hozzáférhet-e a kért erőforráshoz vagy sem. E két lépés együttesen biztosítja a biztonságos hozzáférést a védett erőforrásokhoz.
//AuthenticationProvider interfész maga a jelenlegi implementáció. Az interfész definíciója tartalmazza az authenticate és a supports metódusokat, amelyeket minden konkrét AuthenticationProvider implementáció megvalósít. Az alkalmazásodban valóban meg kell adnod az AuthenticationProvider-nek az autentikációs logikát és azt, hogy milyen típusú Authentication objektumokat támogat.
//Az AuthenticationProvider-ek az Authentication objektumok további ellenőrzésére és azonosítására szolgálnak az autentikációs folyamat során. Az AuthenticationProvider-ek felelnek az autentikáció lényegi ellenőrzéséért és a felhasználó azonosításáért a felhasználónév-jelszó páros vagy egyéb autentikációs adatok alapján.
//
//A JwtAuthenticationFilter és a UsernamePasswordAuthenticationFilter az autentikációs folyamat egy korai szakaszában működnek:
//A JwtAuthenticationFilter az érkező JWT tokent ellenőrzi és ellenőrzi annak érvényességét, valamint az esetlegesen tartalmazott felhasználói információkat. Azonban ez még nem magát az autentikációt hajtja végre, csak az érkező tokenek validálására és az azonosított felhasználói információk kinyerésére szolgál.
//A UsernamePasswordAuthenticationFilter pedig az érkező felhasználónév-jelszó párost kezeli, ha a kérés tartalmazza ezeket az adatokat. Szintén csak az autentikáció kezdeti lépéseit hajtja végre, és ellenőrzi, hogy a megadott felhasználónév-jelszó páros helyes-e.
//Az AuthenticationProvider-ek a fent említett szűrők által előkészített Authentication objektumokkal dolgoznak. Például, ha a JwtAuthenticationFilter sikeresen ellenőrizte és azonosította az érkező JWT tokent, akkor egy UsernamePasswordAuthenticationToken típusú Authentication objektumot hoz létre, amely tartalmazza a felhasználó adatait (például az azonosítót), majd ezt az Authentication objektumot továbbítja az AuthenticationProvider-nek. Az AuthenticationProvider végzi el a további ellenőrzéseket, például a jelszó ellenőrzését vagy egyéb ellenőrzéseket, és dönt arról, hogy az autentikáció sikeres volt-e vagy sem.
//Az AuthenticationProvider-ek tehát a fő autentikációs logikát végzik, és az autentikáció mélyebb szintjén dolgoznak, amikor már megvannak az autentikációs adatok. A szűrők csak az autentikáció előkészítését és az alapvető ellenőrzéseket végzik, de az autentikáció tényleges folyamata az AuthenticationProvider-ek feladata.
//Ha a JWT token érvényes és az JwtAuthenticationFilter sikeresen azonosítja a felhasználót a token alapján, akkor a UsernamePasswordAuthenticationFilter soha nem fog lefutni az adott kérés során. Az JwtAuthenticationFilter végzi az autentikációt a token alapján, és ha a token érvényes, akkor a felhasználót azonosították, és nincs szükség további autentikációra a felhasználónév-jelszó alapján.
//UsernamePasswordAuthenticationFilter.class az HTTP Basic Authentication része, amely felhasználónevet és jelszót használ az autentikációhoz.
//
//Az HTTP Basic Authentication lehetővé teszi a felhasználók számára, hogy felhasználónév és jelszó segítségével autentikálják magukat. Az UsernamePasswordAuthenticationFilter ezt az autentikációs mechanizmust kezeli.
//
//Amennyiben a kódja kizárólag JWT token alapú autentikációt használ, és nincs szükség a felhasználónév-jelszó alapú autentikációra, akkor valóban elhagyhatja a UsernamePasswordAuthenticationFilter-t a konfigurációból. A JWT token alapú autentikáció önnek megfelel, és nem szükséges a felhasználónév-jelszó alapú autentikáció használata, hacsak az alkalmazás egyéb okokból nem igényli azt.
//UsernamePasswordAuthenticationFilter-t tartalmaz, amely a Spring Security alapértelmezett implementációja a felhasználónév és jelszó alapú bejelentkezés kezelésére.
//
//Amikor a kódban használod ezt a szűrőt, és nincs érvényes JWT token a kérésben, akkor a UsernamePasswordAuthenticationFilter megpróbálja felhasználónév és jelszó alapján autentikálni a felhasználót. Ha a felhasználó még nincs bejelentkezve, akkor a UsernamePasswordAuthenticationFilter megjeleníti a bejelentkezési ablakot (login formot), és várja a felhasználói adatokat (felhasználónév és jelszó) a bejelentkezéshez. Ez az, amiért a login ablak megjelenik, amíg nincs érvényes JWT token a kérésben.
//
//Ha a kódodban most már JWT token alapú autentikációt használsz, és nincs szükség a felhasználónév-jelszó alapú autentikációra, akkor elhagyhatod a UsernamePasswordAuthenticationFilter-t a konfigurációból, és a login ablak nem fog megjelenni a JWT tokenek használatakor. Az alkalmazásod csak a JWT token alapú autentikációt használja majd a felhasználók azonosítására, és a felhasználónak nem kell felhasználónév és jelszó párt megadnia a bejelentkezéshez.
// JWT token sikeresen beállítódik a SecurityContextHolder-ben, akkor a további autentikációs lépések során a UsernamePasswordAuthenticationFilter vagy más autentikációs szűrők már nem hívják meg az "username" alapú autentikációt, mivel a felhasználó már azonosítva van a JWT token alapján.
//
//Az AuthenticationProvider, például a DaoAuthenticationProvider, továbbra is használhatja a SecurityContextHolder-ben beállított autentikációs adatokat a további autentikációs lépésekhez. Az AuthenticationProvider például megvizsgálhatja, hogy a SecurityContextHolder-ben található Authentication objektum megfelelően van-e konfigurálva és érvényes-e, majd eldöntheti, hogy engedélyezi-e a kérés folytatását vagy sem.
//
//Tehát a válasz a kérdésedre: Igen, az AuthenticationProvider a SecurityContextHolder-ben beállított autentikációs adatokat használhatja a további autentikációs lépésekhez, még akkor is, ha a JWT autentikáció után a UsernamePasswordAuthenticationFilter vagy más szűrők nem hívják meg az "username" alapú autentikációt. Az AuthenticationProvider az Authentication objektumot használja az autentikációs folyamat végrehajtásához.
