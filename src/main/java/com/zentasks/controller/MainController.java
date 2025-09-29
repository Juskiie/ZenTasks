package com.zentasks.controller;

import com.zentasks.model.Priority;
import com.zentasks.model.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML
    private Button addBtn;
    @FXML
    private VBox placeholderTasks;

    private List<Task> tasks = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("ZenTasks UI loaded!");

//        addBtn.setOnAction(e -> {
//            System.out.println("Add Task button clicked");
//        });

        // Example tasks, just for testing.
        tasks.add(new Task(1, "Finish ZenTasks UI", "Create JavaFX layout",
                LocalDate.of(2025,10,5), Priority.HIGH));
        tasks.add(new Task(2, "Write documentation", "Update project README",
                LocalDate.of(2025,10,10), Priority.MEDIUM));

        loadTasks();
    }

    public void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add new task");
        dialog.setHeaderText("Enter task details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);

        DatePicker dueDatePicker = new DatePicker();

        ComboBox<Priority> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(Priority.LOW, Priority.MEDIUM, Priority.HIGH);
        priorityComboBox.setValue(Priority.MEDIUM);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            addButton.setDisable(newVal.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Task(
                        tasks.size() + 1,
                        titleField.getText(),
                        descField.getText(),
                        dueDatePicker.getValue(),
                        priorityComboBox.getValue()
                );
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();

        result.ifPresent(task -> {
            tasks.add(task);
            loadTasks();
        });
    }

    public void deleteTask(CheckBox taskCheckbox) {
        // TODO: implement deleting a task
    }

    private void loadTasks() {
        placeholderTasks.getChildren().clear();

        for (Task task : tasks) {
            HBox taskCard = new HBox(10);
            taskCard.getStyleClass().add("task-card");

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(task.isCompleted());

            VBox taskInfo = new VBox();
            Label title = new Label(task.getTitle());
            title.getStyleClass().add("task-title");

            Label meta = new Label("Due: " + task.getDueDate() + " • Priority: " + task.getPriority());
            meta.getStyleClass().add("task-meta");

            taskInfo.getChildren().addAll(title, meta);
            taskCard.getChildren().addAll(checkBox, taskInfo);

            placeholderTasks.getChildren().add(taskCard);
        }
    }
}