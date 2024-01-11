import React from "react";
import Typography from "@mui/material/Typography";

const Calculator = ({ cartitems }) => {
  // Számítsuk ki a kosárban lévő termékek árainak összegét
  const totalPrice = cartitems.reduce((total, item) => total + item.price, 0);

  return (
    <Typography variant="h6" component="div" align="center">
      Total Price: ${totalPrice.toFixed(2)}
    </Typography>
  );
};

export default Calculator;
