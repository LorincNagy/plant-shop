import React, { useEffect, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import PaginationControlled from "./PaginationControlled.jsx";
import { useCart } from "./CartProvider";
import { useNavigate } from "react-router-dom";

function Copyright() {
  return (
    <Typography variant="body2" color="text.secondary" align="center">
      {"Copyright © "}
      <Link color="inherit" href="https://mui.com/">
        Plantify
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const customTheme = createTheme({
  palette: {
    background: {
      default: "#f9fbe7",
    },
  },
  components: {
    MuiTextField: {
      styleOverrides: {
        root: {
          "& .focused": {
            color: "#FF5733 !important",
            borderColor: "#FF5733 !important",
          },
        },
      },
    },
  },
});

const fetchProducts = async () => {
  const token = localStorage.getItem("token");
  if (!token) {
    console.error("Nincs token a localStorage-ban.");
    return null;
  }

  try {
    const response = await fetch("/api/products", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Hiba történt a védett erőforrások lekérése során.");
    }

    const products = await response.json();

    // Inicializáljuk a quantity mezőket minden termékhez
    const productsWithQuantity = products.map((product) => ({
      ...product,
      quantity: 0, // Itt inicializálhatod az alapértelmezett értéket
    }));

    return productsWithQuantity;
  } catch (error) {
    console.error("Hiba történt a termékek lekérése során:", error);
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
  const { cartitems, setCartItems, sendCartToBackend,handleSignOut } = useCart();
  const [productQuantities, setProductQuantities] = useState({});
  const navigate = useNavigate();

  const handleNavigateToOrders = () => {
    navigate("/orders");
  };

  const handleQuantityChange = (productId, newValue) => {
    // Ellenőrizzük, hogy az új érték ne legyen negatív
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
    const existingCartItemIndex = cartitems.findIndex(
      (item) => item.productId === product.productId
    );

    let updatedCartItems = [];

    if (existingCartItemIndex !== -1) {
      updatedCartItems = [...cartitems];
      updatedCartItems[existingCartItemIndex] = {
        ...cartitems[existingCartItemIndex],
        quantity:
          cartitems[existingCartItemIndex].quantity +
          productQuantities[product.productId],
      };
    } else {
      updatedCartItems = [
        ...cartitems,
        {
          ...product,
          quantity: productQuantities[product.productId],
        },
      ];
    }

    setCartItems(updatedCartItems);
    console.log();

    console.log("Termék hozzáadása a kosárhoz:", product);
    // Elküldjük a kosár tartalmát a backendnek
    sendCartToBackend(updatedCartItems);
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

  return (
    <ThemeProvider theme={customTheme}>
      <CssBaseline />
      <main>
        <Box
          sx={{
            backgroundImage: `url('https://images.unsplash.com/photo-1603912699214-92627f304eb6?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2825')`,
            backgroundSize: "cover",
            backgroundRepeat: "no-repeat",
            minHeight: "100vh",
          }}
        >
          <Typography
            component="h1"
            variant="h2"
            color="#ffe0b2"
            sx={{
              color: "#ffe0b2",
              textAlign: "center",
              position: "absolute",
              top: "22%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              zIndex: 1,
              fontFamily: "Roboto",
              fontSize: "16.5em",
              textShadow: "0px 4px 4px rgba(0, 0, 0, 1)",
              fontWeight: "Medium",
              width: "100%",
            }}
          >
            Plantify
          </Typography>
          <Typography
            sx={{
              position: "fixed",
              top: 0,
              right: 0,
              zIndex: 1,
              padding: "1em",
            }}
          >
            <Button
              color="primary"
              size="large"
              variant="contained"
              component={RouterLink}
              to="/cart"
              sx={{
                backgroundColor: "#FF5733",
                "&:hover": {
                  backgroundColor: "#FF8040",
                },
                marginRight: "10px",
              }}
            >
              <ShoppingCartIcon />
              <span style={{ marginLeft: "10px" }}>{totalQuantity}</span>
            </Button>
            <Button
              color="primary"
              size="large"
              variant="contained"
              onClick={handleNavigateToOrders}
              sx={{
                backgroundColor: "#FF5733",
                "&:hover": {
                  backgroundColor: "#FF8040",
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
            background: "#ffe0b2", // Ezt az értéket adtuk hozzá
            borderRadius: "10px", // Kerekített sarkok
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.9)", // Homályosított árnyék
          }}
          maxWidth="md"
        >
          <Grid container spacing={7}>
            {displayedProducts.map((product, index) => (
              <Grid
                item
                key={`product_${product.id}_${index}`} // Concatenate index to product.id for unique key
                xs={12}
                sm={6}
                md={4}
              >
                <Card
                  sx={{
                    height: "100%",
                    display: "flex",
                    flexDirection: "column",
                    backgroundColor: "#ffcc80",
                    borderRadius: "10px",
                    boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.8)",
                  }}
                >
                  <CardMedia
                    component="div"
                    sx={{
                      pt: "86.25%",
                    }}
                    image={product.image}
                  />
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography gutterBottom variant="h5" component="h2">
                      {product.name}
                    </Typography>

                    <div style={{ marginBottom: "6px" }}></div>

                    <Typography paragraph>{product.description}</Typography>

                    <div style={{ marginBottom: "6px" }}></div>

                    <Typography>Price: {product.price}</Typography>

                    <div style={{ marginBottom: "6px" }}></div>

                    <Typography>Available: {product.stock}</Typography>
                  </CardContent>

                  <TextField
                    label="Quantity"
                    type="number"
                    value={productQuantities[product.productId] || 0}
                    onChange={(e) =>
                      handleQuantityChange(
                        product.productId,
                        parseInt(e.target.value, 10)
                      )
                    }
                    InputProps={{
                      sx: {
                        "& .MuiInput-input:focus": {
                          backgroundColor: "#FF5733", // Narancssárga háttérszín fókuszban
                          color: "#FFFFFF", // Fehér szöveg fókuszban
                          borderColor: "#FF5733 !important", // Narancssárga keret fókuszban
                          outline: "none", // Eltávolítja a beépített fókusz keretet
                        },
                      },
                    }}
                  />

                  <CardActions>
                    <Button
                      size="small"
                      onClick={() => handleAddToCart(product)}
                      sx={{
                        backgroundColor: "#FF5733", // Saját háttérszín
                        color: "#FFFFFF", // Saját szövegszín
                        "&:hover": {
                          backgroundColor: "#FF8040", // Saját háttérszín egér fölé helyezéskor
                        },
                      }}
                    >
                      Add to Cart
                    </Button>
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>
          <PaginationControlled
            onChange={handleChange}
            totalProducts={products.length}
            pageSize={pageSize}
            page={page}
          />
        </Container>
      </main>

      <Box sx={{ bgcolor: "#ffe0b2", p: 6, mt: 4 }} component="footer">
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
    </ThemeProvider>
  );
}
