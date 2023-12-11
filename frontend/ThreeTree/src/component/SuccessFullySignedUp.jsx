import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import {ThemeProvider, useTheme} from "@mui/material/styles";

function SuccessfullySignedUp() {
    const theme = useTheme();

    const customStyles = {
        backgroundImage: 'url("https://img.freepik.com/premium-photo/tropical-palm-leaves-jungle-leaves-seamless-vector-floral-pattern-background_354831-3425.jpg")',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        height: '100vh',
        overflow: 'hidden',
    };

    return (
        <ThemeProvider theme={theme}>
            <div
                style={customStyles}
            >
                <Container maxWidth="sm">
                    <Typography variant="h4" gutterBottom
                                style={{fontSize: '45px', fontWeight: 'bold', color: 'antiquewhite'}}>
                        Successfully Signed Up
                    </Typography>
                    <Typography variant="h4" gutterBottom
                                style={{fontSize: '25px', fontWeight: 'bold', color: 'antiquewhite'}}>
                        Congratulations! You have successfully signed up.
                    </Typography>
                </Container>
            </div>
        </ThemeProvider>
    )
}


export default SuccessfullySignedUp;

