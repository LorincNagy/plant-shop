import React, { useEffect, useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import PaginationControlled from './PaginationControlled.jsx';
import { useCart } from './CartProvider';
import { useNavigate } from 'react-router-dom';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

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

const getStockStyles = (product) => {
  const stock = product.stock;
  if (stock <= 0) {
    return {
      quantityColor: 'red',
      message: 'Out of stock',
    };
  } else if (stock <= 5) {
    return {
      quantityColor: 'orange',
      message: `Only ${stock} ${stock === 1 ? 'item' : 'items'} left`,
    };
  } else {
    return {
      quantityColor: 'inherit',
      message: '',
    };
  }
};

const customTheme = createTheme({
  palette: {
    background: {
      default: '#f9fbe7',
    },
  },
  components: {
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .focused': {
            color: '#FF5733 !important',
            borderColor: '#FF5733 !important',
          },
        },
      },
    },
  },
});

const fetchProducts = async () => {
  const token = localStorage.getItem('token');
  if (!token) {
    console.error('Nincs token a localStorage-ban.');
    return null;
  }

  try {
    const response = await fetch('/api/products', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Hiba történt a védett erőforrások lekérése során.');
    }

    let products = await response.json();

    products = products.sort((a, b) => a.productId - b.productId);

    const productsWithQuantity = products.map((product) => ({
      ...product,
      quantity: 0,
    }));

    return productsWithQuantity;
  } catch (error) {
    console.error('Hiba történt a termékek lekérése során:', error);
    return null;
  }
};

function displayProducts(products, page, pageSize) {
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  return products.slice(startIndex, endIndex);
}

const pageSize = 9;

