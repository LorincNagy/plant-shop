import React, { createContext, useContext, useEffect, useState } from "react";

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cartitems, setCartItems] = useState([]);

  useEffect(() => {
    localStorage.setItem("cartItems", JSON.stringify(cartitems));
  }, [cartitems]);

  const handleRemoveFromCart = (productId) => {
    // Megkeressük az adott productId-jű termék indexét a cartitems-ben
    const removeItem = cartitems.find((item) => item.productId === productId);
    if (removeItem === -1) return;

    const updatedCartItems = [...cartitems];
    updatedCartItems.splice(removeItem, 1);
    setCartItems(updatedCartItems);
    sendRemoveRequestToBackend(productId);
  };

  const sendRemoveRequestToBackend = async (productId) => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("Nincs token a localStorage-ban.");
        return;
      }

      const response = await fetch(`/api/cart/${productId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Hiba történt a törlés során.");
      }

      console.log("A termék sikeresen eltávolítva a kosárból.");
    } catch (error) {
      console.error("Hiba történt a törlés során:", error);
    }
  };

  const handleEmptyCart = async () => {
    try {
      // Olvasd ki a tokent (például JWT-t) a localStorage-ból
      const token = localStorage.getItem("token");

      // Ha nincs token, akkor megfelelően kezeld le (pl. hibakezelés)
      if (!token) {
        console.log("No token found.");
        return;
      }

      // Elkülded a tokent a szervernek a kérés fejlécében
      const response = await fetch("/api/cart/empty-cart", {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`, // Hozzáadod a tokent a fejléchez
        },
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      // Töröld a localStorage-ban tárolt kosár tartalmat
      localStorage.removeItem("cartItems");

      const data = await response.json();
      console.log("Server response:", data);
      setCartItems([]); // Set an empty cart based on the response
    } catch (error) {
      console.error("An error occurred:", error);
    }
  };

  const sendCartToBackend = async (cartItems) => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("Nincs token a localStorage-ban.");
        return;
      }

      const itemsToSend = cartItems.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
      }));

      const response = await fetch("/api/cart", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(itemsToSend),
      });

      if (!response.ok) {
        throw new Error("Hiba történt a kosár elküldése során.");
      }

      console.log("A kosár sikeresen elküldve a backendnek.");
    } catch (error) {
      console.error("Hiba történt a kosár elküldése során:", error);
    }
  };

  return (
    <CartContext.Provider
      value={{
        cartitems,
        setCartItems,
        handleRemoveFromCart,
        handleEmptyCart,
        sendCartToBackend,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const {
    cartitems,
    setCartItems,
    handleRemoveFromCart,
    handleEmptyCart,
    sendCartToBackend,
  } = useContext(CartContext);

  return {
    cartitems,
    setCartItems,
    handleRemoveFromCart,
    handleEmptyCart,
    sendCartToBackend,
  };
}

// Ebben a részben a useContext(CartContext) segítségével kinyerjük a CartContext-ből az összes szükséges értéket. Az contextCartItems, setCartItems, handleRemoveFromCart és handleEmptyCart változók most elérhetőek a hookon belül, és ezek a változók mutatnak a CartContext által nyújtott értékekre és funkciókra.
// A CartProvider egy kontextust hoz létre, amely tartalmazza a cartitems és a setCartItems állapotot. Ez a kontextus elérhető lesz a kontextust használó összes gyermek komponens számára. Az állapotot itt inicializálják useState segítségével, és bármely komponens hozzáférhet ehhez az állapothoz, amely a CartProvider gyermekként van elhelyezve.

// const {
//   cartitems: contextCartItems,
//   setCartItems,
//   handleRemoveFromCart,
//   handleEmptyCart,
// } = useContext(CartContext);

// return {
//   cartitems: contextCartItems,
//   setCartItems,
//   handleRemoveFromCart,
//   handleEmptyCart,
// };
// Ebben a részben az objektumot hozzuk létre és adjuk vissza a hookból. Az objektum tartalmazza azokat a változókat és funkciókat, amelyeket a komponensek majd felhasználhatnak, amikor használják a useCart hookot. Az objektum kulcsai, például cartitems, setCartItems, handleRemoveFromCart és handleEmptyCart, a kívánt nevek, amelyekkel hozzáférhetsz ezekhez a dolgokhoz a komponensekben.

// Ez a két rész együtt teszi lehetővé a hook számára, hogy könnyen elérhetővé tegye a CartContext által nyújtott értékeket és funkciókat a komponensek számára.

// A useCart hook használatakor egy adott komponensben, pl. a Products komponensben, hozzáférhetsz a cartitems és setCartItems változókhoz, amelyek a CartProvider által biztosított kontextusból származnak. Tehát a Products komponensben hozzáadhatsz vagy módosíthatsz elemeket a cartitems állapotban, és ezek a változások azonnal elérhetőek lesznek más komponensek számára, például a Cart komponensben is, amely ismételten felhasználja a useCart hookot.

//A kód létrehozza a saját CartContext-et a createContext segítségével, majd ezen a Context-en keresztül két saját React hookot definiál: CartProvider és useCart.

// CartContext létrehozása: A createContext függvény létrehozza a Context-et, amely egy speciális objektum, amely lehetővé teszi az adatok megosztását és hozzáférését a komponensek között.

// CartProvider komponens: Ez egy funkcionális komponens, amely a useState hook-ot használja az állapot (state) kezelésére. Az állapotot cartitems néven hozza létre egy üres tömbbel kezdetben. Majd a CartContext.Provider-t használja a gyerekei (children) között, és ezen keresztül megosztja az állapotot a leszármazott komponensekkel.

// useCart hook: Ez egy egyéni hook, amely a useContext hook-ot használja a CartContext-hez való hozzáférésre. A useContext segítségével a komponensek hozzáférhetnek a CartContext-en keresztül megosztott adatokhoz.

// Ez a minta lehetővé teszi az alkalmazás különböző részei között az adatok megosztását anélkül, hogy a komponensek között props-on keresztül kellene átadni azokat. A CartProvider-rel és a useCart-al egyszerűen kezelhető és frissíthető az állapot az alkalmazásban.

// Amikor a Products komponensben hozzáadsz adatokat a cartitems állapothoz a setCartItems segítségével, akkor valójában a CartProvider-ben lévő useState hookhoz adod hozzá az adatokat. Ezek az adatok a CartProvider-en keresztül kerülnek megosztásra más komponensekkel, például a Cart komponenssel.

// A Cart komponensben a const { cartitems } = useCart(); sorral kiolvashatod és használhatod azokat az adatokat, amelyeket a Products komponens adott hozzá a cartitems állapothoz. Így a két komponens közötti adatok megosztása és frissítése nagyon hatékonyan és tisztán történik a kontextus és a hookok használatával.
