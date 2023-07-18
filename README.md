# Zextras backend interview - TodoList

v1.0

## Description
This is a test to assess your skills on Java, SQL, Maven and the application of some design patterns.

To start working on this test:
 - create a branch starting from main
 - make some changes and commit them 
 - make a PR (Merge request/Pull request) to main when the assessment is completed

Take into account that every PR will be processed and reviewed like any other one inside the Company,
so expect that, after requesting a review, there might be some change requests ;)

If something is not clear, please ask.


## Coding exercise
Implement a service exposing a set of APIs to manage simple `Todo` objects.
The project is already initialized with:
- a sql script to create and fill the tables with some data (see the `resources` folder)
- an `OpenAPI` schema containing a draft of the API to implement (see the `resources` folder)
- a simple `Maven` project containing all the dependencies necessary to complete the test
- a boilerplate based on `Jetty`+`RestEasy` and the connection with the database 
- a single `App.java` class containing the draft codebase

### Todo and Task
A `Todo` object is composed by:
 - an identifier
 - a description
 - a creation timestamp

It should contain a list of `Task` objects, each of them is composed by:
 - an identifier
 - a description
 - a creation timestamp
 - a boolean representing if it is `done` or not.

### APIs
The service should expose a set of APIs that allow a client to:
 - Get a list of all saved todos
 - Get a single todo (without its tasks)
 - Create a todo
 - Get a list of all tasks of a specific todo
 - Create a tasks for a specific todo
 - Mark a task as done

Some of them are already implemented, but it contains some error to fix.


## How we evaluate the test
The requested goals are the following (in order of importance):
 - Implement the missing APIs
 - Add javadoc where necessary
 - Fix some bugs/bad implementation that you may find (check also the OpenAPI and the SQL schemas)
 - Create some sort of consistency between each API
 - Refactor the existing codebase applying some design patterns
 - Try to use all the dependencies specified in the pom.xml

If you find that a different implementation or a new dependency of what is already existing can help 
you to write a cleaner code, you are free to changed/add it as you want. The only part that should 
remain __as is__ are:
 - the implementation of the `public static void startServer()` method
 - the existence of the `public class RestApplication extends Application` class 
   (the content of the `public Set<Object> getSingletons()` method can be changed)

We will pay attention on the maintainability, the robustness and the readability of the codebase.


## Tips
To have a fresh and initialized database you can run a postgres docker container executing this 
command in the root of the project:

```bash
docker run \
    -p 5500:5432 \
    -e POSTGRES_USER=todolist \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_DB=todolist-db \
    -e POSTGRES_HOST_AUTH_METHOD=trust \
    -v ./src/main/resources/postgres_init.sql:/docker-entrypoint-initdb.d/create_tables.sql \
    --rm \
    postgres:latest
```
