package com.zentasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Button addBtn;

    @FXML
    public void initialize() {
        System.out.println("ZenTasks UI loaded!");

        addBtn.setOnAction(e -> {
            System.out.println("Add Task button clicked");
        });
    }

    public void addTask() {
        // TODO: implement adding a task
    }

    public void deleteTask(CheckBox taskCheckbox) {
        // TODO: implement deleting a task
    }
}