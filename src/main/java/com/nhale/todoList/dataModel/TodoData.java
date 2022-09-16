package com.nhale.todoList.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {
    private static TodoData instance;
    private static final String fileName = "TodoListItem.txt";

    private ObservableList<TodoItem> todoItems;
    private final DateTimeFormatter formatter;

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static TodoData getInstance() {
        if (instance == null) {
            instance = new TodoData();
        }
        return instance;
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(ObservableList<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public void loadTodoItem() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);

//        if (Files.notExists(path)) {
//            Files.createFile(path);
//        }

        BufferedReader reader = Files.newBufferedReader(path);
        String input;

        try(reader) {
            while ((input = reader.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shotDesc = itemPieces[0];
                String detail = itemPieces[1];
                LocalDate kpi  = LocalDate.parse(itemPieces[2], formatter);

                todoItems.add(new TodoItem(shotDesc, detail, kpi));
            }
        }
    }

    public void storeTodoItems() throws IOException {
        Path path = Paths.get(fileName);

        //file của io
//        File file = new File("dafa");
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        BufferedWriter writer = Files.newBufferedWriter(path);

        try(writer) {
            Iterator<TodoItem> iterator = todoItems.iterator();
            while (iterator.hasNext()) {
                TodoItem item = iterator.next();
                writer.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetail(),
                        item.getKpi().format(formatter)));
                writer.newLine();
            }
        }
    }
}
