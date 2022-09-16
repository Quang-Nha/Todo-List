package com.nhale.todoList;

import com.nhale.todoList.dataModel.TodoData;
import com.nhale.todoList.dataModel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker kpi;

    public TodoItem processResult() {
        String shortDesc = shortDescriptionField.getText();
        String detail = detailsArea.getText();
        LocalDate kpi = this.kpi.getValue();

        TodoItem newItem = new TodoItem(shortDesc, detail, kpi);
        TodoData.getInstance().getTodoItems().add(newItem);
        return newItem;

    }
}
