import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null); // Hiba állapot hozzáadása

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
            {orders.map((order) => {
              console.log(order);
              return (
                <TableRow key={order.id}>
                  <TableCell component="th" scope="row">
                    {order.id}
                  </TableCell>
                  <TableCell align="right">{order.orderDate}</TableCell>
                  <TableCell align="right">{order.orderTotal}</TableCell>
                  <TableCell align="right">{order.personId}</TableCell>{" "}
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default Orders;
