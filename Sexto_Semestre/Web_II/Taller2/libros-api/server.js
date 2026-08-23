const express = require("express");
const cors = require("cors");
const librosRouter = require("./routes/libros");

const app = express();
const PORT = process.env.PORT || 3000;

const FRONTEND_ORIGIN = process.env.FRONTEND_ORIGIN || "http://localhost:5173";

app.use(cors({ origin: FRONTEND_ORIGIN }));
app.use(express.json());
app.use("/api/libros", librosRouter);

app.listen(PORT, () => {
  console.log(`libros-api escuchando en http://localhost:${PORT}`);
});
