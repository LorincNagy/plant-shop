import React from "react";
import Typography from "@mui/material/Typography";

const Calculator = ({ cartitems }) => {
  
  const totalPrice = cartitems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  return (
    <Typography variant="h6" component="div" align="center">
      Total Price: ${totalPrice.toFixed(2)}
    </Typography>
  );
};

export default Calculator;
