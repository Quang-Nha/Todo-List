module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.nhale.todoList to javafx.fxml;
    exports com.nhale.todoList;
}