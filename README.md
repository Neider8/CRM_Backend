Documentación Técnica – CRM_TECH360
Descripción General
CRM_TECH360 es una aplicación backend desarrollada con Spring Boot (JDK 21) y Maven, que proporciona una API RESTful para la gestión de clientes. Utiliza PostgreSQL como sistema de gestión de bases de datos y cuenta con documentación interactiva mediante Swagger.

Tecnologías Utilizadas
Java 21

Spring Boot

Maven

PostgreSQL

Swagger para documentación de la API

Requisitos Previos
Java 21 instalado

Maven instalado

PostgreSQL en funcionamiento

Instalación y Configuración
Clonar el repositorio:

git clone https://github.com/Neider8/CRM_Backend.git
cd CRM_Backend
Configurar la base de datos:

Crear una base de datos en PostgreSQL, por ejemplo, crm_tech360.

Actualizar el archivo application.properties con las credenciales y URL de conexión adecuadas.

Construir el proyecto:
./mvnw clean install
Ejecución de la Aplicación
Iniciar la aplicación con el siguiente comando:

./mvnw spring-boot:run
La API estará disponible en: http://localhost:8080/

Documentación de la API
Swagger proporciona una interfaz interactiva para explorar y probar los endpoints de la API. Accede a la documentación en:


http://localhost:8080/swagger-ui.html
🗂️ Estructura del Proyecto
src/main/java: Contiene el código fuente organizado en paquetes:

controller: Controladores REST que manejan las solicitudes HTTP.

service: Lógica de negocio y servicios.

repository: Interfaces para el acceso a datos utilizando Spring Data JPA.

model: Clases de entidad que representan las tablas de la base de datos.

src/main/resources: Archivos de configuración y recursos estáticos:

application.properties: Configuraciones de la aplicación, incluyendo la conexión a la base de datos.

Scripts SQL
Incluye scripts SQL para la creación y población de la base de datos. Asegúrate de ejecutarlos en tu instancia de PostgreSQL antes de iniciar la aplicación.

Autores
Neider, Juan Felipe, Jhosep, daniel Y Alejo – Desarrolladores principal

