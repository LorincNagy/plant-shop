// eslint-disable-next-line no-unused-vars
import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { Link as RouterLink, useNavigate } from 'react-router-dom';

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
      sx={{
        color: '#000000', // Fekete szín beállítása
        marginTop: '20px', // Például a szöveg kicsit lejjebb helyezése
      }}
    >
      {'Copyright © '}
      <Link color="#000000" href="https://mui.com/">
        Plantify
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const defaultTheme = createTheme({
  palette: {
    primary: {
      main: '#FF5733', // Narancssárga szín beállítása
    },
  },
});

export default function SignIn() {
  const navigate = useNavigate();

  const submitForm = async (event) => {
    event.preventDefault();
    const data = new FormData(event.target);

    const requestBody = {
      email: data.get('email'),
      password: data.get('password'),
    };

    try {
      const response = await fetch('/api/authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        throw new Error('ERROR: Failed to send request to server.');
      } else {
        console.log('Successfully sent request to server.');
        const data = await response.json();
        // Token a válaszból jön, mentés a localStorage-ban
        localStorage.setItem('token', data.token);
        // Átirányítás a védett oldalra
        // window.location.href = "/products"; // Eredeti átirányítás
        navigate('/products');
      }
    } catch (error) {
      console.error('Failed to send request:', error);
    }
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <CssBaseline />
      <Grid container component="main" sx={{ height: '100vh' }}>
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage:
              'url(https://images.unsplash.com/photo-1603912699214-92627f304eb6?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2825)',
            backgroundRepeat: 'no-repeat',
            backgroundColor: (t) =>
              t.palette.mode === 'light'
                ? t.palette.grey[50]
                : t.palette.grey[900],
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        >
          <Box
            component="h1"
            variant="h2"
            sx={{
              textAlign: 'center',
              position: 'absolute',
              top: '7%', // Position the text in the middle vertically
              left: '50%', // Position the text in the middle horizontally
              transform: 'translate(-50%, -50%)', // Center the text
              zIndex: 1,
              fontFamily: 'Murray Text',
              fontSize: '10em',
              textShadow: '0px 4px 4px rgba(0, 0, 0, 1)',
              fontWeight: 'Medium',
              width: '100%',
              color: 'white',
            }}
          ></Box>
        </Grid>
        <Grid
          item
          xs={12}
          sm={8}
          md={5}
          component={Paper}
          elevation={6}
          square
          sx={{
            backgroundColor: '#ffe0b2', // A háttérszín beállítása
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Box
            sx={{
              my: 8,
              mx: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'black' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box
              component="form"
              noValidate
              onSubmit={submitForm}
              sx={{ mt: 1 }}
            >
              <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                InputProps={{
                  sx: {
                    '&:focus': {
                      backgroundColor: '#FF5733', // Narancssárga háttérszín beállítása kattintáskor
                      borderColor: '#FF5733', // Narancssárga keret beállítása kattintáskor
                      color: '#000000', // Fekete szöveg beállítása kattintáskor
                    },
                  },
                }}
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                InputProps={{
                  sx: {
                    '&:focus': {
                      backgroundColor: '#FF5733', // Narancssárga háttérszín beállítása kattintáskor
                      borderColor: '#FF5733', // Narancssárga keret beállítása kattintáskor
                      color: '#000000', // Fekete szöveg beállítása kattintáskor
                    },
                  },
                }}
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
                  mt: 3,
                  mb: 2,
                  backgroundColor: '#000000', // Alapértelmezett fekete háttérszín beállítása
                  color: '#ffffff', // Fehér szövegszín beállítása
                  '&:hover': {
                    backgroundColor: '#FF5733', // Narancssárga háttérszín beállítása, amikor az egér föléviszed
                  },
                }}
              >
                Sign In
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link href="#" variant="body2" style={{ color: '#212121' }}>
                    Forgot password?
                  </Link>
                </Grid>
                <Grid item>
                  <Link
                    component={RouterLink}
                    to="/signup"
                    variant="body2"
                    style={{ color: '#212121' }}
                  >
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>
      </Grid>
      <Copyright />
    </ThemeProvider>
  );
}
