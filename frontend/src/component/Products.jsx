import React, { useEffect, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
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

const defaultTheme = createTheme();

const customTheme = createTheme({
  palette: {
    background: {
      default: "#f9fbe7",
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

    return products;
  } catch (error) {
    console.error("Hiba történt a termékek lekérése során:", error);
    return null;
  }
};

function displayProducts(products, page, pageSize) {
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  return products.slice(startIndex, endIndex).map((product) => ({
    ...product,
    id: product.productId,
    quantity: 0,
  }));
}

const pageSize = 9;

export default function Products() {
  const [products, setProducts] = useState([]);
  console.log(products);
  const [page, setPage] = useState(1);
  const { cartitems, setCartItems } = useCart();

  const handleQuantityChange = (id, newValue) => {
    const updatedProducts = products.map((product) =>
      product.id === id ? { ...product, quantity: newValue } : product
    );
    setProducts(updatedProducts);
  };

  const handleAddToCart = (product) => {
    if (product.quantity > 0) {
      // Hozzáadjuk a terméket annyi példányszámban, amennyit a 'quantity' mezőben megadtak
      const updatedCartItems = [...cartitems];
      for (let i = 0; i < product.quantity; i++) {
        updatedCartItems.push(product);
      }
      // Frissítjük a 'cartitems' állapotot
      setCartItems(updatedCartItems);
    }
  };

  const handleChange = (event, value) => {
    setPage(value);
  };

  useEffect(() => {
    fetchProducts().then((data) => {
      if (data) {
        setProducts(displayProducts(data, page, pageSize));
      }
    });
  }, []);

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
            {" "}
            <Button
              color="primary"
              size="large"
              variant="contained"
              component={RouterLink}
              to="/cart"
              sx={{
                backgroundColor: "#FF5733", // Itt állítsd be a kívánt háttérszínt
                "&:hover": {
                  backgroundColor: "#FF8040", // Itt állítsd be a hover állapot háttérszínét
                },
              }}
            >
              Cart
            </Button>
          </Typography>
        </Box>

        {/* <Container maxWidth="sm">
          <Typography
            component="h1"
            variant="h2"
            align="center"
            color="#616161"
            gutterBottom
            sx={{
              mt: 8,
              fontFamily: "Murray Text",
              fontSize: "5em",
              textShadow: "0px 4px 4px rgba(0, 0, 0, 3)",
              fontWeight: "Medium",
              width: "100%",
            }}
          >
            Choose Your Plant
          </Typography>
        </Container> */}

        <Container
          sx={{
            py: 4, // Itt állítsd be a kívánt padding értéket
            mt: 4,
            background: "#ffe0b2",
            borderRadius: "10px", // Kerekített sarkok
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.9)", // Homályosított árnyék
          }}
          maxWidth="md"
        >
          <Grid container spacing={7}>
            {products.map((product) => (
              <Grid
                item
                key={`product_${product.id}`} // Itt hozzáadtuk az egyedi kulcsot
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
                    <Typography>{product.description}</Typography>
                  </CardContent>
                  <TextField
                    label="Quantity"
                    type="number"
                    value={product.quantity}
                    onChange={(e) =>
                      handleQuantityChange(
                        product.id,
                        parseInt(e.target.value, 10)
                      )
                    }
                    inputProps={{
                      min: 0,
                      step: 1,
                      inputMode: "numeric",
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
