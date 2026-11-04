# Memoria de trabajo · Producto 3 FP065

## 1. Trabajo realizado

Se ha ampliado la aplicación MVC de gestión de biblioteca desarrollada en el Producto 2 para incorporar autenticación, registro y autorización por roles mediante Spring Security.

Los principales cambios realizados han sido:

- Incorporación de la dependencia Spring Security al proyecto Maven.
- Ampliación de la entidad `Usuario` con contraseña cifrada, rol y email único.
- Creación de los roles `ADMIN` y `USER`.
- Implementación de un `UserDetailsService` que obtiene las cuentas desde MySQL mediante `UsuarioRepository`.
- Uso de BCrypt para almacenar las contraseñas de forma no reversible.
- Creación de formulario de login y formulario de registro.
- Registro automático de nuevas cuentas con rol `USER`.
- Configuración de autorización de rutas:
  - `/admin/**`: acceso exclusivo ADMIN.
  - `/user/**`: acceso para usuarios autenticados.
- Traslado de las operaciones de creación, modificación y borrado de libros, usuarios y préstamos a rutas `/admin/**`.
- Creación de un área privada en `/user`.
- Creación de `/user/prestamos`, que muestra únicamente los préstamos asociados al usuario autenticado.
- Actualización de las vistas Thymeleaf para mostrar opciones diferentes según el estado de autenticación y el rol.
- Revisión del entorno Docker para evitar conflictos de puertos con otros proyectos y utilizar un usuario de base de datos específico de la aplicación.
- Incorporación de un healthcheck para MySQL y persistencia mediante volumen Docker.

## 2. Dificultades encontradas

La principal dificultad encontrada fue la configuración inicial del entorno Docker. En el equipo de desarrollo ya existían otros contenedores MySQL y phpMyAdmin utilizando los puertos 3307 y 8080. Esto provocaba que la aplicación se conectara a un servidor MySQL diferente al esperado o que no pudiera publicar los puertos del contenedor.

Para resolverlo se separó completamente el entorno de BiblioApp:

- MySQL se publica en el puerto 3310.
- La aplicación se publica en el puerto 8090.
- Se utiliza un usuario MySQL propio (`biblio`) en lugar del usuario `root`.
- Se eliminó la dependencia de gestión automática de Docker Compose desde Spring Boot y el ciclo de vida se gestiona directamente con `docker compose`.

También se revisó el `pom.xml`, ya que contenía dependencias duplicadas de JPA y Validation y una configuración de `annotationProcessor` no válida.

## 3. Puntos no realizados

El desarrollo de código y configuración se ha preparado para cumplir los requisitos del Producto 3. Antes de la entrega definitiva debe realizarse la prueba funcional completa en el equipo de entrega y grabarse el vídeo solicitado por el aula.

## 4. Propuestas de mejora

Como mejoras futuras se proponen:

- Incorporar recuperación de contraseña por correo electrónico.
- Añadir paginación y búsqueda en los listados.
- Añadir tests de integración específicos para las reglas de autorización.
- Añadir una pantalla para que el usuario pueda actualizar su perfil.
- Incorporar auditoría de operaciones administrativas.
- Sustituir `ddl-auto=update` por una herramienta de migraciones como Flyway o Liquibase en un entorno de producción.

## 5. Autoevaluación

La solución cumple los objetivos principales del producto: gestiona usuarios persistidos en base de datos, implementa Spring Security, incorpora login y registro, cifra las contraseñas, diferencia permisos por roles y dispone de zonas `/admin/**` y `/user/**`. Además, mantiene la relación entre usuarios y préstamos y la utiliza para limitar la información mostrada en el área personal.

La estructura se ha intentado mantener sencilla y comprensible, utilizando separación entre modelos, repositorios, DTO, controladores, servicio de seguridad y vistas.

## 6. Webgrafía

- Spring Boot Reference Documentation — Spring Security.
- Spring Security Reference — Form Login.
- Spring Security Reference — Authorize HttpServletRequests.
- Spring Security Reference — Username/Password Authentication.
- Spring Data JPA Reference Documentation.
- Thymeleaf Documentation.
- MySQL Docker Image Documentation.
