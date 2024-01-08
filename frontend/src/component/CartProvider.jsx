import React, { createContext, useContext, useEffect, useState } from "react";

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cartitems, setCartItems] = useState(() => {
    const storedCartItems = localStorage.getItem("cartItems");
    return storedCartItems ? JSON.parse(storedCartItems) : [];
  });

  useEffect(() => {
    localStorage.setItem("cartItems", JSON.stringify(cartitems));
  }, [cartitems]);

  const handleRemoveFromCart = (productId) => {
 
    const indexToRemove = cartitems.findIndex((item) => item.id === productId);
    if (indexToRemove !== -1) {
      const updatedCartItems = [...cartitems];
      updatedCartItems.splice(indexToRemove, 1);
      setCartItems(updatedCartItems);
    }
  };

  // Egyéb funkciók és állapotok

  return (
    <CartContext.Provider
      value={{ cartitems, setCartItems, handleRemoveFromCart }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const { cartitems, setCartItems, handleRemoveFromCart } =
    useContext(CartContext);

  return { cartitems, setCartItems, handleRemoveFromCart };
}

//A CartProvider egy kontextust hoz létre, amely tartalmazza a cartitems és a setCartItems állapotot. Ez a kontextus elérhető lesz a kontextust használó összes gyermek komponens számára. Az állapotot itt inicializálják useState segítségével, és bármely komponens hozzáférhet ehhez az állapothoz, amely a CartProvider gyermekként van elhelyezve.

// A useCart hook használatakor egy adott komponensben, pl. a Products komponensben, hozzáférhetsz a cartitems és setCartItems változókhoz, amelyek a CartProvider által biztosított kontextusból származnak. Tehát a Products komponensben hozzáadhatsz vagy módosíthatsz elemeket a cartitems állapotban, és ezek a változások azonnal elérhetőek lesznek más komponensek számára, például a Cart komponensben is, amely ismételten felhasználja a useCart hookot.

//A kód létrehozza a saját CartContext-et a createContext segítségével, majd ezen a Context-en keresztül két saját React hookot definiál: CartProvider és useCart.

// CartContext létrehozása: A createContext függvény létrehozza a Context-et, amely egy speciális objektum, amely lehetővé teszi az adatok megosztását és hozzáférését a komponensek között.

// CartProvider komponens: Ez egy funkcionális komponens, amely a useState hook-ot használja az állapot (state) kezelésére. Az állapotot cartitems néven hozza létre egy üres tömbbel kezdetben. Majd a CartContext.Provider-t használja a gyerekei (children) között, és ezen keresztül megosztja az állapotot a leszármazott komponensekkel.

// useCart hook: Ez egy egyéni hook, amely a useContext hook-ot használja a CartContext-hez való hozzáférésre. A useContext segítségével a komponensek hozzáférhetnek a CartContext-en keresztül megosztott adatokhoz.

// Ez a minta lehetővé teszi az alkalmazás különböző részei között az adatok megosztását anélkül, hogy a komponensek között props-on keresztül kellene átadni azokat. A CartProvider-rel és a useCart-al egyszerűen kezelhető és frissíthető az állapot az alkalmazásban.

// Amikor a Products komponensben hozzáadsz adatokat a cartitems állapothoz a setCartItems segítségével, akkor valójában a CartProvider-ben lévő useState hookhoz adod hozzá az adatokat. Ezek az adatok a CartProvider-en keresztül kerülnek megosztásra más komponensekkel, például a Cart komponenssel.

// A Cart komponensben a const { cartitems } = useCart(); sorral kiolvashatod és használhatod azokat az adatokat, amelyeket a Products komponens adott hozzá a cartitems állapothoz. Így a két komponens közötti adatok megosztása és frissítése nagyon hatékonyan és tisztán történik a kontextus és a hookok használatával.
