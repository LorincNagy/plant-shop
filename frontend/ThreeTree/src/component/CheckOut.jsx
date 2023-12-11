import * as React from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Toolbar from '@mui/material/Toolbar';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Review from './Review';

function Copyright() {

    return (
        <Typography variant="body2" color="text.secondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                Plantify
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

export default function Checkout() {
    const [orderPlaced, setOrderPlaced] = React.useState(false);

    const handlePlaceOrder = () => {
        setOrderPlaced(true);
    };

    return (
        <React.Fragment>
            <CssBaseline />
            <AppBar position="absolute" elevation={0} sx={{ position: 'relative', borderBottom: (t) => `1px solid ${t.palette.divider}`, backgroundColor: 'grey' }}>
                <Toolbar>
                    <Typography variant="h6" color="black" noWrap>
                        PLantify
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container component="main" maxWidth="sm" sx={{ mb: 4 }} >
                <Paper variant="outlined" sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 } }}>
                    <Typography component="h1" variant="h4" align="center">
                        Checkout
                    </Typography>
                    {!orderPlaced ? (
                        <React.Fragment>
                            <Review />
                            <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                                <Button variant="contained" onClick={handlePlaceOrder} sx={{ mt: 3, ml: 1 }}>
                                    Place order
                                </Button>
                            </Box>
                        </React.Fragment>
                    ) : (
                        <React.Fragment>
                            <Typography variant="h5" gutterBottom>
                                Thank you for your order.
                            </Typography>
                            <Typography variant="subtitle1">
                                Your order number is #2001539. We have emailed your order confirmation, and will send you an update when your order has shipped.
                            </Typography>
                        </React.Fragment>
                    )}
                </Paper>
                <Copyright />
            </Container>
        </React.Fragment>
    );
}
