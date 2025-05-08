package org.example.emergency_connect;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image icon = new Image(getClass().getResourceAsStream("/org/example/emergency_connect/images/medicine.png"));
        primaryStage.getIcons().add(icon);

        // Load and validate the splash screen FXML
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/SplashScreen.fxml"));
        Scene splashScene = new Scene(splashLoader.load());

        // Set the splash screen style
        primaryStage.initStyle(StageStyle.UNDECORATED); // No title bar
        primaryStage.setScene(splashScene);

        // Center the splash screen
        centerStage(primaryStage);
        primaryStage.show();

        // Pause for 2 seconds and transition to the main screen
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> showMainScreen(primaryStage));
        delay.play();
    }

    private void showMainScreen(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/org/example/emergency_connect/images/medicine.png"));
            primaryStage.getIcons().add(icon);
            // Load the main screen FXML
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/DashboardController.fxml"));
            Scene mainScene = new Scene(mainLoader.load());

            // Set the main scene to the stage
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Disaster Coordination System"); // Optional: Add a title for the main window
            primaryStage.setResizable(false); // Optional: Disable resizing
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void centerStage(Stage stage) {
        stage.show();
        javafx.application.Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
    }
    // Add this method to your main application class
    private void initializeDatabase() {
        try {
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError("Database Connection Failed",
                        "Could not establish connection to the database.\n" +
                                "Please ensure MySQL is running and credentials are correct.");
                System.exit(1);
            }
        } catch (Exception e) {
            showDatabaseError("Database Error",
                    "Failed to initialize database: " + e.getMessage());
            System.exit(1);
        }
    }

    private void showDatabaseError(String title, String content) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args); // Starts the JavaFX application
    }
}
