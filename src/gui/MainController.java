package gui;

import businesslogic.GlobalData;
import businesslogic.GrainMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    public Button startSimulationButton;
    public Button clearDataButton;
    public CheckBox startWithInclusionsCheckBox;
    @FXML
    Canvas canvas;

    GrainMap grainMap;
    GlobalData globalData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.globalData = new GlobalData();
        canvas.setVisible(true);
    }

    public void getGlobalDataFromGUI() {
        int numberOfInitialGrains = Integer.parseInt(numberOfGrainsField.getText());
        this.globalData.setNumberOfInitialGrains(numberOfInitialGrains);

        int dimensionX = Integer.parseInt(dimensionXField.getText());
        this.globalData.setNumberOfGrainsAtX(dimensionX);

        int dimensionY = Integer.parseInt(dimensionYField.getText());
        this.globalData.setNumberOfGrainsAtY(dimensionY);

        if (periodicRadioButton.isSelected()) {
            this.globalData.setNeighbourType("periodic");
        } else {
            this.globalData.setNeighbourType("absorbing");
        }
        
        if (startWithInclusionsCheckBox.isSelected()) {
            this.globalData.setNumberOfInclusions(Integer.parseInt(numberOfInclusionsField.getText()));
            this.globalData.setSizeOfInclusion(Integer.parseInt(sizeOfInclusionsField.getText()));

            if (circleInclusionRadioButton.isSelected()) {
                this.globalData.setTypeOfInclusion("circle");
            } else {
                this.globalData.setTypeOfInclusion("square");
            }

        }



    }


    public void startSimulation() {
        getGlobalDataFromGUI();

        this.grainMap = new GrainMap(this.globalData);
        View view = new View(this.canvas, this.globalData);

        view.generateView(this.grainMap.getCurrentStep());

        int generationsCounter = 0;
        while (grainMap.hasEmptyCells()) {

            grainMap.nextStep();
            view.generateView(grainMap.getCurrentStep());

           /* CanvasRedrawThread canvasRedrawThread = new CanvasRedrawThread(view, grainMap.currentStep);
            Thread thread = new Thread(canvasRedrawThread);
            thread.setDaemon(true);
            thread.start();
*/

            /*try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


            generationsCounter++;
            System.out.println(generationsCounter);


        }

        GrainMap.IdCounter = 0;
    }

    public void clearData() {
        grainMap = null;
        GrainMap.IdCounter = 0;
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void addInclusions() {
        if (grainMap.getCurrentStep() == null) { //add inclusions before grain algorithm


        } else { //add inclusions after grain algo

        }


    }
}
