# Ticket Master

## Descripción

Ticket Master es una aplicación de consola desarrollada en Java para la gestión de una tiquetera. Permite consultar eventos, gestionar asientos y realizar compras de entradas, facilitando el almacenamiento de la información y la generación de tickets en formato PDF.

El proyecto aplica principios de programación orientada a objetos y busca mantener una separación de responsabilidades entre los modelos, los repositorios y los servicios.

## Funcionalidades

- **Gestión de eventos:** consulta de los eventos disponibles.
- **Gestión de asientos:** consulta de asientos por evento y control de disponibilidad.
- **Compra de entradas:** validación de los datos del comprador y de los asientos seleccionados.
- **Cálculo de compras:** cálculo del subtotal, el costo del servicio y el total.
- **Persistencia de datos:** almacenamiento de eventos y asientos en archivos CSV y de las compras en un archivo JSON.
- **Generación de tickets PDF:** creación de documentos PDF asociados a las compras.
- **Generación de códigos QR:** incorporación de información de la compra en códigos QR.
- **Auditoría:** registro de operaciones relevantes de la aplicación.
- **Pruebas unitarias:** pruebas con JUnit 5 y Mockito para verificar el comportamiento de los servicios.

## Tecnologías utilizadas

- Java 20
- Maven
- Gson
- Apache PDFBox
- ZXing
- JUnit 5
- Mockito
- IntelliJ IDEA
- Git y GitHub

## Estructura del proyecto

```text
ticket_master/
├── Data/
│   ├── eventos.csv
│   ├── asientos.csv
│   ├── compras.json
│   └── auditoria.txt
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/example/
│   │           ├── Main.java
│   │           ├── MenuPrincipal.java
│   │           ├── modelo/
│   │           ├── repositorios/
│   │           ├── servicios/
│   │           └── excepciones/
│   └── test/
│       └── java/
│           └── org/example/
├── pom.xml
└── README.md
```

### Organización del código

- **modelo:** contiene las clases que representan los eventos, los asientos y las compras.
- **repositorios:** se encargan de leer y guardar información en archivos.
- **servicios:** contienen la lógica de negocio, las validaciones, los cálculos y la generación de documentos.
- **excepciones:** contiene excepciones personalizadas para gestionar situaciones específicas.
- **pruebas:** contiene las pruebas unitarias de los componentes de la aplicación.

## Requisitos previos

Para ejecutar el proyecto necesitas:

- Tener instalado JDK 20 o una versión compatible con la configuración de Maven.
- Tener Maven instalado o utilizar el Maven Wrapper si está disponible.
- Contar con un entorno de desarrollo como IntelliJ IDEA.

## Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. Ingresa a la carpeta del proyecto:

   ```bash
   cd ticket_master
   ```

3. Compila el proyecto:

   ```bash
   mvn clean compile
   ```

4. Ejecuta las pruebas:

   ```bash
   mvn test
   ```

5. Abre el proyecto en IntelliJ IDEA y ejecuta la clase `Main.java`.

**Nota:** reemplaza `<URL_DEL_REPOSITORIO>` por la URL real del repositorio de GitHub.

## Almacenamiento de datos

La aplicación utiliza archivos locales para persistir la información:

- `eventos.csv`: información de los eventos.
- `asientos.csv`: información de los asientos y su estado de disponibilidad.
- `compras.json`: información de las compras registradas.
- `auditoria.txt`: registro de operaciones relevantes.

Los archivos deben encontrarse en las rutas configuradas en la aplicación. Se recomienda conservar una copia de seguridad de los datos antes de realizar modificaciones manuales.

## Pruebas

El proyecto utiliza JUnit 5 para las pruebas unitarias y Mockito para simular dependencias y verificar interacciones entre componentes.

Para ejecutar las pruebas desde Maven:

```bash
mvn test
```

## Autoría

Proyecto académico desarrollado como parte del proceso de formación en Ingeniería de Sistemas.

## Licencia

Este proyecto se desarrolla con fines académicos.
