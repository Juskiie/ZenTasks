package com.zentasks.controller;

import com.zentasks.model.Priority;
import com.zentasks.model.Task;
import com.zentasks.util.Database;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML
    private Button addBtn;
    @FXML
    private VBox placeholderTasks;
    @FXML
    private VBox taskListVBox;

    private List<Task> tasks = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("ZenTasks UI loaded!");
        Database.initialize();
        tasks = Database.loadTasks();
        loadTasks();
    }

    @FXML
    private void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
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
            System.out.println(task);
            tasks.add(task);
            Database.saveTask(task);
            loadTasks();
        });
    }

    public void loadTasks() {
        loadTasks(this.tasks);
    }

    public void loadTasks(List<Task> taskList) {
        taskListVBox.getChildren().clear();

        for (Task task : taskList) {
            System.out.println(task);
            System.out.println(task.getId());
            HBox taskCard = new HBox(10);
            taskCard.setAlignment(Pos.CENTER_LEFT);
            taskCard.getStyleClass().add("task-card");

            CheckBox completedCheckBox = new CheckBox();
            completedCheckBox.setSelected(task.isCompleted());
            completedCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                task.setCompleted(newVal);
                updateTaskStyle(taskCard, task);
                Database.saveTask(task);
            });

            VBox taskInfo = new VBox(5);
            Label titleLabel = new Label(task.getTitle());
            titleLabel.getStyleClass().add("task-title");

            Label metaLabel = new Label(
                    "Due: " + (task.getDueDate() != null ? task.getDueDate() : "N/A") +
                            " • Priority: " + task.getPriority()
            );
            metaLabel.getStyleClass().add("task-meta");
            taskInfo.getChildren().addAll(titleLabel, metaLabel);
            HBox.setHgrow(taskInfo, javafx.scene.layout.Priority.ALWAYS);

            Button deleteButton = new Button("X🗑");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(e -> deleteTask(task));

            taskCard.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    editTask(task);
                }
            });

            updateTaskStyle(taskCard, task);

            taskCard.getChildren().addAll(completedCheckBox, taskInfo, deleteButton);
            taskListVBox.getChildren().add(taskCard);

        }
    }


    private void editTask(Task task) {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");
        dialog.setHeaderText("Modify task details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(task.getTitle());
        TextArea descField = new TextArea(task.getDescription());
        descField.setPrefRowCount(3);
        DatePicker dueDatePicker = new DatePicker(task.getDueDate());
        ComboBox<Priority> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(Priority.LOW, Priority.MEDIUM, Priority.HIGH);
        priorityComboBox.setValue(task.getPriority());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(titleField.getText().trim().isEmpty());
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                task.setTitle(titleField.getText());
                task.setDescription(descField.getText());
                task.setDueDate(dueDatePicker.getValue());
                task.setPriority(priorityComboBox.getValue());
                return task;
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();
        result.ifPresent(t -> {
            Database.saveTask(t);
            loadTasks();
        });
    }

    private void deleteTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText("Are you sure you want to delete this task?");
        alert.setContentText(task.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tasks.remove(task);
            Database.deleteTask(task);
            loadTasks();
        }
    }


    private void updateTaskStyle(HBox taskCard, Task task) {
        if (task.isCompleted()) {
            taskCard.setStyle("-fx-opacity: 0.5; -fx-strikethrough: true;");
        } else {
            taskCard.setStyle("-fx-opacity: 1;");
        }
    }

    @FXML
    private void filterAll() {
        loadTasks(tasks); // show all
    }

    @FXML
    private void filterActive() {
        List<Task> activeTasks = tasks.stream()
                .filter(t -> !t.isCompleted())
                .toList();
        loadTasks(activeTasks);
    }

    @FXML
    private void filterCompleted() {
        List<Task> completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .toList();
        loadTasks(completedTasks);
    }

    @FXML
    private void filterHighPriority() {
        List<Task> highPriorityTasks = tasks.stream()
                .filter(t -> t.getPriority() == Priority.HIGH)
                .toList();
        loadTasks(highPriorityTasks);
    }

}