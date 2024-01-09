import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import { useCart } from "./CartProvider";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CardMedia from "@mui/material/CardMedia";
import { Link as RouterLink, useNavigate } from "react-router-dom";

const Cart = () => {
  const navigate = useNavigate();
  const { cartitems, handleRemoveFromCart } = useCart();

  const emptyCartMessage = (
    <Typography
      variant="h6"
      component="div"
      align="center"
      color="textSecondary"
    >
      Your cart is empty.
    </Typography>
  );

  const cartContent =
    cartitems.length === 0
      ? emptyCartMessage
      : cartitems.map((item, index) => (
          <Card
            key={`cartItem_${item.id}_${index}`}
            sx={{
              marginBottom: "16px",
              backgroundColor: "#f9fbe7", // Háttérszín
              borderRadius: "10px", // Kerekített sarkok
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.8)", // Árnyékolás
              maxWidth: "400px", // Max szélesség
              margin: "0 auto 16px", // Középre igazítás és térköz alul
            }}
          >
            <CardMedia
              component="div"
              sx={{
                display: "flex",
                justifyContent: "center", // Középre vízszintesen
                alignItems: "center", // Középre függőlegesen
              }}
            >
              <img
                src={item.image}
                alt={item.name}
                style={{ maxWidth: "100%", maxHeight: "100%" }}
              />
            </CardMedia>

            <Box sx={{ display: "flex", flexDirection: "column", flex: "1" }}>
              <CardContent sx={{ flex: "1 0 auto" }}>
                <Typography variant="h6" component="div">
                  {item.name}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Description: {item.description}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Price: {item.price}
                </Typography>
              </CardContent>
              <div
                sx={{
                  display: "flex",
                  justifyContent: "flex-end",
                  padding: "8px",
                }}
              >
                <Button
                  variant="outlined"
                  color="secondary"
                  onClick={() => {
                    handleRemoveFromCart(index);
                  }}
                  sx={{
                    backgroundColor: "#FF5733", // Gomb háttérszín
                    color: "#FFFFFF", // Gomb szövegszín
                    "&:hover": {
                      backgroundColor: "#FF8040", // Gomb háttérszín egér fölé helyezéskor
                    },
                  }}
                >
                  Remove
                </Button>
              </div>
            </Box>
          </Card>
        ));

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
            m: 1,
          }}
        >
          Close Cart
        </Button>
      </Box>
      <Typography
        variant="h4"
        component="h2"
        sx={{ textAlign: "center", marginBottom: "16px" }}
      >
        Your Cart
      </Typography>
      {cartContent}
    </div>
  );
};

export default Cart;
