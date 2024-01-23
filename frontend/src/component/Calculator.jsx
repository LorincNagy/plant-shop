import React, { useEffect } from "react";
import Typography from "@mui/material/Typography";

const Calculator = ({ cartitems, onResult }) => {
  const totalPrice = cartitems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  useEffect(() => {
    if (typeof onResult === "function") {
      onResult(totalPrice);
    }
  }, [totalPrice, onResult]);

  return (
    <Typography variant="h6" component="div" align="center">
      Total Price: ${totalPrice.toFixed(2)}
    </Typography>
  );
};

export default Calculator;
