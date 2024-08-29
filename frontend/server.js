import express from "express";
import { fileURLToPath } from "url";
import path from "path";
import { createProxyMiddleware } from "http-proxy-middleware";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();

// Szolgáld ki a statikus fájlokat a frontend/dist mappából
app.use(express.static(path.join(__dirname, "dist")));

// API proxy konfiguráció, dinamikus target beállítással
// eslint-disable-next-line no-undef
const isDocker = process.env.DOCKER === "true"; // Ellenőrizzük, hogy Dockerben fut-e

app.use(
  "/api",
  createProxyMiddleware({
    target: isDocker
      ? "http://threetree-backend:8080"
      : "http://localhost:8080",
    changeOrigin: true,
    timeout: 5000, // 5 másodperc
    proxyTimeout: 5000, // 5 másodperc a proxy-nak
  })
);

// Minden egyéb útvonalra küldd vissza az index.html-t
app.get("*", (req, res) => {
  res.sendFile(path.resolve(__dirname, "dist", "index.html"));
});

// eslint-disable-next-line no-undef
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