export default function Products() {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const { cartitems, setCartItems, sendCartToBackend, handleSignOut } =
    useCart();
  const [productQuantities, setProductQuantities] = useState({});
  const [snackbarInfo, setSnackbarInfo] = useState({
    open: false,
    message: '',
    severity: 'info',
  });
  const navigate = useNavigate();

  const handleNavigateToOrders = () => {
    navigate('/orders');
  };

  const handleQuantityChange = (productId, newValue) => {
    const validNewValue = newValue < 0 ? 0 : newValue;

    setProductQuantities((prevQuantities) => ({
      ...prevQuantities,
      [productId]: validNewValue,
    }));
  };
  const totalQuantity = cartitems
    ? cartitems.reduce((total, item) => total + item.quantity, 0)
    : 0;

  const handleAddToCart = (product) => {
    const quantityInCart = cartitems.reduce((total, item) => {
      if (item.productId === product.productId) {
        return total + item.quantity;
      }
      return total;
    }, 0);

    const quantityToAdd = productQuantities[product.productId] || 0; // Módosítás itt

    const totalQuantity = quantityInCart + quantityToAdd;

    if (totalQuantity <= product.stock) {
      if (quantityToAdd > 0) {
        if (product.stock === 0) {
          setSnackbarInfo({
            open: true,
            message: 'This product is out of stock.',
            severity: 'error',
          });
        } else if (product.stock <= 5) {
          setSnackbarInfo({
            open: true,
            message:
              'Low stock warning: This product has limited stock available.',
            severity: 'warning',
          });
        }

        const existingCartItemIndex = cartitems.findIndex(
          (item) => item.productId === product.productId,
        );

        let updatedCartItems = [];

        if (existingCartItemIndex !== -1) {
          updatedCartItems = [...cartitems];
          updatedCartItems[existingCartItemIndex] = {
            ...cartitems[existingCartItemIndex],
            quantity: cartitems[existingCartItemIndex].quantity + quantityToAdd,
          };
        } else {
          updatedCartItems = [
            ...cartitems,
            {
              ...product,
              quantity: quantityToAdd,
            },
          ];
        }

        setCartItems(updatedCartItems);
        sendCartToBackend(updatedCartItems);

        setSnackbarInfo({
          open: true,
          message: 'Product added to cart successfully.',
          severity: 'success',
        });
      } else {
        setSnackbarInfo({
          open: true,
          message: 'Please add a quantity before adding to cart.',
          severity: 'warning',
        });
      }
    } else {
      setSnackbarInfo({
        open: true,
        message: `You cannot add more than ${product.stock} ${
          product.stock === 1 ? 'item' : 'items'
        } to the cart.`,
        severity: 'warning',
      });
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbarInfo({ ...snackbarInfo, open: false });
  };

  const handleChange = (event, value) => {
    setPage(value);
  };

  useEffect(() => {
    fetchProducts().then((data) => {
      if (data) {
        setProducts(data);
      }
    });
  }, [page]);

  // Oldalváltásnál csak a jelenlegi oldalon lévő termékeket jelenítjük meg
  const displayedProducts = displayProducts(products, page, pageSize);

  const containerStyle = {
    backgroundImage: `url('https://images.unsplash.com/photo-1603912699214-92627f304eb6?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2825')`,
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    minHeight: '100vh',
  };

  return (
    <ThemeProvider theme={customTheme}>
      <CssBaseline />
      <main>
        <Box sx={containerStyle}>
          <Box
            sx={{
              position: 'fixed',
              top: '1em',
              left: '1em',
              zIndex: 2,
            }}
          >
            <Button
              onClick={handleSignOut}
              variant="contained"
              sx={{
                backgroundColor: '#FF5733',
                '&:hover': {
                  backgroundColor: '#FF8040',
                },
              }}
            >
              Sign out
            </Button>
          </Box>
          <Typography
            component="h1"
            variant="h2"
            color="#ffe0b2"
            sx={{
              color: '#ffe0b2',
              textAlign: 'center',
              position: 'absolute',
              top: '22%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              zIndex: 1,
              fontFamily: 'Roboto',
              fontSize: '16.5em',
              textShadow: '0px 4px 4px rgba(0, 0, 0, 1)',
              fontWeight: 'Medium',
              width: '100%',
            }}
          >
            Plantify
          </Typography>
          <Typography
            sx={{
              position: 'fixed',
              top: 0,
              right: 0,
              zIndex: 1,
              padding: '1em',
            }}
          >
            <Button
              color="primary"
              size="large"
              variant="contained"
              component={RouterLink}
              to="/cart"
              sx={{
                backgroundColor: '#FF5733',
                '&:hover': {
                  backgroundColor: '#FF8040',
                },
                marginRight: '10px',
              }}
            >
              <ShoppingCartIcon />
              <span style={{ marginLeft: '10px' }}>{totalQuantity}</span>
            </Button>
            <Button
              color="primary"
              size="large"
              variant="contained"
              onClick={handleNavigateToOrders}
              sx={{
                backgroundColor: '#FF5733',
                '&:hover': {
                  backgroundColor: '#FF8040',
                },
              }}
            >
              My orders
            </Button>
          </Typography>
        </Box>

        <Container
          sx={{
            py: 4, // Itt állítsd be a kívánt padding értéket
            mt: 4,
            background: '#ffe0b2', // Ezt az értéket adtuk hozzá
            borderRadius: '10px', // Kerekített sarkok
            boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.9)', // Homályosított árnyék
          }}
          maxWidth="md"
        >
          <Grid container spacing={7}>
            {displayedProducts.map((product, index) => {
              const { quantityColor, message } = getStockStyles(product);

              return (
                <Grid
                  item
                  key={`product_${product.productId}_${index}`}
                  xs={12}
                  sm={6}
                  md={4}
                >
                  <Card
                    sx={{
                      height: '100%',
                      display: 'flex',
                      flexDirection: 'column',
                      backgroundColor: '#ffcc80',
                      borderRadius: '10px',
                      boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.8)',
                    }}
                  >
                    <CardMedia
                      component="div"
                      sx={{
                        pt: '86.25%',
                      }}
                      image={product.image}
                    />
                    <CardContent sx={{ flexGrow: 1 }}>
                      <Typography gutterBottom variant="h5" component="h2">
                        {product.name}
                      </Typography>

                      <div style={{ marginBottom: '6px' }}></div>

                      <Typography paragraph>{product.description}</Typography>

                      <div style={{ marginBottom: '6px' }}></div>

                      <Typography>Price: {product.price}</Typography>

                      <div style={{ marginBottom: '6px' }}></div>

                      <Typography>Available: {product.stock}</Typography>
                    </CardContent>

                    <TextField
                      label="Quantity"
                      type="number"
                      value={productQuantities[product.productId] || 0}
                      onChange={(e) =>
                        handleQuantityChange(
                          product.productId,
                          parseInt(e.target.value, 10),
                        )
                      }
                      InputProps={{
                        sx: {
                          '& .MuiInput-input:focus': {
                            backgroundColor: '#FF5733', // Narancssárga háttérszín fókuszban
                            color: '#FFFFFF', // Fehér szöveg fókuszban
                            borderColor: '#FF5733 !important', // Narancssárga keret fókuszban
                            outline: 'none', // Eltávolítja a beépített fókusz keretet
                          },
                          color: quantityColor, // Mennyiség színének beállítása
                        },
                      }}
                    />

                    {message && (
                      <Typography
                        variant="body2"
                        color={quantityColor} // Üzenet színének beállítása
                        gutterBottom
                      >
                        {message}
                      </Typography>
                    )}

                    <CardActions>
                      <Button
                        size="small"
                        onClick={() => handleAddToCart(product)}
                        sx={{
                          backgroundColor: '#FF5733', // Saját háttérszín
                          color: '#FFFFFF', // Saját szövegszín
                          '&:hover': {
                            backgroundColor: '#FF8040', // Saját háttérszín egér fölé helyezéskor
                          },
                        }}
                      >
                        Add to Cart
                      </Button>
                    </CardActions>
                  </Card>
                </Grid>
              );
            })}
          </Grid>
          <PaginationControlled
            onChange={handleChange}
            totalProducts={products.length}
            pageSize={pageSize}
            page={page}
          />
        </Container>
      </main>

      <Box sx={{ bgcolor: '#ffe0b2', p: 6, mt: 4 }} component="footer">
        <Typography variant="h6" align="center" gutterBottom>
          Visit Our Store
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="text.secondary"
          component="p"
        >
          Come see our full selection of plants and accessories at our physical
          store location.
        </Typography>
        <Copyright />
      </Box>
      <Snackbar
        open={snackbarInfo.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbarInfo.severity}
          sx={{ width: '100%' }}
        >
          {snackbarInfo.message}
        </Alert>
      </Snackbar>
    </ThemeProvider>
  );
}
