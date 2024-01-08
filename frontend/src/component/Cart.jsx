import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import { useCart } from "./CartProvider";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CardMedia from "@mui/material/CardMedia";
import { Link as RouterLink, useNavigate } from "react-router-dom";

export default function Cart() {
  const navigate = useNavigate();
  const { cartitems, handleRemoveFromCart } = useCart();

  return (
    <div>
      <Box sx={{ position: "relative" }}>
        <Button
          variant="outlined"
          color="secondary"
          onClick={() => {
            navigate("/products");
          }}
          sx={{
            position: "absolute",
            top: 0,
            right: 0,
            m: 1, // Margó a gomb körül
          }}
        >
          Close Cart
        </Button>
      </Box>
      <h2>Your Cart</h2>
      {cartitems.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <div>
          {cartitems.map((item, index) => (
            <Card
              key={`cartItem_${item.id}_${index}`}
              style={{ marginBottom: "16px" }}
            >
              <CardMedia
                component="div"
                sx={{
                  pt: "35%",
                  maxWidth: "40%", // Hozzáadott maxWidth
                }}
                image={item.image}
              />
              <CardContent>
                <Typography variant="h6" component="div">
                  {item.name}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Description: {item.description}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Price: {item.price}
                </Typography>
                <Button
                  variant="outlined"
                  color="secondary"
                  onClick={() => {
                    handleRemoveFromCart(item.id);
                  }}
                  style={{ marginTop: "8px" }}
                >
                  Remove
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
