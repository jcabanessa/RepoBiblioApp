# Cambios realizados sobre el ZIP recibido

## Correcciones de Producto 2
- Eliminadas dependencias Maven duplicadas y configuración inválida de `annotationProcessor`.
- Eliminado el soporte automático `spring-boot-docker-compose` que estaba iniciando/deteniendo MySQL de forma conflictiva.
- Docker Compose reconstruido con MySQL 8.4, usuario propio `biblio`, healthcheck y volumen persistente.
- Puertos cambiados a MySQL 3310 y aplicación 8090 para evitar colisiones con los contenedores existentes del equipo local.
- Modelos modernizados a `LocalDate` y con validaciones.
- CRUD movido a rutas coherentes y borrados mediante POST.
- Gestión de errores al borrar libros/usuarios con préstamos asociados.
- Eliminados `.DS_Store`, archivo accidental `3306` y `data.sql` redundante.
- Añadido test de contexto con H2 para no depender de MySQL en la fase de tests.

## Implementación de Producto 3
- Añadido Spring Security.
- Login y registro propios.
- BCrypt para contraseñas.
- Roles ADMIN y USER.
- Rutas `/admin/**` protegidas para altas, ediciones y bajas.
- Rutas `/user/**` para el área del usuario.
- Consulta de préstamos del usuario autenticado.
- Usuario ADMIN y USER de demostración.
- Documentación E/R, mockup, rutas y pasos de ejecución.
