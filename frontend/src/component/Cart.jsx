import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import { useCart } from "./CartProvider";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CardMedia from "@mui/material/CardMedia";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import Calculator from "./Calculator";

const Cart = () => {
  const navigate = useNavigate();
  const {
    cartitems,
    setCartItems,
    handleRemoveFromCart,
    handleEmptyCart,
    sendCartToBackend,
  } = useCart();

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

  const handleQuantityChange = (productId, newQuantity) => {
    if (newQuantity < 1) {
      return; // Ne engedjük a mennyiség csökkentését 1 alá
    }
    const updatedCartItems = cartitems.map((item) =>
      item.productId === productId ? { ...item, quantity: newQuantity } : item
    );
    setCartItems(updatedCartItems);
    sendCartToBackend(updatedCartItems); // Frissítjük a backendet az új kosártartalommal
  };

  const handlePlaceOrder = () => {
    navigate("/checkout")
  };

  const cartContent =
    cartitems.length === 0
      ? emptyCartMessage
      : cartitems.map((item, index) => (
          <Card
            key={`cartItem_${item.id}_${index}`}
            sx={{
              marginBottom: "16px",
              borderRadius: "10px",
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.8)",
              maxWidth: "400px",
              margin: "0 16px 16px 0",
            }}
          >
            <CardMedia
              component="div"
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "200px",
                backgroundColor: "#f9fbe7",
              }}
            >
              <img
                src={item.image}
                alt={item.name}
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
              />
            </CardMedia>
            <CardContent
              sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
              }}
            >
              <Box>
                <Typography variant="h6">{item.name}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Description: {item.description}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Price: {item.price}
                </Typography>
              </Box>
              <Box
                sx={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between",
                  paddingTop: "8px",
                }}
              >
                <Box>
                  <Button
                    onClick={() =>
                      handleQuantityChange(item.productId, item.quantity - 1)
                    }
                    sx={{ minWidth: "30px" }}
                  >
                    ↓
                  </Button>
                  <Typography sx={{ display: "inline-block", mx: 1 }}>
                    {item.quantity}
                  </Typography>
                  <Button
                    onClick={() =>
                      handleQuantityChange(item.productId, item.quantity + 1)
                    }
                    sx={{ minWidth: "30px" }}
                  >
                    ↑
                  </Button>
                </Box>
                <Button
                  onClick={() => handleRemoveFromCart(item.productId)}
                  variant="outlined"
                  color="secondary"
                  sx={{
                    backgroundColor: "#FF5733",
                    color: "#FFFFFF",
                    "&:hover": { backgroundColor: "#FF8040" },
                  }}
                >
                  Remove
                </Button>
              </Box>
            </CardContent>
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
          justifyContent: "space-between",
          alignItems: "flex-start",
          width: "100%",
          marginTop: "16px",
        }}
      >
        {/* Calculator komponens */}
        <Calculator cartitems={cartitems} />

        <div sx={{ display: "flex", gap: "8px" }}>
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
              marginRight: "8px", // Kis tér hozzáadása a gombok között
            }}
          >
            Close Cart
          </Button>

          {/* Empty Cart gomb */}
          <Button
            variant="outlined"
            color="secondary"
            onClick={() => {
              handleEmptyCart();
            }}
            sx={{
              backgroundColor: "#FF5733",
              color: "#FFFFFF",
              "&:hover": {
                backgroundColor: "#FF8040",
              },
            }}
          >
            Empty Cart
          </Button>
        </div>
      </Box>
      <Typography
        variant="h4"
        component="h2"
        sx={{ textAlign: "center", marginBottom: "16px", flexBasis: "100%" }}
      >
        Your Cart
      </Typography>
      {cartContent}

      {cartitems.length > 0 && (
        <Box sx={{ width: "100%", textAlign: "center", marginTop: 2 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handlePlaceOrder}
          >
            Place Order
          </Button>
        </Box>
      )}
    </div>
  );
};
export default Cart;
