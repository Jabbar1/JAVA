
Spring-Boot based PostgreSQL Rest Example that Handles CRUD operations in a Single Class
```
Spring Boot + SpringDataJPA + Hibernate + PostgreSQL 9.4 + RESTful Services
```
###Description
```
A Simple Project which is built in Spring Boot and SpringDataJpa which consumes Rest Api's. In this template,
All crud operations are abstracted to a Single class in each layer of project. we can write all CRUD operations 
and common code in Base classes. We can obtainthe behaviour by extending theses Base calsses. Each functionality
gets it's behaviour polymorphically.
```
###How to Open
```
IntelliJ -> Open -> Select Project Directory
```

###How to Run
```
- It is packaged as a war, setup a tomcat server and run server and it will be ready
- If you wan't this project as a JAR then comment <packaging>war</> in pom file and execute the command
- in current directory but make sure no other server runs with the port:8080 as spring boot runs with
  that port by default
- mvn spring-boot:run
```

###Environment

- Java 8
- Spring Boot 1.4.2.RELEASE
- PostgreSQL 9.4
- Maven 3.3.x
