import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="https://mui.com/">
        Plantify
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const defaultTheme = createTheme();

export default function SignUp() {
  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const requestBody = {
      firstName: data.get("firstName"),
      lastName: data.get("lastName"),
      email: data.get("email"),
      phoneNumber: data.get("phoneNumber"),
      address: data.get("address"),
      password: data.get("password"),
    };

    try {
      const response = await fetch("/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      });
      
      if (!response.ok) {
        throw new Error("ERROR: Failed to send request to server.");
      } else {
        console.log("Successfully sent request to server.");
        console.log("haha");
        // Átirányítás a bejelentkezési oldalra
        window.location.href = "/signin";
      }
    } catch (error) {
      console.error("Failed to send request:", error);
    }
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Typography
        component={Box}
        display="flex"
        alignItems="flex-start"
        justifyContent="flex-end"
        sx={{
          backgroundImage:
            'url("https://images.unsplash.com/photo-1603912699214-92627f304eb6?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1pYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2825")',
          backgroundSize: "cover",
          backgroundRepeat: "no-repeat",
          minHeight: "100vh",
          padding: "1em",
        }}
      >
        <Container component="main" maxWidth="md">
          <CssBaseline />
          <Box
            sx={{
              width: "100%",
              backgroundColor: "darkgrey",
              marginTop: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              borderRadius: "2em",
              padding: "2em",
              boxShadow: "0px 10px 20px 0px rgba(0,0,0,1)",
            }}
          >
            <Avatar sx={{ m: 1, color: "black" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign Up to Plantify
            </Typography>
            <Box
              component="form"
              onSubmit={handleSubmit}
              noValidate
              sx={{ mt: 1 }}
            >
              <TextField
                margin="normal"
                required
                fullWidth
                name="firstName" // Javítva "First Name"-ről "firstName"-re
                label="First Name"
                type="text"
                id="firstName"
                autoComplete="given-name"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="lastName" // Javítva "Last Name"-ről "lastName"-re
                label="Last Name"
                type="text"
                id="lastName"
                autoComplete="family-name"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="address" // Itt nincs szükség változtatásra
                label="Address"
                type="text"
                id="address"
                autoComplete="address-line1"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="email" // Itt nincs szükség változtatásra
                label="Email Address"
                type="text"
                id="email"
                autoComplete="email"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="phoneNumber" // Javítva "Phone Number"-ről "phoneNumber"-re
                label="Phone Number"
                type="text"
                id="phoneNumber"
                autoComplete="tel"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password" // Itt nincs szükség változtatásra
                label="Password"
                type="password" // Javítva a típus "text"-ről "password"-ra
                id="password"
                autoComplete="current-password"
              />
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                  width: "46%",
                  height: "2.8em",
                  mx: "auto",
                  mt: 5,
                  mb: 3,
                  ml: 3.8,
                }}
                onClick={() => {
                  console.log("Hello"); // Kiírja a "Hello" üzenetet
                }}
              >
                Sign Up
              </Button>
              <Grid container>
                <Grid item xs></Grid>
              </Grid>
            </Box>
          </Box>
          <Copyright sx={{ mt: 8, mb: 4 }} />
        </Container>
      </Typography>
    </ThemeProvider>
  );
}
