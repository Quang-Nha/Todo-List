package com.nhale.todoList;

import com.nhale.todoList.dataModel.TodoData;
import com.nhale.todoList.dataModel.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class Controller implements Initializable {

//    private List<TodoItem> todoItems;

    @FXML
    public ListView<TodoItem> todoListView;
    @FXML
    public TextArea itemDetailsTextArea;
    @FXML
    public Label kpiLabel;
    @FXML
    public BorderPane mainBoderPane;
    @FXML
    public ToggleButton filterToggleButton;

    FilteredList<TodoItem> filteredList;
    private Predicate<TodoItem> wantAllItems;
    private Predicate<TodoItem> wantTodayItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        todoItems = new ArrayList<>();
//
//        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John",
//                LocalDate.of(2016, Month.APRIL, 25));
//        TodoItem item2 = new TodoItem("Doctor's Appointment", "See Dr. Smith at 123 Main Street.  Bring paperwork",
//                LocalDate.of(2016, Month.MAY, 23));
//        TodoItem item3 = new TodoItem("Finish design proposal for client", "I promised Mike I'd email website mockups by Friday 22nd April",
//                LocalDate.of(2016, Month.APRIL, 22));
//        TodoItem item4 = new TodoItem("Pickup Doug at the train station", "Doug's arriving on March 23 on the 5:00 train",
//                LocalDate.of(2016, Month.MARCH, 23));
//        TodoItem item5 = new TodoItem("Pick up dry cleaning", "The clothes should be ready by Wednesday",
//                LocalDate.of(2016, Month.APRIL,20));
//
//        todoItems.add(item1);
//        todoItems.add(item2);
//        todoItems.add(item3);
//        todoItems.add(item4);
//        todoItems.add(item5);
//
//        TodoData.getInstance().setTodoItems(todoItems);

        // s??? ki???n thay ?????i h??ng ??ang select trong listview thay ?????i gi?? tr??? c??c control kh??c t????ng ???ng
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem todoItem, TodoItem t1) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();

                if (item == null) {
                    return;
                }

                itemDetailsTextArea.setText(item.getDetail());

                DateTimeFormatter df = DateTimeFormatter.ofPattern("d - MMMM - yyyy");
                kpiLabel.setText(df.format(item.getKpi()));
            }
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteItem);

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteItem();
            }
        });

        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> listCell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean empty) {
                        super.updateItem(todoItem, empty);

                        if (empty) {
                            setText(null);
                        } else {
                            setText(todoItem.getShortDescription());
                            setStyle("-fx-control-inner-background: derive(bisque, 50%);");
                            if (todoItem.getKpi().equals(LocalDate.now())) {
                                setTextFill(Color.GREEN);
                            } else if (todoItem.getKpi().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BLUE);
                            } else if (todoItem.getKpi().isBefore(LocalDate.now())) {
                                setTextFill(Color.RED);
                            }
                        }
                    }
                };

                //n???u ?? ??ang ch???n ko null th?? th??m contextmenu
                listCell.emptyProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldCell, Boolean newCell) {
                        if (!newCell) {
                            listCell.setContextMenu(contextMenu);
                        }
                    }
                });

                return listCell;
            }
        });

        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        };

        wantTodayItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return todoItem.getKpi().equals(LocalDate.now());
            }
        };

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(),
                wantAllItems);

        SortedList<TodoItem> sortedList = new SortedList<>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return o1.getKpi().compareTo(o2.getKpi());
                    }
                });

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

    }

    /**
     * x??a item trong listView
     */
    private void deleteItem() {
        TodoItem selectItem = todoListView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + selectItem.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().getTodoItems().remove(selectItem);
        }

    }

    /**
     * th??m item v??o listView
     *
     * @param event s??? ki???n
     */
    public void showNewDialog(ActionEvent event) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBoderPane.getScene().getWindow());// l???y window ??ang ch???y

        dialog.setTitle("Add new Todo Item");
        dialog.setHeaderText("Use this dialog to create a new Todo Item");

        // th??m 2 n??t ok, cancel v??o dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Controller.class.getResource("todoItemDialog.fxml"));// th??m ui fxml

        try {
            dialog.getDialogPane().setContent(loader.load());// li??n k???t ui fxml v??o dialog
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }

        Optional<ButtonType> result = dialog.showAndWait(); // l???y k???t qu??? n??t nh???n dialog tr??? v??? sau khi t???t dialog

        // n???u l?? n??t ok th?? th??m item nh???p t??? dialog v??o listview
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = loader.getController();// l???y controller c???a ui fxml
            TodoItem newItem = controller.processResult();// nh???n TodoItem t??? h??m c???a controller tr??? v???, h??m n??y ???? th??m item v??a list li??n k???t v???i listview
            todoListView.getSelectionModel().select(newItem);//cho list view ch???n item tr??n
        } else {
            System.out.println("cancel");
        }

    }

    @FXML
    /**
     * s??? ki???n b??n ph??m
     */
    public void keyHandle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            deleteItem();
        }
    }

    @FXML
    public void handleFilterButton(ActionEvent event) {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(wantTodayItems);
            if (filteredList.isEmpty()) {
                itemDetailsTextArea.clear();
                kpiLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }

        } else {
            filteredList.setPredicate(wantAllItems);
            if (selectedItem != null) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        }
    }

    public void handleExit(ActionEvent event) {
//        Platform.exit();
        System.exit(0);
    }
}