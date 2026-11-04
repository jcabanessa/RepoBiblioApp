# Guion para el vídeo · Producto 3

Duración orientativa: 5-8 minutos.

## 1. Presentación

> En este Producto 3 he ampliado la aplicación de biblioteca del Producto 2 incorporando gestión de usuarios y seguridad mediante Spring Security. La aplicación utiliza Java 21, Spring Boot, Spring Data JPA, Thymeleaf, MySQL y Docker.

## 2. Mostrar Docker

En una terminal:

```cmd
docker compose ps
```

Explicar que:

- MySQL está aislado en el puerto 3310.
- La aplicación se expone en 8090.
- Docker Compose espera a que MySQL esté healthy antes de iniciar la aplicación.

## 3. Mostrar el modelo Usuario

En IntelliJ abrir `Usuario.java` y enseñar:

- `email` único.
- `password`.
- `rol`.
- relación `@OneToMany` con préstamos.

Explicar que la contraseña no se guarda en texto plano, sino codificada con BCrypt.

## 4. Mostrar SecurityConfig

Enseñar:

- rutas públicas `/login` y `/register`.
- `/admin/**` con rol ADMIN.
- `/user/**` para usuarios autenticados.
- login personalizado.
- logout.

## 5. Probar registro

Abrir `http://localhost:8090/register`.

- Crear una cuenta nueva.
- Explicar que recibe automáticamente el rol USER.
- Iniciar sesión con esa cuenta.

## 6. Probar rol USER

- Abrir el catálogo de libros.
- Abrir `Mi área`.
- Mostrar `/user/prestamos`.
- Intentar abrir manualmente `http://localhost:8090/usuarios` o una ruta `/admin/...` y mostrar que el acceso se deniega.

## 7. Probar rol ADMIN

Cerrar sesión e iniciar con:

- `admin@biblioteca.local`
- `Admin123!`

Mostrar:

- listado de usuarios.
- creación o edición de un libro.
- creación o edición de un préstamo.
- que las rutas de modificación se encuentran bajo `/admin/**`.

## 8. Base de datos

Opcionalmente entrar a MySQL o utilizar el IDE para mostrar las tablas:

- `usuarios`
- `libros`
- `prestamos`

En `usuarios`, mostrar que existe la columna `rol` y que la contraseña aparece cifrada.

## 9. Cierre

> Con esto se cumplen los objetivos del Producto 3: la aplicación parte del MVC del Producto 2, incorpora usuarios persistidos, login y registro con Spring Security, roles diferenciados y zonas protegidas para administrador y usuario.
