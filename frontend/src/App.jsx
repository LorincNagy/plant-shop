import React from "react";
import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import { CartProvider } from "./component/CartProvider";

import Home from "./component/Home";
import SignUp from "./component/SignUp";
import SignIn from "./component/SignIn";
import Cart from "./component/Cart";
import Products from "./component/Products";
import Checkout from "./component/CheckOut";
import ThankYou from "./component/ThankYou";
import Orders from "./component/Orders";

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
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/thank-you" element={<ThankYou />} />
            <Route path="/orders" element={<Orders />} />
          </Routes>
        </CartProvider>
      </div>
    </BrowserRouter>
  );
}

export default App;
