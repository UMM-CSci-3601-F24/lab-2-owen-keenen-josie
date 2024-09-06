package umm3601.todo;
import umm3601.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;



public class TodoControllerSpec {

  private TodoController todoController;
  private static TodoDatabase db;

  @Mock
  private Context ctx;

  @Captor
  private ArgumentCaptor<Todo[]> todoArrayCaptor;


 @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    db = new TodoDatabase(Main.TODO_DATA_FILE);
    todoController = new TodoController(db);
  }

  @Test
  public void canFormString() {
    Todo testTodo = new Todo();
    testTodo.body = "This is a test!";

    assertEquals("This is a test!", testTodo.toString());

  }

  @Test
  public void canBuildController() throws IOException {

    TodoController controller = TodoController.buildTodoController(Main.TODO_DATA_FILE);
    Javalin mockServer = Mockito.mock(Javalin.class);
    controller.addRoutes(mockServer);
    verify(mockServer, Mockito.atLeast(2)).get(any(), any());
  }

  @Test
  public void buildControllerFailsWithIllegalDbFile() {
    assertThrows(IOException.class, () -> {
      TodoController.buildTodoController("Legal file name 100%");
    });
  }

  @Test
  public void canGetAllTodos() throws IOException {
    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    assertEquals(db.size(), todoArrayCaptor.getValue().length);
  }


  @Test
  public void canGetTodoWithSpecifiedId() {

    String id = "58895985a22c04e761776d54";
    Todo todo = db.getTodo(id);
    when(ctx.pathParam("id")).thenReturn(id);

    todoController.getTodo(ctx);

    verify(ctx).json(todo);
    verify(ctx).status(HttpStatus.OK);
  }

  @Test
  public void canGetTodoWithInvalidId() {

    String id = null;
    when(ctx.pathParam("id")).thenReturn(id);

    assertThrows(NotFoundResponse.class, () -> todoController.getTodo(ctx));

    Throwable exception = assertThrows(NotFoundResponse.class, () -> {
      todoController.getTodo(ctx);
    });
    assertEquals("No todo with id " + null + " was found.", exception.getMessage());

  }

  @Test
  public void canFilterTrueTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"complete"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertEquals(true, todo.status);
    }
  }

  @Test
  public void canFilterFalseTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"incomplete"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertEquals(false, todo.status);
    }
  }

  @Test
  @SuppressWarnings({ "MagicNumber" })
  public void canFilterTodosByOwner() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("owner", Arrays.asList(new String[] {"Fry"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertEquals("Fry", todo.owner);
    }
    assertEquals(61, todoArrayCaptor.getValue().length);
   }

  @Test
  public void canFilterCategoryTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] {"software design"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
  }

   @Test
   @SuppressWarnings({ "MagicNumber" })
   public void canFilterTodosByBody() throws IOException {
      Map<String, List<String>> queryParams = new HashMap<>();
      queryParams.put("contains", Arrays.asList(new String[] {"laborum"}));
      when(ctx.queryParamMap()).thenReturn(queryParams);

      todoController.getTodos(ctx);

      verify(ctx).json(todoArrayCaptor.capture());
      for (Todo todo : todoArrayCaptor.getValue()) {
          assertTrue(todo.body.contains("laborum"));
      }
      assertEquals(80, todoArrayCaptor.getValue().length);
    }

    @Test
    @SuppressWarnings({ "MagicNumber" })
    public void canLimitTodos() {
      Map<String, List<String>> queryParams = new HashMap<>();
      queryParams.put("limit", Arrays.asList(new String[] {"10"}));
      when(ctx.queryParamMap()).thenReturn(queryParams);

      todoController.getTodos(ctx);

      verify(ctx).json(todoArrayCaptor.capture());
      assertEquals(10, todoArrayCaptor.getValue().length);


    }
}
