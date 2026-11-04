# Pasos para aplicar esta versión en tu repositorio local

Tu repositorio original estaba en medio de un merge. Para conservar el historial Git, **no sustituyas la carpeta `.git`**. El ZIP corregido no la incluye.

## 1. Haz una copia de seguridad

Copia tu carpeta actual:

`C:\Users\Cinth\Documents\UOC-Java-Backend\RepoBiblioApp`

por ejemplo a:

`C:\Users\Cinth\Documents\UOC-Java-Backend\RepoBiblioApp_backup`

## 2. Extrae el ZIP corregido

Extrae el contenido de `RepoBiblioApp_P2_P3_corregido.zip` sobre:

`C:\Users\Cinth\Documents\UOC-Java-Backend\RepoBiblioApp`

Acepta reemplazar los archivos existentes.

## 3. Elimina restos que pertenecían a la versión anterior

Desde CMD, dentro de la carpeta del proyecto:

```cmd
if exist 3306 del /f /q 3306
if exist src\main\resources\data.sql del /f /q src\main\resources\data.sql
```

Los `.DS_Store` ya están ignorados por Git; si todavía aparecen como cambios, se eliminarán al hacer `git add -A` después de haberlos borrado físicamente.

## 4. Reinicia únicamente el entorno de BiblioApp

```cmd
docker compose down -v --remove-orphans
docker compose up -d mysql
```

Esta versión utiliza:

- MySQL: `localhost:3310`
- Aplicación: `http://localhost:8090`

No es necesario detener los contenedores de ReparaYa que usan 3307 y 8080.

## 5. Compila y ejecuta

```cmd
mvnw.cmd clean test
mvnw.cmd spring-boot:run
```

Cuando aparezca `Started BiblioAppApplication`, abre:

`http://localhost:8090`

## 6. Prueba funcional mínima

### ADMIN

- Email: `admin@biblioteca.local`
- Contraseña: `Admin123!`

Comprobar:

1. Login correcto.
2. `/usuarios` accesible.
3. Crear/editar un libro.
4. Crear/editar un préstamo.
5. Logout.

### USER

- Email: `usuario@biblioteca.local`
- Contraseña: `Usuario123!`

Comprobar:

1. Login correcto.
2. Catálogo `/libros` accesible.
3. `/user` accesible.
4. `/user/prestamos` muestra sólo sus préstamos.
5. `/usuarios` o `/admin/...` devuelve acceso denegado.

### Registro

1. Abrir `/register`.
2. Crear una cuenta nueva.
3. Iniciar sesión con ella.
4. Confirmar que tiene permisos USER y no ADMIN.

## 7. Cierra el merge y guarda los cambios

Cuando las pruebas sean correctas:

```cmd
git add -A
git status
git commit -m "Integrar Producto 2 e implementar Producto 3"
git push origin Cinthia
```

El `git commit` cerrará el merge que estaba pendiente en el ZIP original.
