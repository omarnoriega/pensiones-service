# 🏛️ Pensiones Service

> **Platform Engineering Demo** — Microservicio Spring Boot con datos reales de pensiones de Colombia.

Consulta estadísticas de afiliación al **Sistema General de Pensiones de Colombia** por entidad administradora.

---

## 🚀 Inicio rápido

```bash
# Compilar y ejecutar
mvn spring-boot:run

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

No requiere base de datos ni configuración adicional. Los datos se cargan desde el CSV incluido al arrancar.

---

## 📡 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/pensiones` | Todas las entidades |
| `GET` | `/api/v1/pensiones/nombres` | Solo los nombres disponibles |
| `GET` | `/api/v1/pensiones/buscar?nombre=X` | Búsqueda por nombre (query param) |
| `GET` | `/api/v1/pensiones/{nombre}` | Búsqueda por nombre (path variable) |
| `GET` | `/actuator/health` | Health check |

---

## 💡 Ejemplos

```bash
# Todas las entidades
curl http://localhost:8080/api/v1/pensiones

# Ver nombres disponibles
curl http://localhost:8080/api/v1/pensiones/nombres

# Buscar COLPENSIONES
curl "http://localhost:8080/api/v1/pensiones/buscar?nombre=COLPENSIONES"

# Buscar Porvenir (por path)
curl http://localhost:8080/api/v1/pensiones/Porvenir

# Búsqueda parcial — retorna la primera coincidencia que contenga "total"
curl "http://localhost:8080/api/v1/pensiones/buscar?nombre=total%20afiliados"
```

### Respuesta de ejemplo

```json
{
  "success": true,
  "message": "Entidad encontrada",
  "data": {
    "entidadAdministradora": "COLPENSIONES",
    "cotizantes": 3045937,
    "noCotizantes": 4036279,
    "activos": 3666215,
    "inactivos": 3416001,
    "total": 7082216,
    "cotizantesFormateado": "3,045,937",
    "noCotizantesFormateado": "4,036,279",
    "activosFormateado": "3,666,215",
    "inactivosFormateado": "3,416,001",
    "totalFormateado": "7,082,216"
  },
  "timestamp": "2025-01-15T10:30:00Z"
}
```

---

## 🧪 Tests

```bash
mvn test
```

---

## 🐳 Docker

```bash
docker build -t pensiones-service .
docker run -p 8080:8080 pensiones-service
```

---

## 📊 Entidades disponibles

| Entidad | Tipo |
|---------|------|
| CAXDAC | Prima Media |
| FONPRECON - Ley 4/92 | Prima Media |
| FONPRECON - Ley 100/93 | Prima Media |
| FONPRECON Total | Subtotal |
| Pensiones de Antioquia | Prima Media |
| COLPENSIONES | Prima Media |
| Total Prima Media | Subtotal |
| Protección | Ahorro Individual |
| Porvenir | Ahorro Individual |
| Skandia | Ahorro Individual |
| Colfondos | Ahorro Individual |
| Skandia alternativo | Ahorro Individual |
| Total Ahorro Individual | Subtotal |
| **Total Afiliados Al Sistema** | **TOTAL** |
