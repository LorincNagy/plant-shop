import express from "express";
import {createProxyMiddleware} from "http-proxy-middleware";
import {fileURLToPath} from 'url';
import path from "path";
import process from ".eslintrc.cjs";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();

app.use(express.static(path.join(__dirname, 'build')));

app.use("/api", createProxyMiddleware({target: "http://backend:8080/"}));

app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, 'build', 'index.html'));
});

const PORT = process.env.PORT || 3000;
console.log('server started on port:', PORT);
app.listen(PORT);
