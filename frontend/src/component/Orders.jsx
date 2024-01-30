import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button
} from "@mui/material";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null); 
  const navigate = useNavigate();

  const handleNavigateToProducts = () => {
    navigate('/products'); // Átirányítás a /products oldalra
  };

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("/api/orders", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Hiba történt az adatok lekérése közben");
      }

      const data = await response.json();

      setOrders(data);
    } catch (error) {
      setError(error.message); // Hiba beállítása
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  if (error) {
    return <div>Hiba: {error}</div>; // Hiba megjelenítése
  }
  return (
    <div>
      <h1>Orders</h1>
      <Button onClick={handleNavigateToProducts} variant="contained" color="primary">
        Go to Products
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell align="right">Date</TableCell>
              <TableCell align="right">Total Amount</TableCell>
              <TableCell align="right">Customer</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <React.Fragment key={order.id}>
                {/* Egy sor az order adataival */}
                <TableRow>
                  <TableCell component="th" scope="row">
                    {order.id}
                  </TableCell>
                  <TableCell align="right">
                    {order.orderDate.replace("T", ", ")}
                  </TableCell>
                  <TableCell align="right">{order.orderTotal}</TableCell>
                  <TableCell align="right">
                    {order.person
                      ? `${order.person.firstName} ${order.person.lastName}`
                      : "N/A"}
                  </TableCell>
                </TableRow>

                {/* További sorok az orderhez tartozó order items adataival */}
                {order.orderItemResponses.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell align="right">Item ID: {item.id}</TableCell>
                    <TableCell align="right">
                      Product Name:{" "}
                      {item.productResponse ? item.productResponse.name : "N/A"}
                    </TableCell>
                    <TableCell align="right">
                      Description:{" "}
                      {item.productResponse
                        ? item.productResponse.description
                        : "N/A"}
                    </TableCell>
                    <TableCell align="right">
                      Price:{" "}
                      {item.productResponse
                        ? item.productResponse.price
                        : "N/A"}
                    </TableCell>
                    <TableCell align="right">
                      Quantity: {item.quantity}
                    </TableCell>
                  </TableRow>
                ))}
              </React.Fragment>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default Orders;
