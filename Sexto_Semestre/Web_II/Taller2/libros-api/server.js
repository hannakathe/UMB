const express = require("express");
const librosRouter = require("./routes/libros");

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use("/api/libros", librosRouter);

app.listen(PORT, () => {
  console.log(`libros-api escuchando en http://localhost:${PORT}`);
});
