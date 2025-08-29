from pydantic import BaseModel, Field, constr, conint
from typing import Optional

class Producto(BaseModel):
    name: constr(strip_whitespace=True, min_length=1, max_length=100) = Field(..., description="Nombre del producto")
    description: Optional[constr(strip_whitespace=True, max_length=300)] = Field(None, description="Descripción opcional")
    price: conint(ge=100, le=500) = Field(..., description="Precio entre 100 y 500")
