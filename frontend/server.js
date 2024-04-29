// server.js
import express from "express";
import { fileURLToPath } from "url";
import path from "path";
import { createProxyMiddleware } from "http-proxy-middleware";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();

// Szolgáld ki a statikus fájlokat a frontend/dist mappából
app.use(express.static(path.join(__dirname, "dist")));

// API proxy konfiguráció, ha szükséges
app.use(
  "/api",
  createProxyMiddleware({ target: "http://backend:8080/", changeOrigin: true })
);

// Minden egyéb útvonalra küldd vissza az index.html-t
app.get("*", (req, res) => {
  res.sendFile(path.resolve(__dirname, "dist", "index.html"));
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
