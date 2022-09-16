module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.nhale.todoList to javafx.fxml;
    exports com.nhale.todoList;
}