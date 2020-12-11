package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TextField numberOfGrainsField;
    public TextField numberOfInclusionsField;
    public TextField sizeOfInclusionsField;
    public RadioButton squareInclusionRadioButton;
    public RadioButton circleInclusionRadioButton;
    public Button addInclusionsButton;
    public TextField dimensionXField;
    public TextField dimensionYField;
    public RadioButton absorbingRadioButton;
    public RadioButton periodicRadioButton;
    @FXML Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.beginPath();
        graphicsContext.setFill(Color.rgb(225, 229, 111));
        graphicsContext.rect(100, 100, 100, 100);
        graphicsContext.fill();

        graphicsContext.beginPath();
        graphicsContext.setFill(Color.rgb(233, 33, 111));
        graphicsContext.rect(200, 200, 200, 200);
        graphicsContext.fill();
    }


    public void startSimulation() {



    }
}
