import React from "react";
import CssBaseline from "@mui/material/CssBaseline";
import AppBar from "@mui/material/AppBar";
import Container from "@mui/material/Container";
import Toolbar from "@mui/material/Toolbar";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { useNavigate } from "react-router-dom";

function ThankYou() {
  const navigate = useNavigate();

  const handleBackToHome = () => {
    navigate("/products");
  };

  return (
    <Container component="main" maxWidth="sm">
      <CssBaseline />
      <AppBar position="relative">
        <Toolbar sx={{ justifyContent: "center" }}>
          <Typography variant="h6" color="inherit" noWrap>
            Plantify
          </Typography>
        </Toolbar>
      </AppBar>
      <Paper
        variant="outlined"
        sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 }, textAlign: "center" }}
      >
        <Typography component="h1" variant="h4">
          THANK YOU FOR YOUR ORDER
        </Typography>
        <Button variant="contained" onClick={handleBackToHome} sx={{ mt: 4 }}>
          Back to Home
        </Button>
      </Paper>
    </Container>
  );
}

export default ThankYou;
