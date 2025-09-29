package com.zentasks;

import com.zentasks.model.Priority;
import com.zentasks.model.Task;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("New project new me.");

        Task t1 = new Task(1, "Finish ZenTasks UI", "Create JavaFX layout",
                LocalDate.of(2025, 10, 5), Priority.HIGH);

        System.out.println(t1);
    }
}