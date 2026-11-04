# Auditoría del Producto 2 sobre el ZIP recibido

## Estado recibido

El proyecto ya contenía una parte importante del Producto 2:

- Entidades `Libro`, `Usuario` y `Prestamo`.
- Repositorios JPA de las tres entidades.
- Controladores MVC.
- Vistas Thymeleaf para listados y formularios.
- CRUD de las tres entidades.
- Relación entre usuarios, libros y préstamos.
- Configuración de MySQL y Docker.
- Página principal y estilos Bootstrap.

## Problemas detectados

1. `pom.xml` con dependencias duplicadas de Validation y Data JPA.
2. `spring-boot-configuration-processor` configurado con un `scope` no válido.
3. Dependencia `spring-boot-docker-compose` provocando que Spring Boot iniciara y detuviera contenedores automáticamente durante el arranque.
4. MySQL configurado en el puerto 3307, ya ocupado en el equipo por otro proyecto.
5. Aplicación prevista en 8080, también ocupado por phpMyAdmin.
6. Uso de `mysql:latest`, que había descargado una versión distinta de la utilizada inicialmente y complicaba la autenticación.
7. Uso de `root` como usuario de aplicación para MySQL.
8. Archivos `.DS_Store` de macOS incluidos en el proyecto.
9. Archivo accidental llamado `3306` en la raíz.
10. `data.sql` y `DataLoader` duplicaban estrategias de carga de datos.
11. Uso de `java.util.Date` y `@Temporal`, que generaban avisos deprecados con la versión de Hibernate utilizada.
12. Los borrados se realizaban mediante GET y podían fallar por relaciones con préstamos.
13. Faltaba documentación clara del diagrama E/R y del mockup dentro del proyecto recibido.

## Correcciones aplicadas

- Limpieza de Maven y dependencias.
- MySQL fijado a 8.4 y usuario específico `biblio`.
- Puertos 3310 (MySQL) y 8090 (aplicación).
- Docker Compose con app + MySQL, healthcheck y volumen.
- Modelos actualizados a `LocalDate`.
- Validaciones de formularios.
- Eliminación por POST.
- Control de borrado cuando existen préstamos relacionados.
- Eliminación de archivos basura.
- Documentación E/R y mockup añadida.
- Test de carga de contexto preparado con H2.

## Resultado respecto al enunciado

| Requisito P2 | Estado final |
|---|---|
| Diagrama E/R con al menos 3 entidades | Cumplido, documentado en `PRODUCTO2.md` |
| Mockup | Cumplido, documentado en `PRODUCTO2.md` |
| Base de datos y tablas | Cumplido mediante JPA + MySQL |
| `application.properties` | Cumplido y corregido |
| Model | Cumplido |
| Repository | Cumplido |
| Controller | Cumplido |
| Thymeleaf | Cumplido |
| CRUD de al menos una entidad | Cumplido en las tres entidades |
| Docker | Cumplido y corregido |
