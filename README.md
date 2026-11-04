# BiblioApp · Productos 2 y 3 (FP065)

Aplicación web de gestión de biblioteca realizada con Java 21, Spring Boot 4.1.1, Spring MVC, Spring Data JPA, Thymeleaf, Spring Security, MySQL y Docker Compose.

## Funcionalidades

### Producto 2
- Modelo MVC para `Libro`, `Usuario` y `Prestamo`.
- Persistencia mediante Spring Data JPA y MySQL.
- Relación Usuario 1:N Préstamo y Libro 1:N Préstamo.
- CRUD administrativo de libros, usuarios y préstamos.
- Vistas Thymeleaf y diseño responsive con Bootstrap.
- Entorno reproducible con Docker.

### Producto 3
- Registro e inicio de sesión con Spring Security.
- Contraseñas cifradas con BCrypt.
- Roles `ADMIN` y `USER`.
- `/admin/**`: altas, modificaciones y bajas, sólo para ADMIN.
- `/user/**`: área privada y consulta de préstamos del usuario autenticado.
- Registro público: todas las cuentas nuevas reciben el rol `USER`.
- Relación entre la cuenta autenticada y sus préstamos de biblioteca.

## Ejecución recomendada: Docker Compose

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Abrir:

- Aplicación: `http://localhost:8090`
- MySQL del proyecto: `localhost:3310`

Se han elegido los puertos 8090 y 3310 para no interferir con otros proyectos locales que usan 8080 y 3307.

Para detener:

```bash
docker compose down
```

Para reiniciar también la base de datos desde cero:

```bash
docker compose down -v
docker compose up --build
```

## Ejecución desde IntelliJ / Maven

1. Arrancar sólo MySQL:

```bash
docker compose up -d mysql
```

2. Ejecutar:

```bash
./mvnw spring-boot:run
```

En Windows:

```cmd
mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en `http://localhost:8090`.

## Usuarios de demostración

| Rol | Email | Contraseña |
|---|---|---|
| ADMIN | `admin@biblioteca.local` | `Admin123!` |
| USER | `usuario@biblioteca.local` | `Usuario123!` |

Estas credenciales son exclusivamente de demostración para el proyecto académico.

## Rutas principales

| Ruta | Acceso | Función |
|---|---|---|
| `/` | Público | Inicio |
| `/login` | Público | Login |
| `/register` | Público | Registro |
| `/libros` | Autenticado | Consulta del catálogo |
| `/user` | USER / ADMIN | Área privada |
| `/user/prestamos` | USER / ADMIN | Préstamos del usuario autenticado |
| `/usuarios` | ADMIN | Listado de usuarios |
| `/prestamos` | ADMIN | Listado completo de préstamos |
| `/admin/**` | ADMIN | Crear, modificar y borrar |

Consulta `docs/PRODUCTO2.md` y `docs/PRODUCTO3.md` para los diagramas y la correspondencia con los enunciados.
