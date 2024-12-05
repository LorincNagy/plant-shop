import React, { useState } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Toolbar from '@mui/material/Toolbar';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import { useCart } from './CartProvider';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import Calculator from './Calculator';

function Checkout() {
  const { cartitems, setCartItems, handleSignOut } = useCart();
  const [customerInfo, setCustomerInfo] = useState({
    address: '',
    email: '',
    phone: '',
  });
  const [totalAmount, setTotalAmount] = useState(0);
  const navigate = useNavigate();

  const handleCalculatorResult = (result) => {
    setTotalAmount(result);
  };

  const handleSubmit = async (event) => {
    event.preventDefault(); // Megakadályozza az űrlap alapértelmezett elküldését
    const token = localStorage.getItem('token');

    if (!token) {
      console.error('Nincs token a localStorage-ban.');
      return;
    }

    const confirmOrder = window.confirm(
      'Are you sure you want to place the order?',
    );
    if (confirmOrder) {
      try {
        const response = await fetch('/api/orders', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            address: customerInfo.address,
            email: customerInfo.email,
            phone: customerInfo.phone,
            orderTotal: totalAmount,
          }),
        });
        if (response.ok) {
          setCartItems([]);
          navigate('/thank-you');
        } else {
          console.error('Order placement failed');
        }
      } catch (error) {
        console.error('Error placing order:', error);
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
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Typography variant="h6" color="inherit" noWrap>
            Plantify Checkout
          </Typography>
          <Button
            variant="contained"
            onClick={handleSignOut}
            sx={{
              backgroundColor: '#FF5733',
              '&:hover': {
                backgroundColor: '#FF8040',
              },
              color: 'white',
            }}
          >
            Sign out
          </Button>
        </Toolbar>
      </AppBar>
      <Paper
        variant="outlined"
        sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 } }}
      >
        <Typography component="h1" variant="h4" align="center">
          Order Review
        </Typography>
        {/* Cart items list */}
        {cartitems.map((item, index) => (
          <Box
            key={index}
            sx={{ display: 'flex', justifyContent: 'space-between', my: 2 }}
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
        <form onSubmit={handleSubmit}>
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
            label="Phone Number"
            name="phone"
            fullWidth
            variant="outlined"
            value={customerInfo.phone}
            onChange={handleChange}
            required
            sx={{ mt: 2 }}
          />
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
            <Button
              variant="contained"
              onClick={() => navigate('/cart')}
              sx={{ mt: 3, ml: 1 }}
            >
              Back to Cart
            </Button>
            <Button type="submit" variant="contained" sx={{ mt: 3, ml: 1 }}>
              Confirm Order
            </Button>
          </Box>
        </form>
      </Paper>
    </Container>
  );
}

export default Checkout;
