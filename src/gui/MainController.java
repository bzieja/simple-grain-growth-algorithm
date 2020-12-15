package gui;

import businesslogic.GlobalData;
import businesslogic.GrainMap;
import businesslogic.inclusion.Inclusion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setVisible(true);
    }

    public void getGlobalDataFromGUI() {
        int numberOfInitialGrains = Integer.parseInt(numberOfGrainsField.getText());
        GlobalData.getInstance().setNumberOfInitialGrains(numberOfInitialGrains);

        int dimensionX = Integer.parseInt(dimensionXField.getText());
        GlobalData.getInstance().setNumberOfGrainsAtX(dimensionX);

        int dimensionY = Integer.parseInt(dimensionYField.getText());
        GlobalData.getInstance().setNumberOfGrainsAtY(dimensionY);

        if (periodicRadioButton.isSelected()) {
            GlobalData.getInstance().setNeighbourType("periodic");
        } else if(absorbingRadioButton.isSelected()) {
            GlobalData.getInstance().setNeighbourType("absorbing");
        }

        GlobalData.getInstance().setNumberOfInclusions(Integer.parseInt(numberOfInclusionsField.getText()));
        GlobalData.getInstance().setSizeOfInclusion(Integer.parseInt(sizeOfInclusionsField.getText()));

        if (circleInclusionRadioButton.isSelected()) {
            GlobalData.getInstance().setTypeOfInclusion("circle");
        } else if (squareInclusionRadioButton.isSelected()) {
            GlobalData.getInstance().setTypeOfInclusion("square");
        }

        if(startWithInclusionsCheckBox.isSelected()) {
            GlobalData.getInstance().setStartWithInclusions(true);
        } else {
            GlobalData.getInstance().setStartWithInclusions(false);
        }


    }

    public void startSimulation() {
        softClearData();
        getGlobalDataFromGUI();

        this.grainMap = new GrainMap(GlobalData.getInstance());

        View view = View.getInstance(this.canvas, GlobalData.getInstance(), this.grainMap);
        view.generateView();

        int generationsCounter = 0;
        while (grainMap.hasEmptyCells()) {

            grainMap.nextStep();
            view.generateView();
            //view.generateView(grainMap.getCurrentStep());

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

    }

    public void addInclusionsToExistingCellBoard() {
        getGlobalDataFromGUI();

        if (this.grainMap != null) { //do sth

            Inclusion inclusion = new Inclusion(GlobalData.getInstance(), grainMap.currentStep, grainMap.hasEmptyCells());
            inclusion.add();
            View.getInstance(this.canvas, GlobalData.getInstance(), this.grainMap).generateView();

        } else { //add inclusions after grain algo

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("You must first generate grains cells!");
            alert.setContentText("To generate grains cells click \"start simulation\" button");
            alert.show();
        }

    }

    public void clearData() {
        grainMap = null;
        GrainMap.IdCounter = 0;
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void softClearData() {
        GrainMap.IdCounter = 0;
        GlobalData.getInstance().setNumberOfInclusions(0);
        View.getInstance(this.canvas, GlobalData.getInstance(), this.grainMap).clear();
    }

}
