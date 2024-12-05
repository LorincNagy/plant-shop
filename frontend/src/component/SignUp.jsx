import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { Link as RouterLink, useNavigate } from 'react-router-dom';

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        Plantify
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

function getPasswordStrength(password) {
  const passwordStrength = {
    minLength: password.length >= 8,
    hasUppercase: /[A-Z]/.test(password),
    hasLowercase: /[a-z]/.test(password),
    hasNumber: /\d/.test(password),
  };

  return passwordStrength;
}

function getPasswordStrengthMessage(passwordStrength) {
  if (
    passwordStrength.minLength &&
    passwordStrength.hasUppercase &&
    passwordStrength.hasLowercase &&
    passwordStrength.hasNumber
  ) {
    return 'Strong';
  } else if (
    passwordStrength.minLength &&
    (passwordStrength.hasUppercase ||
      passwordStrength.hasLowercase ||
      passwordStrength.hasNumber)
  ) {
    return 'Moderate';
  } else {
    return 'Weak';
  }
}

const defaultTheme = createTheme({
  palette: {
    primary: {
      main: '#FF5733', // Narancssárga szín beállítása
    },
  },
});

export default function SignUp() {
  const navigate = useNavigate();
  const [passwordStrength, setPasswordStrength] = React.useState({
    minLength: false,
    hasUppercase: false,
    hasLowercase: false,
    hasNumber: false,
  });

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const requestBody = {
      firstName: data.get('firstName'),
      lastName: data.get('lastName'),
      email: data.get('email'),
      phoneNumber: data.get('phoneNumber'),
      address: data.get('address'),
      password: data.get('password'),
    };

    try {
      const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        alert('This email address is already registered.');
        throw new Error('ERROR: Failed to send request to server.');
      } else {
        console.log('Successfully sent request to server.');
        const data = await response.json();
        localStorage.setItem('token', data.token);

        // Átirányítás a bejelentkezési oldalra
        navigate('/signin');
      }
    } catch (error) {
      console.error('Failed to send request:', error);
    }
  };

  const handlePasswordChange = (event) => {
    const password = event.target.value;
    const strength = getPasswordStrength(password);
    setPasswordStrength(strength);
  };

  const passwordStrengthMessage = getPasswordStrengthMessage(passwordStrength);

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
          backgroundSize: 'cover',
          backgroundRepeat: 'no-repeat',
          minHeight: '100vh',
          padding: '1em',
        }}
      >
        <Container component="main" maxWidth="md">
          <CssBaseline />
          <Box
            sx={{
              width: '100%',
              backgroundColor: '#ffe0b2', // A háttérszín beállítása
              marginTop: 1,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              borderRadius: '2em',
              padding: '2em',
              boxShadow: '0px 10px 20px 0px rgba(0,0,0,1)',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'black' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign Up to Plantify
            </Typography>
            <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                name="firstName"
                label="First Name"
                type="text"
                id="firstName"
                autoComplete="given-name"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="lastName"
                label="Last Name"
                type="text"
                id="lastName"
                autoComplete="family-name"
              />
              <TextField
                margin="normal"
                fullWidth
                name="address"
                label="Address"
                type="text"
                id="address"
                autoComplete="address-line1"
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="email"
                label="Email Address"
                type="email" // Az email típus használata az email validációhoz
                id="email"
                autoComplete="email"
              />
              <TextField
                margin="normal"
                fullWidth
                name="phoneNumber"
                label="Phone Number"
                type="tel" // A tel típus használata a telefonszám mezőhöz
                id="phoneNumber"
                autoComplete="tel"
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
                onChange={handlePasswordChange}
              />
              <p>Password Strength: {passwordStrengthMessage}</p>
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  mt: 2,
                }}
              >
                <Button
                  type="submit"
                  variant="contained"
                  sx={{
                    width: '45%', // A gomb szélessége
                    backgroundColor: '#000000',
                    color: '#ffffff',
                    '&:hover': {
                      backgroundColor: '#FF5733',
                    },
                  }}
                >
                  Sign Up
                </Button>
                <Link component={RouterLink} to="/signin" variant="body2">
                  Already have an account? Sign in
                </Link>
              </Box>
            </Box>
          </Box>
          <Copyright sx={{ mt: 8, mb: 4 }} />
        </Container>
      </Typography>
    </ThemeProvider>
  );
}
