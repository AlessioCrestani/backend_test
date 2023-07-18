package com.zextras.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;

public class App {

  public static void main(String[] args) throws Exception {

    System.out.println("Starting server....");
    startServer();
  }

  /**
   * The content of this method does not contain errors ;)
   */
  public static void startServer() throws Exception {
    Server server = new Server();
    try {
      try (ServerConnector connector = new ServerConnector(server)) {
        connector.setDefaultProtocol("HTTP/1.1");
        connector.setHost("127.0.0.1");
        connector.setPort(5000);
        server.addConnector(connector);
      }

      ServletContextHandler servletContextHandler = new ServletContextHandler();
      servletContextHandler.setContextPath("/");
      ServletHolder servletHolder =
        servletContextHandler.addServlet(HttpServlet30Dispatcher.class, "/*");
      servletHolder.setInitParameter("javax.ws.rs.Application", RestApplication.class.getName());

      server.setHandler(servletContextHandler);

      server.start();
      System.out.println("Server started @ 127.0.0.1:5000");
      server.join();

    } finally {
      server.stop();
    }
  }

  public static Connection getConnection() {
    String dbURL = "jdbc:postgresql://localhost:5500/todolist-db";
    String dbUser = "todolist";
    String dbPassword = "password";
    try {
      Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
      return DriverManager.getConnection(dbURL, dbUser, dbPassword);
    } catch (Exception ignore) {
    }
    return null;
  }

  @Path("/")
  public static class RestAPI {

    @PUT
    @Path("/todos/{name}")
    public Response createTodo(@PathParam("name") String name) throws SQLException {

      Statement statement = getConnection().createStatement();
      ResultSet result1 = statement.executeQuery(
        "SELECT id FROM todo ORDER BY creation_timestamp DESC LIMIT 1;"
      );

      result1.next();
      int lastTodoId = Integer.parseInt(result1.getString(1));

      statement.execute(
        "INSERT INTO todo VALUES("
          + String.valueOf(lastTodoId + 2) + ","
          + "'" + name + "',"
          + System.currentTimeMillis()
          + ");");

      statement.close();
      return Response.ok().entity(Boolean.TRUE).build();
    }

    @GET
    @Path("/todos/{id}")
    public Response getTodo(@PathParam("id") Integer id)
      throws SQLException, JsonProcessingException {

      Statement statement = getConnection().createStatement();
      ResultSet result = statement.executeQuery(
        "SELECT * FROM todo WHERE " + "id = '" + id + "';"
      );
      result.next();

      Map<String, Object> todo = new HashMap<>();
      todo.put("id", result.getString("id"));
      todo.put("name", result.getString("name"));
      todo.put("creation", String.valueOf(result.getLong("creation_timestamp")));

      ResultSet numberOfTaskResult = statement.executeQuery(
        "SELECT COUNT(id) FROM task WHERE todo_id = '" + id + "';"
      );
      numberOfTaskResult.next();
      todo.put("numberOfTasks", numberOfTaskResult.getLong(1));
      statement.close();
      return Response.ok().entity(new ObjectMapper().writeValueAsString(todo)).build();
    }

    @GET
    @Path("/todos")
    public Response getTodos() {
      return Response.ok().build();
    }

    @PUT
    @Path("/tasks")
    public Response createTask(String body) throws SQLException, JsonProcessingException {

      JsonNode jsonRequest = new ObjectMapper().readValue(body, JsonNode.class);
      Statement statement = getConnection().createStatement();

      statement.execute(
        "INSERT INTO task VALUES("
          + new Random().nextInt(9999) + ","
          + "'" + jsonRequest.get("todoId").asText() + "',"
          + "'" + jsonRequest.get("description").asText() + "',"
          + System.currentTimeMillis() + ","
          + false
          + ");");

      statement.close();

      return Response.ok().entity(Boolean.TRUE).build();
    }

    @POST
    @Path("/tasks/{taskId}")
    public Response updateTasks(){
      return Response.ok().build();
    }

    @GET
    @Path("/tasks/{todoId}")
    public Response getTasks(){
      return Response.ok().build();
    }
  }
}
