## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


Statement
Es la interfaz más básica para ejecutar sentencias SQL estáticas (sin parámetros) que no cambian en tiempo de ejecución.

Funcionalidad:

Ejecuta consultas como SELECT, INSERT, UPDATE o DELETE.

No es eficiente para consultas repetitivas.

Vulnerable a la inyección SQL porque concatena las cadenas de texto del código Java con el código SQL, permitiendo que un atacante inyecte código malicioso.

PreparedStatement
Es una interfaz que extiende a Statement y está diseñada para ejecutar sentencias SQL pre-compiladas con parámetros.

Funcionalidad:

Evita la inyección SQL al tratar los valores de entrada como datos, no como parte de la consulta SQL. Esto lo hace mucho más seguro.

Mejora el rendimiento para consultas que se ejecutan varias veces, ya que el motor de la base de datos solo necesita compilar la sentencia una vez.

Permite el uso de marcadores de posición (?) para sustituir valores en tiempo de ejecución.

CallableStatement
Es otra interfaz que se usa para ejecutar procedimientos almacenados y funciones en la base de datos.

Funcionalidad:

Permite el uso de parámetros de entrada, salida y de entrada-salida.

Es útil para operaciones complejas o para aprovechar la lógica de negocio almacenada directamente en la base de datos.


