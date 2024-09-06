package umm3601.todo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.javalin.http.Context;
import umm3601.Main;

public class TodoSortSpec {

  private TodoController todoController;
  private static TodoDatabase db;

  @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    db = new TodoDatabase(Main.TODO_TEST_DATA_FILE);
    // This is a differnt JSON file than normal
    // It is smaller as to make testing easier
    todoController = new TodoController(db);
  }

  @Mock
  private Context ctx;

  @Captor
  private ArgumentCaptor<Todo[]> todoArrayCaptor;

  @Test
  @SuppressWarnings({ "MagicNumber" })
  public void canSortByOwner() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] {"owner"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());

    Todo[] todos = db.getAllTodosArray();

    assertEquals("Barry", todos[0].owner);
    assertEquals("Blanche", todos[1].owner);
    assertEquals("Dawn", todos[2].owner);
    assertEquals("Fry", todos[3].owner);
    assertEquals("Fry", todos[4].owner);
  }

  @Test
  @SuppressWarnings({ "MagicNumber" })
  public void canSortByCategory() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] {"category"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());

    Todo[] todos = db.getAllTodosArray();

    assertEquals("homework", todos[0].category);
    assertEquals("software design", todos[1].category);
    assertEquals("software design", todos[2].category);
    assertEquals("video games", todos[3].category);
    assertEquals("video games", todos[4].category);
  }

  @Test
  @SuppressWarnings({ "MagicNumber" })
  public void canSortByBody() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] {"body"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(todoArrayCaptor.capture());
  }

  @Test
  @SuppressWarnings({ "MagicNumber" })
  public void canSortByStatus() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] {"status"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(todoArrayCaptor.capture());

    Todo[] todos = db.getAllTodosArray();

    assertEquals(false, todos[0].status);
    assertEquals(false, todos[1].status);
    assertEquals(false, todos[2].status);
    assertEquals(true, todos[3].status);
    assertEquals(true, todos[4].status);
  }

}
