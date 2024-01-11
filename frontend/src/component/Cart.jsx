import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import { useCart } from "./CartProvider";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CardMedia from "@mui/material/CardMedia";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import Calculator from "./Caculator";

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
              borderRadius: "10px", // Kerekített sarkok
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.8)", // Árnyékolás
              maxWidth: "400px", // Max szélesség
              margin: "0 16px 16px 0", // Térköz a kártyák között
            }}
          >
            <CardMedia
              component="div"
              sx={{
                display: "flex",
                justifyContent: "center", // Középre vízszintesen
                alignItems: "center", // Középre függőlegesen
                height: "200px", // Állítsd be a kívánt magasságot
                backgroundColor: "#f9fbe7", // Háttérszín
              }}
            >
              <img
                src={item.image}
                alt={item.name}
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
              />
            </CardMedia>

            <Box
              sx={{
                backgroundColor: "#f9fbe7", // Háttérszín
                display: "flex",
                flexDirection: "column",
                flex: "1",
              }}
            >
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
    <div
      style={{
        display: "flex", // Flex layout engedélyezése
        flexWrap: "wrap", // Több sorba törés, ha elfogy a hely
        gap: "16px", // Térköz a kártyák között
        justifyContent: "center", // Középre igazítás
      }}
    >
      {/* Új Box konténer a Calculator komponenshez */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between", // Elemek közötti teret hagy
          alignItems: "flex-start", // Bal felső sarokba igazítja az elemeket
          width: "100%",
          marginTop: "16px",
        }}
      >
        {/* Calculator komponens */}
        <Calculator cartitems={cartitems} />

        {/* Close Cart gomb */}
        <Button
          variant="outlined"
          color="secondary"
          onClick={() => {
            navigate("/products");
          }}
          sx={{
            backgroundColor: "#FF5733",
            color: "#FFFFFF",
            "&:hover": {
              backgroundColor: "#FF8040",
            },
          }}
        >
          Close Cart
        </Button>
      </Box>

      <Typography
        variant="h4"
        component="h2"
        sx={{ textAlign: "center", marginBottom: "16px", flexBasis: "100%" }}
      >
        Your Cart
      </Typography>
      {cartContent}
    </div>
  );
};

export default Cart;
