# 🛠️ CarMotors - Sistema para Taller Automotriz

CarMotors es un sistema integral de gestión para talleres automotrices, desarrollado en Java con arquitectura **MVC** y utilizando **Maven** como gestor de dependencias. Este sistema busca optimizar las operaciones diarias del taller, incluyendo la gestión de inventarios, mantenimiento de vehículos, atención al cliente, facturación electrónica y manejo de proveedores.

---

## 📌 Características del Sistema

- 🧾 Gestión de clientes y vehículos
- 🛠️ Registro y seguimiento de órdenes de servicio (mantenimiento preventivo y correctivo)
- 🧃 Inventario de repuestos con control de stock y trazabilidad
- 🧑‍🔧 Gestión de técnicos, tareas y productividad
- 💳 Generación de facturas electrónicas en imagen (PDF/PNG)
- 📦 Control de proveedores y órdenes de compra
- 📊 Reportes detallados por módulo (inventario, servicios, facturación)
- ✅ Cumple con la normativa DIAN (Colombia) para facturación electrónica

---

## 🚀 Requisitos del Proyecto

- Java 17+
- Apache Maven 3.6+
- MySQL Server 8.0+
- IDE recomendada: IntelliJ IDEA o NetBeans

---

## ⚙️ Configuración del Entorno

1. **Clona el repositorio**

```bash
git clone https://github.com/tu-usuario/CarMotors.git
cd CarMotors
```


2. **Crea el archivo `dbconfig.properties`** en la raíz del proyecto con las credenciales de tu base de datos:

```properties
# dbconfig.properties
app.db=carmotorsdb
app.user=carmotors_user
app.password=tu_contraseña_segura
```

> ⚠️ Este archivo está ignorado en `.gitignore` para evitar exposición de credenciales.

3. **Importa el esquema de base de datos**

Puedes importar manualmente con MySQL Workbench o desde CLI:

```bash
mysql -u root -p < sql/schema.sql
```

Opcionalmente, importa datos de prueba:

```bash
mysql -u root -p carmotorsdb < sql/data.sql
```

---

## ▶️ Cómo Ejecutar el Proyecto

1. Abre el proyecto en tu IDE.
2. Ejecuta la clase `CarMotors.java`, que inicializa la conexión, crea usuarios si es necesario y lanza el menú principal.

---

## 🗃️ Estructura del Proyecto

```
CarMotors/
├── pom.xml
├── .gitignore
├── dbconfig.properties              # ⚠️ NO subir este archivo
├── sql/
│   ├── schema.sql
│   ├── data.sql
│   └── functions.sql
└── src/
└── main/
├── java/
│   └── com/
│       └── carmotors/
│           ├── CarMotors.java
│           ├── controller/
│           ├── model/
│           │   ├── entities/
│           │   └── dao/
│           ├── view/
│           └── utils/
└── resources/
```

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor abre un issue o un pull request si deseas mejorar el sistema, añadir nuevas funcionalidades o corregir errores.

---

## 📄 Licencia

Este proyecto es de uso académico y libre distribución con fines educativos.

---

## 👨‍💻 Autor

Desarrollado por el equipo de desarrollo para el Taller Automotriz **"CarMotors"**.
