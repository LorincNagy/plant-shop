import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from './CartProvider';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Box,
} from '@mui/material';

function Orders() {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const { handleSignOut } = useCart();

  const tableStyle = {
    minWidth: 650,
    maxWidth: '60%',
    margin: 'auto',
  };

  const cellStyle = {
    fontFamily: 'Arial, sans-serif',
    color: 'darkblue',
    fontWeight: 'bold',
    fontSize: '20px',
  };
  const cellStyle2 = {
    fontFamily: 'Arial, sans-serif',
    color: '#009688',
    fontWeight: 'bold',
    fontSize: '16px',
  };

  const handleNavigateToProducts = () => {
    navigate('/products');
  };

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/orders', {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Hiba történt az adatok lekérése közben');
      }

      const data = await response.json();

      setOrders(data);
    } catch (error) {
      setError(error.message);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  if (error) {
    return <div>Hiba: {error}</div>;
  }
  return (
    <div>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        sx={{ padding: '10px' }}
      >
        <h1 style={{ alignSelf: 'center', margin: '10px 0' }}>Orders</h1>
        <Box
          display="flex"
          justifyContent="space-between"
          sx={{ width: '100%', maxWidth: '60%', margin: 'auto' }}
        >
          <Button
            onClick={handleNavigateToProducts}
            variant="contained"
            style={{ backgroundColor: '#FF5733', color: 'white' }}
          >
            Go to Products
          </Button>
          <Button
            onClick={handleSignOut}
            variant="contained"
            style={{ backgroundColor: '#FF5733', color: 'white' }}
          >
            Sign out
          </Button>
        </Box>
      </Box>
      {orders.map((order) => (
        <Box key={order.id} sx={{ marginBottom: '100px' }}>
          <TableContainer component={Paper} sx={tableStyle}>
            <Table aria-label="order table">
              <TableHead>
                <TableRow style={{ backgroundColor: '#f5f5f5' }}>
                  <TableCell
                    style={{ ...cellStyle, width: '25%' }}
                    align="center"
                  >
                    Order ID
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '25%' }}
                    align="center"
                  >
                    Date
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '25%' }}
                    align="center"
                  >
                    Total Amount
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '25%' }}
                    align="center"
                  >
                    Customer
                  </TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                <TableRow>
                  <TableCell
                    style={{ ...cellStyle2, width: '20%' }}
                    align="center"
                  >
                    {order.id}
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle2, width: '20%' }}
                    align="center"
                  >
                    {order.orderDate.replace('T', ',')}
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle2, width: '20%' }}
                    align="center"
                  >
                    {order.orderTotal}
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle2, width: '20%' }}
                    align="center"
                  >
                    {order.person
                      ? `${order.person.firstName} ${order.person.lastName}`
                      : 'N/A'}
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>
          <TableContainer
            component={Paper}
            sx={{ ...tableStyle, marginTop: '20px' }}
          >
            <Table aria-label="order items table">
              <TableHead>
                <TableRow style={{ backgroundColor: '#e8eaf6' }}>
                  <TableCell
                    style={{ ...cellStyle, width: '20%' }}
                    align="center"
                  >
                    Product ID
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '20%' }}
                    align="center"
                  >
                    Product Name
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '20%' }}
                    align="center"
                  >
                    Description
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '20%' }}
                    align="center"
                  >
                    Price
                  </TableCell>
                  <TableCell
                    style={{ ...cellStyle, width: '20%' }}
                    align="center"
                  >
                    Quantity
                  </TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {order.orderItemResponses.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell
                      style={{ ...cellStyle2, width: '20%' }}
                      align="center"
                    >
                      {item.productResponse.productId}
                    </TableCell>
                    <TableCell
                      style={{ ...cellStyle2, width: '20%' }}
                      align="center"
                    >
                      {item.productResponse ? item.productResponse.name : 'N/A'}
                    </TableCell>
                    <TableCell
                      style={{ ...cellStyle2, width: '20%' }}
                      align="center"
                    >
                      {item.productResponse
                        ? item.productResponse.description
                        : 'N/A'}
                    </TableCell>
                    <TableCell
                      style={{ ...cellStyle2, width: '20%' }}
                      align="center"
                    >
                      {item.productResponse
                        ? item.productResponse.price
                        : 'N/A'}
                    </TableCell>
                    <TableCell
                      style={{ ...cellStyle2, width: '20%' }}
                      align="center"
                    >
                      {item.quantity}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      ))}
    </div>
  );
}

export default Orders;
