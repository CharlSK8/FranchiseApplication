# API de Gestión de Franquicias

Este proyecto es una API RESTful desarrollada con Spring WebFlux y MongoDB para la gestión de franquicias, sucursales y productos. Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre estos recursos, así como otras funcionalidades específicas.

## Funcionalidades Principales

* **Gestión de Franquicias:**
    * Crear, actualizar y eliminar franquicias.
    * Obtener información de una franquicia por su ID.
* **Gestión de Sucursales:**
    * Crear, actualizar y eliminar sucursales.
    * Agregar y eliminar productos de una sucursal.
    * Actualizar el stock de un producto en una sucursal.
    * Obtener el producto con mayor stock por sucursal en una franquicia.
* **Gestión de Productos:**
    * Actualizar el nombre de un producto.
* **Validaciones:**
    * Validación de la existencia de franquicias, sucursales y productos.
    * Validación de nombres únicos para franquicias y sucursales.
    * Validación de datos de entrada en las solicitudes HTTP.
* **Manejo de Excepciones:**
    * Manejo de excepciones personalizadas para errores específicos (por ejemplo, producto no encontrado, nombre de sucursal duplicado).
    * Respuestas HTTP con códigos de estado y mensajes descriptivos.
* **Contenedorización:**
    * Creación de imagen y contenedor Docker para la aplicación.

## Tecnologías Utilizadas

* Spring WebFlux
* Spring Data MongoDB
* MongoDB
* Java 17
* Docker

## Requisitos

* Java 17 o superior
* MongoDB instalado y en ejecución
* Docker (opcional, para la contenedorización)

## Configuración

1.  Clona el repositorio:

    ```bash
    git clone https://github.com/CharlSK8/FranchiseApplication.git
    ```

2.  Configura la conexión a MongoDB en el archivo `application.properties`:

    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/nombre_de_la_base_de_datos
    ```

3.  Construye la aplicación con Gradle:

    ```bash
    ./gradlew build
    ```

4.  Ejecuta la aplicación:

    ```bash
    ./gradlew bootRun
    ```

## Documentación de la API (Swagger)

Esta API incluye documentación interactiva utilizando Swagger UI. Para acceder a la documentación, sigue estos pasos:

1. Asegúrate de que la aplicación esté en ejecución.
2. Abre tu navegador web y navega a la siguiente URL:

    ```bash
    http://localhost:8090/webjars/swagger-ui/index.html#/
    ```

3. Explora los endpoints disponibles, sus parámetros y los modelos de datos.
4. Realiza solicitudes de prueba directamente desde la interfaz de Swagger UI.

**Nota:** La URL puede variar dependiendo de la configuración de tu aplicación y el puerto en el que se esté ejecutando. Asegúrate de ajustar la URL si es necesario.

## Endpoints

A continuación se listan algunos de los endpoints principales de la API:

* **Franquicias:**
    * `POST /franquicias`: Crea una nueva franquicia.
    * `PUT /franquicias/{id}`: Actualiza el nombre de una franquicia existente.
    * `GET /franquicias/{id}`: Obtiene información de una franquicia por su ID.
* **Sucursales:**
    * `POST /sucursales`: Crea una nueva sucursal.
    * `PUT /sucursales/{id}`: Actualiza el nombre de una sucursal existente.
    * `POST /sucursales/{id}/productos`: Agrega un producto a una sucursal existente.
    * `DELETE /sucursales/{id}/productos/{productoId}`: Elimina un producto de una sucursal existente.
    * `PUT /sucursales/{id}/productos/{productoId}/stock`: Actualiza el stock de un producto en una sucursal existente.
    * `GET /franquicias/{id}/productos/mayor-stock`: Obtiene el producto con mayor stock por sucursal en una franquicia.
* **Productos:**
    * `PUT /productos/{id}/nombre`: Actualiza el nombre de un producto existente.

## Contenedorización (Docker)

1.  Construye la imagen Docker:

    ```bash
    docker build -t franchise-image .
    ```

2.  Ejecuta el contenedor Docker:

    ```bash
    docker run --rm --name franchise-app -p 8090:8090 franchise-image
    ```

# Despliegue de Amazon DocumentDB con AWS CloudFormation

Esta sección explica cómo desplegar una base de datos **Amazon DocumentDB** utilizando la plantilla de AWS CloudFormation nombrada **documentdb.yml**.

## Requisitos Previos

Antes de ejecutar el despliegue, asegúrate de contar con:

- Una VPC configurada.
- Al menos dos subnets públicas o privadas.
- AWS CLI instalada y configurada con credenciales válidas.
- La plantilla YAML (`documentdb.yaml`) correctamente estructurada.

## Parámetros de la plantilla

| Parámetro   | Descripción                                                        |
|-------------|--------------------------------------------------------------------|
| `VpcId`     | ID de la VPC donde se desplegará el clúster de DocumentDB.         |
| `Subnet1`   | ID de la primera subnet.                                           |
| `Subnet2`   | ID de la segunda subnet.                                           |
| `DBUsername`| Nombre de usuario administrador para DocumentDB.                   |
| `DBPassword`| Contraseña para el usuario administrador.                          |

## Comando de despliegue

Ejecuta el siguiente comando para crear el stack de CloudFormation:

```bash
aws cloudformation create-stack \
  --stack-name documentdb-stack \
  --template-body file://documentdb.yml \
  --parameters \
      ParameterKey=VpcId,ParameterValue=vpc-xxxxxxxx \
      ParameterKey=Subnet1,ParameterValue=subnet-xxxxxxxx \
      ParameterKey=Subnet2,ParameterValue=subnet-yyyyyyyy \
      ParameterKey=DBUsername,ParameterValue=username \
      ParameterKey=DBPassword,ParameterValue=password
```

## Contribución

Si deseas contribuir a este proyecto, por favor sigue los siguientes pasos:

1.  Haz un fork del repositorio.
2.  Crea una rama para tu contribución: `git checkout -b feature/mi-contribucion`.
3.  Realiza los cambios y commitea: `git commit -m "Agrega mi contribucion"`.
4.  Haz push a la rama: `git push origin feature/mi-contribucion`.
5.  Abre un pull request.