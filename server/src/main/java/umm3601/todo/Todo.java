package umm3601.todo;
  @SuppressWarnings({"VisibilityModifier"})
  public class Todo {

    @SuppressWarnings({"MemberName"})
    public String _id;
    public String owner;
    public Boolean status;
    public String body;
    public String category;

    @Override
    public String toString() {
      return body;
    }
}

