package com.ThreeTree.service;

import com.ThreeTree.dao.CartItemRepository;
import com.ThreeTree.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializálja a mockokat és az injektálást is elvégzi
    }

    @Test
    void testAddCartItem() {

        CartItem cartItem = new CartItem();


        cartItemService.addCartItem(cartItem);


        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testFindById() {

        Long expectedId = 1L;
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(expectedId);

        // Mockolás: Amikor a findById metódust meghívják az expectedId-val, akkor a mockolt objektum visszaadja az expectedCartItem-ot
        when(cartItemRepository.findById(expectedId)).thenReturn(Optional.of(expectedCartItem));

        // Cselekvés: Meghívja a findById metódust az elvárt ID-val
        Optional<CartItem> actualResult = cartItemService.findById(expectedId);

        // Ellenőrzés: Megvizsgálja, hogy a visszaadott entitás megegyezik-e az elvárttal
        assertTrue(actualResult.isPresent()); // Ellenőrzi, hogy van-e eredmény
        assertEquals(expectedCartItem, actualResult.get()); // Ellenőrzi, hogy az eredmény megegyezik-e az elvárásokkal
    }


    @Test
    void testDelete() {

        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);


        doNothing().when(cartItemRepository).delete(cartItem);


        cartItemService.delete(cartItem);


        verify(cartItemRepository, times(1)).delete(cartItem);
    }

}


//A megadott kódrészletben a CartItemService függőségeit (ebben az esetben a CartItemRepository-t) nem a Mockito @InjectMocks annotációja, hanem közvetlenül a setUp metódusban manuálisan injektálod. Itt az injektálás azt jelenti, hogy a CartItemRepository mock példányát közvetlenül átadod a CartItemService konstruktorának:
//
//java
//Copy code
//@BeforeEach
//void setUp() {
//    MockitoAnnotations.initMocks(this);
//    cartItemService = new CartItemService(cartItemRepository);
//}
//Ebben az esetben a Mockito @InjectMocks annotáció nélkül a CartItemService példányosítása és a függőségének (a CartItemRepository mock) beállítása explicit módon, kézzel történik. Az initMocks(this) hívás a @Mock annotációval létrehozott objektumokat inicializálja, de magától még nem injektál függőségeket az @InjectMocks annotáció nélküli osztályokba. Ahhoz, hogy a CartItemService példányba beinjektáld a függőségeit, neked kell példányosítanod azt a konstruktor (vagy setter metódus) segítségével, ahol átadod a szükséges függőségeket.
//Az @InjectMocks annotáció jelzi, hogy a Mockito-nak automatikusan be kell injektálnia a @Mock (vagy @Spy) annotációval létrehozott függőségeket az adott osztály példányába. Azonban maga az injektálás folyamata – tehát a mockolt függőségek fizikai beillesztése az @InjectMocks-szel jelölt osztály példányába – a MockitoAnnotations.openMocks(this) (vagy a korábbi verziókban használt MockitoAnnotations.initMocks(this)) metódus hívásakor történik.
//
//Tehát a válasz a kérdésedre:
//
//Az @InjectMocks annotáció jelöli azon osztály példányát, amelybe a Mockito be fogja injektálni a mockolt függőségeket. Ez magában nem hajt végre injektálást; csak "megjelöli" az osztályt, mint injektálásra kész célpontot.
//
//A MockitoAnnotations.openMocks(this) hívás indítja el az injektálási folyamatot a teszt életciklusának kezdetén (általában a @BeforeEach annotációval jelölt metódusban). Ez a metódus felkeresi az @InjectMocks-szel jelölt osztály példányát, és beinjektálja bele a @Mock-kal (vagy @Spy-al) létrehozott függőségeket.
//
//Ezért a tesztelési kódodban mindkettőre szükséged van ahhoz, hogy a mockolt függőségek sikeresen be legyenek injektálva
//MockitoAnnotations.initMocks(this) (Korábbi Mockito verziókban)
//Ez a metódus inicializálja az aktuális teszt osztályban meghatározott összes mockot (@Mock, @Spy) és elvégzi az @InjectMocks annotációval jelölt osztály példányának függőség-injektálását. A MockitoAnnotations.initMocks(this) már elavult a Mockito újabb verzióiban, helyette MockitoAnnotations.openMocks(this) használata ajánlott.
//MockitoAnnotations.openMocks(this) (Újabb Mockito verziókban)
//Hasonlóan az initMocks-hez, ez a metódus is inicializálja a mockokat és elvégzi a függőség-injektálást, de úgy, hogy figyelembe veszi az erőforrások megfelelő kezelését (pl. AutoCloseable interface implementálásával, ami lehetővé teszi az erőforrások automatikus felszabadítását a teszt végén).
//@InjectMocks
//private CartService cartService;
//
//@BeforeEach
//void setUp() {
//    MockitoAnnotations.openMocks(this); // Inicializálja a mockokat és az injektálást is elvégzi
//}
//Ebben az esetben a CartService példány automatikusan létrejön a setUp metódus futtatásakor, és minden @Mock annotációval jelölt függőség be lesz injektálva ebbe a példányba. Nem szükséges explicit módon példányosítanod a CartService-t vagy manuálisan beállítanod a függőségeit.
//
//2. Manuális Inicializálás és Mockok Beállítása:
//Ezzel szemben, ha explicit módon inicializálod a tesztelendő osztályt és a függőségeit, akkor nagyobb kontrollod van a folyamat felett, de több boilerplate kódot is írsz:
//
//java
//Copy code
//@BeforeEach
//void setUp() {
//    MockitoAnnotations.initMocks(this); // Csak a mockokat inicializálja
//    cartItemService = new CartItemService(cartItemRepository); // Explicit inicializálás
//}
//Ebben az esetben explicit módon létrehozod a CartItemService példányt, és manuálisan adod át neki a cartItemRepository mockot. Ez akkor lehet hasznos, ha egyedi konfigurációt szeretnél beállítani vagy ha a tesztelendő osztály nem támogatja jól az automatikus injektálást (például nincsenek megfelelő konstruktorai vagy setter metódusai).
//
//Összegzés:
//Az @InjectMocks használata automatizálja a függőségek injektálását és csökkenti a boilerplate kódot, de feltételezi, hogy a Mockito képes lesz megfelelően kezelni az injektálást az osztályod struktúrája alapján.
//A manuális inicializálás és beállítás nagyobb kontrollt biztosít, de több kódot igényel, és növeli a boilerplate mennyiségét.
//Inicializálás Hiánya: A leggyakoribb ok, hogy az id mező értéke nem lett explicit módon inicializálva az objektum létrehozásakor. Java-ban, ha egy osztály mezőjét nem inicializáljuk explicit módon, akkor az alapértelmezett értéket veszi fel, ami primitív típusoknál 0, objektum referencia típusoknál pedig null.
//
//JPA Entitások: JPA (Java Persistence API) esetében, amikor egy entitást példányosítasz, az id mezője általában akkor kap értéket, amikor az entitást az adatbázisba mented, és az adatbázis generálja az azonosítót (például auto increment használatával). Mivel a tesztkódodban az expectedCartItem objektumot nem mentetted az adatbázisba, és nem állítottál be neki manuálisan id értéket, az id mező null marad.
//Amikor a cartItemService.addCartItem(cartItem) metódust meghívjuk a teszt során, a cartItemRepository mockolt példányának save metódusát "meghívjuk" – de valójában ez a hívás nem eredményez valódi adatbázisműveletet, mivel a repository egy mockolt (meghamisított) objektum.
//
//A verify(cartItemRepository, times(1)).save(cartItem); kód ebben a kontextusban azt ellenőrzi, hogy a mockolt cartItemRepository save metódusa pontosan egyszer meghívásra került-e a teszt során. Ez a fajta ellenőrzés lehetővé teszi, hogy megbizonyosodjunk arról: a cartItemService osztály addCartItem metódusa a várt módon működik, azaz delegálja a mentési feladatot a cartItemRepository-nak.
//@Mock private CartItemRepository cartItemRepository;: Ez a sor egy CartItemRepository típusú mock objektumot deklarál és annotál a @Mock annotációval. Ez a mock objektum arra szolgál, hogy szimulálja a valódi CartItemRepository működését a tesztek során.
//
//MockitoAnnotations.initMocks(this);: Ez a sor az összes @Mock annotációval jelzett mezőt inicializálja mock objektummá az adott osztályban. Ez azt jelenti, hogy a cartItemRepository most már egy inicializált és használható mock objektum lesz.
//
//cartItemService = new CartItemService(cartItemRepository);: Ez a sor létrehozza a CartItemService osztály egy példányát, és injektálja bele a cartItemRepository mock objektumot. Tehát a cartItemService most egy olyan objektum lesz, amely használja a mockolt cartItemRepository-t a tesztek során.
//
//Ez a folyamat segít abban, hogy a tesztek során mock objektumokat használjunk a valós adatbázisok vagy szolgáltatások helyett, így a tesztek determinisztikusak és isoláltak lesznek, és könnyen ellenőrizhetővé válnak az alkalmazásunk egyes részei.

