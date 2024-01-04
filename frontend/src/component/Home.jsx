import * as React from 'react';
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { Stack } from "@mui/system";

function Index() {
    return (
        <React.Fragment>
            <Typography
                component={Box}
                display="flex"
                alignItems="flex-start"
                justifyContent="flex-end"
                sx={{
                    backgroundImage: `url('https://images.unsplash.com/photo-1603912699214-92627f304eb6?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2825')`,
                    backgroundSize: 'cover',
                    backgroundRepeat: 'no-repeat',
                    minHeight: '100vh',
                    padding: '1em',
                    position: 'relative',
                }}
            >
                <Typography
                    component="h1"
                    variant="h2"
                    color={"text.white"}
                    sx={{
                        textAlign: 'center',
                        position: 'absolute',
                        top: '22%', // Position the text in the middle vertically
                        left: '50%', // Position the text in the middle horizontally
                        transform: 'translate(-50%, -50%)', // Center the text
                        zIndex: 1,
                        fontFamily: 'Murray Text',
                        fontSize: '7.5em',
                        textShadow: '0px 4px 4px rgba(0, 0, 0, 1)',
                        fontWeight: 'Medium',
                        color: 'grey',
                        width: '100%',
                    }}
                >
                    Welcome to Plantify
                </Typography>
                <Box
                    sx={{
                        minHeight: '100vh',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center',
                        alignItems: 'center',
                        padding: '1em',
                        marginLeft: '20em',
                        marginRight: '17em',
                        marginTop: '-4em',
                    }}
                >
                    <Button
                        color='primary'
                        size='large'
                        variant='contained'
                        align="center"
                        onClick={() => {
                            window.location.href = "/products"
                        }}
                    >
                        Visit Store
                    </Button>
                </Box>
                <Stack direction="row" spacing={2}>
                    <Button
                        color='primary'
                        size='large'
                        onClick={() => {
                            window.location.href = "/signin"
                        }}
                    >
                        Sign In
                    </Button>
                    <Button
                        color='primary'
                        size='large'
                        variant='contained'
                        onClick={() => {
                            window.location.href = "/signup"
                        }}
                    >
                        Sign Up
                    </Button>
                </Stack>
            </Typography>
        </React.Fragment>
    )
}

export default Index;
