package com.nhale.todoList;

import com.nhale.todoList.dataModel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        TodoData.getInstance().loadTodoItem();
        super.init();
    }

    @Override
    public void stop() throws Exception {
        TodoData.getInstance().storeTodoItems();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}