//Amikor a cartItemService.addCartItem(cartItem) metódust meghívjuk a teszt során, a cartItemRepository mockolt példányának save metódusát "meghívjuk" – de valójában ez a hívás nem eredményez valódi adatbázisműveletet, mivel a repository egy mockolt (meghamisított) objektum.
//
//A verify(cartItemRepository, times(1)).save(cartItem); kód ebben a kontextusban azt ellenőrzi, hogy a mockolt cartItemRepository save metódusa pontosan egyszer meghívásra került-e a teszt során. Ez a fajta ellenőrzés lehetővé teszi, hogy megbizonyosodjunk arról: a cartItemService osztály addCartItem metódusa a várt módon működik, azaz delegálja a mentési feladatot a cartItemRepository-nak.
//@Mock private CartItemRepository cartItemRepository;: Ez a sor egy CartItemRepository típusú mock objektumot deklarál és annotál a @Mock annotációval. Ez a mock objektum arra szolgál, hogy szimulálja a valódi CartItemRepository működését a tesztek során.
//
//MockitoAnnotations.initMocks(this);: Ez a sor az összes @Mock annotációval jelzett mezőt inicializálja mock objektummá az adott osztályban. Ez azt jelenti, hogy a cartItemRepository most már egy inicializált és használható mock objektum lesz.
//
//cartItemService = new CartItemService(cartItemRepository);: Ez a sor létrehozza a CartItemService osztály egy példányát, és injektálja bele a cartItemRepository mock objektumot. Tehát a cartItemService most egy olyan objektum lesz, amely használja a mockolt cartItemRepository-t a tesztek során.
//
//Ez a folyamat segít abban, hogy a tesztek során mock objektumokat használjunk a valós adatbázisok vagy szolgáltatások helyett, így a tesztek determinisztikusak és isoláltak lesznek, és könnyen ellenőrizhetővé válnak az alkalmazásunk egyes részei.
