ARG MSVC_NAME=msvc-libros

#Imagen base (jdk) y agregando alias a la construcción
FROM openjdk:17-jdk-alpine as builder

#Establecer ruta de trabajo en el directorio del contenedor
#WORKDIR /app

#Pasar archivo jar al directorio de trabajo
#COPY ./target/msvc-libros-0.0.1-SNAPSHOT.jar .

#Establecer puerto interno del contenedor para exponer la aplicación
#EXPOSE 8001

#Establecer punto de entrada para ejecutar comandos
#ENTRYPOINT [ "java", "-jar", "msvc-libros-0.0.1-SNAPSHOT.jar" ]

#Otra forma de generar la imagen, utilizando los pom.xml
#Establecer ruta de trabajo para pom padre y pom de microservicio usuarios
WORKDIR /app/msvc-libros

#Definir argumento para el nombre del directorio
ARG MSVC_NAME

#Copiar proyecto POM del proyecto padre y el proyecto completo
COPY ./pom.xml /app
#COPY ./usuarios-msvc .

#Otimizando la creación de imagen Docker utilizando copiando el directorio .mvn, el archivo
#mvnw (ejecutable) y el pom.xml del proyecto de microservicios, para optimizar la creación de la imagen
COPY ./${MSVC_NAME}/.mvn ./.mvn
COPY ./${MSVC_NAME}/mvnw .
COPY ./${MSVC_NAME}/pom.xml .

#Empaquetar el proyecto, sin el codigo fuente y sin la ejecución de los tests
#Eliminar la carpeta target
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline - se usa si no funciona la isntrucción antertior

#Copiar el código fuente, con este solo se ejecuta esta sentencia cuando haya cambios en el código
COPY ./${MSVC_NAME}/src ./src

#Ejecutar comando maven para generar el archivo jar mientras se costruye la imagen
RUN ./mvnw clean package -DskipTests


#Generando nueva estapa de construcción de la imagen, basada en la anterior (builder)
FROM openjdk:17-jdk-alpine

#Creando directorio de trabajo para la nueva etapa
WORKDIR /app

#Argumento para la carpeta donde se genera el jar
#Se define nuevamente el argumento de MSVC_NAME porque el anterior solo existe en la etapa de contrucción "builder", para que este exista en ambas
#construcciones, se debe definir de manera global (sobre todas las construcciones), el argumento se debe definir, sin asignar ningún valor
#dentro de cada etapa
ARG MSVC_NAME
ARG TARGET_FOLDER=/app/${MSVC_NAME}/target

RUN mkdir ./logs

#Copiando el archivo jar generado en la estapa anterior en el directorio de trabajo de la nueva etapa
COPY --from=builder ${TARGET_FOLDER}/msvc-libros-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8000

#Definiendo variable de ambiente
ENV PORT ${PORT_APP}

#Exponer el puerto
EXPOSE ${PORT}

#Establecer punto de entrada para ejecutar comandos
#ENTRYPOINT [ "java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar" ]
#Reemplazando ENTRYPOINT por CMD, para permitir el usuario de comandos de entrada en la terminal (shell)
#CMD sleep 20 && java -jar msvc-usuarios-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "msvc-libros-0.0.1-SNAPSHOT.jar"]