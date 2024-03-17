package scheduling;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SchedulerApp {

    private Stage primaryStage;
    private VBox processScreen;
    private int numOfProcesses;

    public SchedulerApp(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Scheduling Algorithms App");
    }

    public void start() {
        // Initial screen to get the number of processes
        setupProcessInputScreen();
    }

    private void setupProcessInputScreen() {
        VBox inputScreen = new VBox(10);
        inputScreen.setAlignment(Pos.CENTER);

        Label processLabel = new Label("Enter the number of processes:");
        TextField processTextField = new TextField();
        HBox h = new HBox(processTextField);
        h.setAlignment(Pos.CENTER);
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            try {
                numOfProcesses = Integer.parseInt(processTextField.getText());
                setupAlgorithmSelectionScreen();
            } catch (NumberFormatException ex) {
                showAlert("Invalid input. Please enter a valid number.");
            }
        });

        inputScreen.getChildren().addAll(processLabel, h, startButton);

        Scene scene = new Scene(inputScreen, 300, 200);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupAlgorithmSelectionScreen() {
        VBox algorithmScreen = new VBox(10);
        algorithmScreen.setAlignment(Pos.CENTER);

        Label algorithmLabel = new Label("Select an algorithm:");
        ComboBox<String> algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("FCFS", "SJF", "Priority", "Round Robin");
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            String selectedAlgorithm = algorithmComboBox.getValue();
            if (selectedAlgorithm != null) {
                setupProcessDetailsScreen(selectedAlgorithm);
            } else {
                showAlert("Please select an algorithm.");
            }
        });

        algorithmScreen.getChildren().addAll(algorithmLabel, algorithmComboBox, nextButton);

        Scene scene = new Scene(algorithmScreen, 300, 200);
        primaryStage.setScene(scene);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupProcessDetailsScreen(String algorithm) {
        processScreen = new VBox(10);
        processScreen.setAlignment(Pos.CENTER);

        for (int i = 0; i < numOfProcesses; i++) {
            HBox processBox = new HBox(10);
            processBox.setAlignment(Pos.CENTER);

            Label burstLabel = new Label("Burst Time for Process " + i + ":");
            TextField burstTextField = new TextField();

            if (algorithm.equals("Priority")) {
                Label priorityLabel = new Label("Priority for Process " + i + ":");
                TextField priorityTextField = new TextField();
                processBox.getChildren().addAll(burstLabel, burstTextField, priorityLabel, priorityTextField);
            } else {
                processBox.getChildren().addAll(burstLabel, burstTextField);
            }

            processScreen.getChildren().add(processBox);
        }

        Button finishButton = new Button("Finish");
        finishButton.setOnAction(e -> {
            calculateAndDisplayResults(algorithm);
        });

        processScreen.getChildren().add(finishButton);

        Scene scene = new Scene(processScreen, 400, 300);
        primaryStage.setScene(scene);
    }

    private void calculateAndDisplayResults(String algorithm) {
        List<Process> processes = getProcessesFromInputScreen(algorithm);

        Service<List<Process>> schedulingService = new Service<List<Process>>() {
            @Override
            protected Task<List<Process>> createTask() {
                return new Task<List<Process>>() {
                    @Override
                    protected List<Process> call() {
                        switch (algorithm) {
                            case "FCFS":
                                return Scheduler.fcfs(processes);
                            case "SJF":
                                return Scheduler.sjf(processes);
                            case "Priority":
                                return Scheduler.priority(processes);
                            case "Round Robin":
                                // You may need to adjust the time quantum value
                                return Scheduler.roundRobin(processes, 2);
                            default:
                                throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
                        }
                    }
                }
            }
        }

        schedulingService.setOnSucceeded(event -> {
            List<Process> scheduledProcesses = schedulingService.getValue();

            float avgWaitingTime = calculateAverageWaitingTime(scheduledProcesses);
            float avgTurnaroundTime = calculateAverageTurnaroundTime(scheduledProcesses);

            Label resultLabel = new Label("Results for " + algorithm + ":\n" +
                    "Average Waiting Time: " + avgWaitingTime + "\n" +
                    "Average Turnaround Time: " + avgTurnaroundTime);

            VBox resultScreen = new VBox(10);
            resultScreen.setAlignment(Pos.CENTER);
            resultScreen.getChildren().addAll(resultLabel);

            Scene scene = new Scene(resultScreen, 300, 200);
            primaryStage.setScene(scene);
        });

        schedulingService.start();
    }

    private List<Process> getProcessesFromInputScreen(String algorithm) {
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < numOfProcesses; i++) {
            HBox processBox = (HBox) processScreen.getChildren().get(i);
            TextField burstTextField = (TextField) processBox.getChildren().get(1);

            int burstTime = Integer.parseInt(burstTextField.getText());

            if (algorithm.equals("Priority")) {
                TextField priorityTextField = (TextField) processBox.getChildren().get(3);
                int priority = Integer.parseInt(priorityTextField.getText());
                processes.add(new Process(i, burstTime, priority));
            } else {
                processes.add(new Process(i, burstTime));
            }
        }

        return processes;
    }


    private float calculateAverageWaitingTime(List<Process> processes) {
        float totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    private float calculateAverageTurnaroundTime(List<Process> processes) {
        float totalTurnaroundTime = 0;
        for (Process process : processes) {
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        return totalTurnaroundTime / processes.size();
    }
}
