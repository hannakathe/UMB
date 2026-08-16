const API_KEY = process.env.API_KEY || "clave-secreta-123";

function auth(req, res, next) {
  const apiKey = req.header("x-api-key");

  if (!apiKey || apiKey !== API_KEY) {
    return res.status(401).json({ error: "No autorizado" });
  }

  next();
}

module.exports = auth;
