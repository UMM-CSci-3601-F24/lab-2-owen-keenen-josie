package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {

    InputStream resourceAsStream = getClass().getResourceAsStream(todoDataFile);
    if (resourceAsStream == null) {
      throw new IOException("Could not find " + todoDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourceAsStream);
    ObjectMapper objectMapper = new ObjectMapper();
    allTodos = objectMapper.readValue(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }
  /**
   * @param id
   * @return
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  public Todo[] getAllTodosArray() {  // This is for testing purposes
    return allTodos;
  }

  public Todo[] listTodos(Map<String, List<String>> queryParamMap) {
    Todo[] filteredTodos = allTodos;


    if (queryParamMap.containsKey("owner")) {
      String ownerParam = queryParamMap.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, ownerParam);
    }

    if (queryParamMap.containsKey("category")) {
      String categoryParam = queryParamMap.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, categoryParam);
    }

    if (queryParamMap.containsKey("status")) {
      String statusParam = queryParamMap.get("status").get(0);
      if (statusParam.equals("complete")) {
      filteredTodos = filterTodosByStatus(filteredTodos, true);
      } else {
      filteredTodos = filterTodosByStatus(filteredTodos, false);
      }
    }

    if (queryParamMap.containsKey("contains")) {
      String bodyParam = queryParamMap.get("contains").get(0);
      filteredTodos = filterTodosByBody(filteredTodos, bodyParam);
    }
    if (queryParamMap.containsKey("orderBy")) {
      String orderParam = queryParamMap.get("orderBy").get(0);

      if (orderParam.equals("owner")) {
        filteredTodos = orderByOwner();
      }

      if (orderParam.equals("category")) {
        filteredTodos = orderByCategory();
      }

      if (orderParam.equals("body")) {
        filteredTodos = orderByBody();
      }

      if (orderParam.equals("status")) {
        filteredTodos = orderByStatus();
      }

    }

    if (queryParamMap.containsKey("limit")) {
      String limitParam = queryParamMap.get("limit").get(0);
      filteredTodos = limitTodos(filteredTodos, Integer.parseInt(limitParam));
    }

    return filteredTodos;
  }

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, Boolean targetStatus) {
    return Arrays.stream(todos).filter(x -> x.status == targetStatus).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByBody(Todo[] todos, String targetString) {
    return Arrays.stream(todos).filter(x -> x.body.contains(targetString)).toArray(Todo[]::new);
  }

  public Todo[] orderByOwner() {
    List<Todo> todos = Arrays.asList(allTodos);
    todos.sort(Comparator.comparing(t -> t.owner));
    return todos.toArray(new Todo[] {});
  }

  public Todo[] orderByCategory() {
    List<Todo> todos = Arrays.asList(allTodos);
    todos.sort(Comparator.comparing(t -> t.category));
    return todos.toArray(new Todo[] {});
  }

  public Todo[] orderByBody() {
    List<Todo> todos = Arrays.asList(allTodos);
    todos.sort(Comparator.comparing(t -> t.body));
    return todos.toArray(new Todo[] {});
  }

  public Todo[] orderByStatus() {
    List<Todo> todos = Arrays.asList(allTodos);
    todos.sort(Comparator.comparing(t -> t.status));
    return todos.toArray(new Todo[] {});
  }

  public Todo[] limitTodos(Todo[] todos, int n) {
    Todo[] temp = new Todo[n];    // Makes array of size n
    for (int i = 0; i < n; i++) { // Will run until i is equal to or greater than n
      temp[i] = todos[i];         // copies over the values (in order)
    }
    return temp;  // return temp

  }

}
