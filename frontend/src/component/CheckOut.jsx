import React, { useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Toolbar from "@mui/material/Toolbar";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import { useCart } from "./CartProvider";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import Calculator from "./Calculator";

function Checkout() {
  const { cartitems } = useCart();
  const [customerInfo, setCustomerInfo] = useState({
    address: "",
    email: "",
    phone: "",
  });

  // Állapot létrehozása a kiszámolt összeg számára
  const [totalAmount, setTotalAmount] = useState(0);

  const navigate = useNavigate();

  // A "Calculator" komponens által számolt összeg beállítása
  const handleCalculatorResult = (result) => {
    console.log(result);
    setTotalAmount(result);
  };

  const handlePlaceOrder = async () => {
    const token = localStorage.getItem("token"); // Token szerezzük be localStorage-ból

    // Ellenőrizzük, hogy a token létezik-e
    if (!token) {
      console.error("Nincs token a localStorage-ban.");
      return;
    }

    // A confirm alert megjelenítése
    const confirmOrder = window.confirm(
      "Are you sure you want to place the order?"
    );
    if (confirmOrder) {
      try {
        const response = await fetch("/api/orders", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            customerInfo: customerInfo,
            totalAmount: totalAmount,
          }),
        });

        if (response.ok) {
          // A rendelés sikeresen elküldve
          navigate("/thank-you");
        } else {
          // Valamilyen hiba történt a rendelés során
          console.error("Order placement failed");
        }
      } catch (error) {
        console.error("Error placing order:", error);
      }
    }
  };

  const handleChange = (event) => {
    setCustomerInfo({
      ...customerInfo,
      [event.target.name]: event.target.value,
    });
  };

  return (
    <Container component="main" maxWidth="sm">
      <CssBaseline />
      <AppBar position="relative">
        <Toolbar sx={{ justifyContent: "center" }}>
          <Typography variant="h6" color="inherit" noWrap>
            Plantify Checkout
          </Typography>
        </Toolbar>
      </AppBar>
      <Paper
        variant="outlined"
        sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 } }}
      >
        <Typography component="h1" variant="h4" align="center">
          Order Review
        </Typography>
        {cartitems.map((item, index) => (
          <Box
            key={index}
            sx={{ display: "flex", justifyContent: "space-between", my: 2 }}
          >
            <Typography>
              {item.name} (x{item.quantity})
            </Typography>
            <Typography>${item.price * item.quantity}</Typography>
          </Box>
        ))}

        <Calculator cartitems={cartitems} onResult={handleCalculatorResult} />
        <Typography variant="h5" sx={{ mt: 4 }}>
          Customer Information
        </Typography>
        <TextField
          label="Address"
          name="address"
          fullWidth
          variant="outlined"
          value={customerInfo.address}
          onChange={handleChange}
          required
          sx={{ mt: 2 }}
        />
        <TextField
          label="Email"
          name="email"
          fullWidth
          variant="outlined"
          value={customerInfo.email}
          onChange={handleChange}
          required
          sx={{ mt: 2 }}
        />
        <TextField
          label="Phone Number (Optional)"
          name="phone"
          fullWidth
          variant="outlined"
          value={customerInfo.phone}
          onChange={handleChange}
          sx={{ mt: 2 }}
        />
        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 4 }}>
          <Button
            variant="contained"
            sx={{ mt: 3, ml: 1 }}
            onClick={() => navigate("/cart")}
          >
            Back to Cart
          </Button>
          <Button
            variant="contained"
            sx={{ mt: 3, ml: 1 }}
            onClick={handlePlaceOrder}
          >
            Confirm Order
          </Button>
        </Box>
      </Paper>
    </Container>
  );
}

export default Checkout;
