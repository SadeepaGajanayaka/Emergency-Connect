package org.example.emergency_connect;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SplashScreen {

    @FXML
    private VBox vBox;

    public void initialize() {
        animateVBox();
    }

    private void animateVBox() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(8000), vBox);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(100.0); // Adjust based on desired size
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);
        scaleTransition.play();
    }
}
