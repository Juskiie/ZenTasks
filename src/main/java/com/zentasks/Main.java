package com.zentasks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
//        System.out.println("New project new me.");
//
//        Task t1 = new Task(1, "Finish ZenTasks UI", "Create JavaFX layout",
//                LocalDate.of(2025, 10, 5), Priority.HIGH);
//
//        System.out.println(t1);
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("ZenTasks");
        stage.setScene(scene);
        stage.show();
    }
}