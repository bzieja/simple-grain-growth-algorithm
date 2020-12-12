package gui;

import businesslogic.GlobalData;
import businesslogic.GrainMap;
import com.sun.javafx.iio.gif.GIFImageLoader2;
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
import java.util.concurrent.TimeUnit;

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
    public Button startSimulationButton;
    public Button clearDataButton;
    @FXML Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
/*
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.beginPath();
        graphicsContext.setFill(Color.rgb(225, 229, 111));
        graphicsContext.rect(100, 100, 100, 100);
        graphicsContext.fill();

        graphicsContext.beginPath();
        graphicsContext.setFill(Color.rgb(233, 33, 111));
        graphicsContext.rect(200, 200, 200, 200);
        graphicsContext.fill();
*/
    }

    public void getGlobalDataFromGUI() {
        int numberOfInitialGrains = Integer.parseInt(numberOfGrainsField.getText());
        GlobalData.setNumberOfInitialGrains(numberOfInitialGrains);

        int dimensionX = Integer.parseInt(dimensionXField.getText());
        GlobalData.setNumberOfGrainsAtX(dimensionX);

        int dimensionY = Integer.parseInt(dimensionYField.getText());
        GlobalData.setNumberOfGrainsAtY(dimensionY);

        String neighbourType;
        if (periodicRadioButton.isSelected()) {
            neighbourType = "periodic";
            GlobalData.setNeighbourType("periodic");

        } else {
            neighbourType = "absorbing";
            GlobalData.setNeighbourType("absorbing");
        }



    }


    public void startSimulation() {
        getGlobalDataFromGUI();

        GrainMap grainMap = new GrainMap(GlobalData.getNumberOfInitialGrains(), GlobalData.getNumberOfGrainsAtX(), GlobalData.getNumberOfGrainsAtY(), GlobalData.getNeighbourType());
        View view = new View(this.canvas, GlobalData.getNumberOfGrainsAtX(), GlobalData.getNumberOfGrainsAtY(), GlobalData.getNumberOfInitialGrains());

        view.generateView(GrainMap.getCurrentStep());


        int generationsCounter = 0;
        while (grainMap.hasEmptyCells()) {
            grainMap.nextStep();
            view.generateView(GrainMap.getCurrentStep());

            generationsCounter++;
            System.out.println(generationsCounter);

/*

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/


        }
        System.out.println(generationsCounter);
    }

    public void clearData() {
        GrainMap.currentStep = null;
        GrainMap.previousStep = null;
        GrainMap.IdCounter = 0;
    }
}
