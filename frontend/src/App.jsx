import React from "react";
import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import { CartProvider } from "./component/CartProvider";

import Home from "./component/Home";
import SignUp from "./component/SignUp";
import SignIn from "./component/SignIn";
import Cart from "./component/Cart";
import Products from "./component/Products";

function App() {
  return (
    <BrowserRouter caseSensitive>
      <div>
        <CartProvider>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/signin" element={<SignIn />} />
            <Route path="/products" element={<Products />} />
            <Route path="/cart" element={<Cart />} />
          </Routes>
        </CartProvider>
      </div>
    </BrowserRouter>
  );
}

export default App;
