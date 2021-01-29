package gui;

import app.AppConfiguration;
import app.boundaries.Boundaries;
import app.grid.Cell;
import app.grid.GrainMap;
import app.inclusion.Inclusion;
import app.subphase.SubPhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

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
    public Canvas canvas;
    public MenuItem exportDataFileButton;
    public MenuItem exportBitmapButton;
    public MenuItem importDataFileButton;
    public MenuItem importBitmapButton;
    public AnchorPane anchorPane;
    public TextField probabilityOfChangeField;
    public Button startSelectingImmutablePhasesButton;
    public TextField numberOfGrainsInSubStructuresField;
    public Button addSubStructuresButton;
    public Slider thicknessSlider;
    public Button showHideAllBoundariesButton;
    public Button showBoundariesOfParticularGrainButton;
    public Button clearSpaceButton;
    public RadioButton curvatureTypeOfGrowth;
    public RadioButton simpleTypeOfGrowth;
    GrainMap grainMap;
    EventHandler<MouseEvent> selectImmutablePhasesEventHandler;
    EventHandler<MouseEvent> selectParticularGrainEventHandler;
    boolean clearedSpace;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setVisible(true);

        selectImmutablePhasesEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                CanvasPrinter canvasPrinter = CanvasPrinter.getInstance();

                if (!SubPhase.hasInstance()) {
                    int phaseToChange = canvasPrinter.getCellIdByCoordinates(x, y);

                    //changed all id of clicked phase to unmutable phase
                    grainMap.changePhaseToImmutable(phaseToChange);

                } else
                {
                    Cell cell = canvasPrinter.getCellByCoordinates(x, y);

                    SubPhase subPhase = SubPhase.getInstance();
                    subPhase.divideIntoRegions();
                    subPhase.changeRegionToImmutable(subPhase.getSubPhaseRegionByCell(cell));

                }
                canvasPrinter.generateView();
            }
        };

        selectParticularGrainEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                CanvasPrinter canvasPrinter = CanvasPrinter.getInstance();
                Cell cell = canvasPrinter.getCellByCoordinates(x, y);

                SubPhase subPhase = SubPhase.getInstance();
                subPhase.divideIntoRegions();
                //Boundaries.clear();
                Boundaries.getInstance().drawBoundaries(Math.round((int) thicknessSlider.getValue()), subPhase.getSubPhaseRegionByCell(cell));

                canvasPrinter.generateView();
            }
        };

    }

    public void startSimulation() {
        softClearData();
        loadAppConfiguration();

        this.grainMap = new GrainMap(AppConfiguration.getInstance());

        CanvasPrinter canvasPrinter = CanvasPrinter.getInstance(this.canvas, AppConfiguration.getInstance(), this.grainMap);
        canvasPrinter.generateView();

/*
        new Thread(() -> {
            while (grainMap.hasEmptyCells()) {
                grainMap.nextStep();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(canvasPrinter::generateView);
            }
        }).start();
*/

        Executors.newFixedThreadPool(1).execute(() ->{
            while (grainMap.hasEmptyCells()) {
                grainMap.nextStep();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(canvasPrinter::generateView);
            }
        });
    }

    //move loadLogicToAppConf
    public void loadAppConfiguration() {
        int numberOfInitialGrains = Integer.parseInt(numberOfGrainsField.getText());
        AppConfiguration.getInstance().setNumberOfInitialGrains(numberOfInitialGrains);

        int dimensionX = Integer.parseInt(dimensionXField.getText());
        AppConfiguration.getInstance().setNumberOfGrainsAtX(dimensionX);

        int dimensionY = Integer.parseInt(dimensionYField.getText());
        AppConfiguration.getInstance().setNumberOfGrainsAtY(dimensionY);

        if (periodicRadioButton.isSelected()) {
            AppConfiguration.getInstance().setNeighbourType("periodic");
        } else if(absorbingRadioButton.isSelected()) {
            AppConfiguration.getInstance().setNeighbourType("absorbing");
        }

        AppConfiguration.getInstance().setNumberOfInclusions(Integer.parseInt(numberOfInclusionsField.getText()));
        AppConfiguration.getInstance().setSizeOfInclusion(Integer.parseInt(sizeOfInclusionsField.getText()));

        if (circleInclusionRadioButton.isSelected()) {
            AppConfiguration.getInstance().setTypeOfInclusion("circle");
        } else if (squareInclusionRadioButton.isSelected()) {
            AppConfiguration.getInstance().setTypeOfInclusion("square");
        }

        if(startWithInclusionsCheckBox.isSelected()) {
            AppConfiguration.getInstance().setStartWithInclusions(true);
        } else {
            AppConfiguration.getInstance().setStartWithInclusions(false);
        }

        int probability = Integer.parseInt(probabilityOfChangeField.getText());
        AppConfiguration.getInstance().setProbabilityOfChange(probability);

        int numberOfGrainsInSubPhases = Integer.parseInt(numberOfGrainsInSubStructuresField.getText());
        AppConfiguration.getInstance().setNumberOfGrainsInSubPhases(numberOfGrainsInSubPhases);

        if (curvatureTypeOfGrowth.isSelected()) {
            AppConfiguration.getInstance().setTypeOfGrowth("curvatureTypeOfGrowth");
        } else if (simpleTypeOfGrowth.isSelected()) {
            AppConfiguration.getInstance().setTypeOfGrowth("simpleTypeOfGrowth");
        }


    }

    public void addInclusionsToExistingCellBoard() {
        loadAppConfiguration();

        if (this.grainMap != null) { //do sth

            Inclusion inclusion = new Inclusion(AppConfiguration.getInstance(), grainMap.currentStep, grainMap.hasEmptyCells());
            inclusion.add();
            CanvasPrinter.getInstance(this.canvas, AppConfiguration.getInstance(), this.grainMap).generateView();

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
        AppConfiguration.getInstance().setNumberOfInclusions(0);
        CanvasPrinter.getInstance(this.canvas, AppConfiguration.getInstance(), this.grainMap).clear();
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        SubPhase.clear();
        Boundaries.clear();
        clearedSpace = false;
    }

    public void exportDataFile() {
        File file = new File("simulations/simulation" + LocalTime.now().toString().replace(":", ".") + ".txt");
        AppConfiguration appConfiguration = AppConfiguration.getInstance();

        try(PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(appConfiguration.getNumberOfGrainsAtX() + " ");
            printWriter.print(appConfiguration.getNumberOfGrainsAtY() + " ");
            printWriter.print(appConfiguration.getNumberOfInitialGrains() + "\n");

            for (int i = 0; i < appConfiguration.getNumberOfGrainsAtX(); i++) {
                for (int j = 0; j < appConfiguration.getNumberOfGrainsAtY(); j++) {
                    printWriter.print(grainMap.getCurrentStep()[i][j].getX() + " " + grainMap.getCurrentStep()[i][j].getY() + " " + grainMap.getCurrentStep()[i][j].getId());
                    printWriter.print("\n");
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }

    public void exportBitmap() {
        AppConfiguration appConfiguration = AppConfiguration.getInstance();
        final BufferedImage bmp = new BufferedImage(appConfiguration.getNumberOfGrainsAtX(), appConfiguration.getNumberOfGrainsAtY(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < appConfiguration.getNumberOfGrainsAtX(); i++) {
            for (int j = 0; j < appConfiguration.getNumberOfGrainsAtY(); j++) {

                int r, g, b;
                if (grainMap.currentStep[i][j].getId() == -1) { //empty
                    r = 255;
                    g = 255;
                    b = 255;
                } else if (grainMap.currentStep[i][j].getId() == -2) {  //inclusion
                    r = 0;
                    g = 0;
                    b = 0;
                } else if (grainMap.currentStep[i][j].getId() == -3) {
                    r = 255;
                    g = 0;
                    b = 255;
                } else {
                     r = CanvasPrinter.getInstance(canvas, appConfiguration, grainMap).getCellsRGB()[grainMap.currentStep[i][j].getId()][0];
                     g = CanvasPrinter.getInstance(canvas, appConfiguration, grainMap).getCellsRGB()[grainMap.currentStep[i][j].getId()][1];
                     b = CanvasPrinter.getInstance(canvas, appConfiguration, grainMap).getCellsRGB()[grainMap.currentStep[i][j].getId()][2];
                }
                Color color = new Color(r, g, b);

                bmp.setRGB(i, j, color.getRGB());
            }
        }

        File file = new File("simulations/simulation" + LocalTime.now().toString().replace(":", ".") + ".bmp");
        try {
            ImageIO.write(bmp, "bmp", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importDataFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("simulations"));
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showOpenDialog((anchorPane.getScene().getWindow()));

        softClearData();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String firstLine = bufferedReader.readLine();
            String[] initialValues = firstLine.split(" ");
            AppConfiguration.getInstance().setNumberOfGrainsAtX(Integer.parseInt(initialValues[0]));
            AppConfiguration.getInstance().setNumberOfGrainsAtY(Integer.parseInt(initialValues[1]));
            AppConfiguration.getInstance().setNumberOfInitialGrains(Integer.parseInt(initialValues[2]));
            grainMap = new GrainMap(AppConfiguration.getInstance());


            for (int i = 0; i < AppConfiguration.getInstance().getNumberOfGrainsAtX(); i++) {
                for (int j = 0; j < AppConfiguration.getInstance().getNumberOfGrainsAtY(); j++) {

                    String line = bufferedReader.readLine();
                    String[] lineValues = line.split(" ");

                    int x = Integer.parseInt(lineValues[0]);
                    int y = Integer.parseInt(lineValues[1]);
                    int id = Integer.parseInt(lineValues[2]);
                    //System.out.printf("read line: %s %s %s\n", x,y, id);

                    grainMap.currentStep[i][j] = new Cell(x ,y ,id);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        CanvasPrinter.getInstance(canvas, AppConfiguration.getInstance(), grainMap).generateView();

    }

    public void importBitmap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("simulations"));
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showOpenDialog((anchorPane.getScene().getWindow()));
        softClearData();

        try {
            final BufferedImage bmp = ImageIO.read(file);
            int sizeX = bmp.getWidth();
            int sizeY = bmp.getHeight();
            int numOfInitialGrains = 0;
            //HashMap<Integer, Integer> grainId_colorHashMap = new HashMap<Integer, Integer>();
            List<Color> listOfCountedColors = new ArrayList<Color>();

            Color blackColor = new Color(0, 0, 0);
            Color whiteColor = new Color(255, 255, 255);
            Color magentaColor = new Color(255, 0, 255);
            //new ColorModel().
            //count initial phases
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {

                    Color currentColor = new Color(bmp.getRGB(i, j));
                    if (!currentColor.equals(blackColor) && !currentColor.equals(whiteColor) && !listOfCountedColors.contains(currentColor) & !currentColor.equals(magentaColor)) {
                        listOfCountedColors.add(currentColor);
                        numOfInitialGrains++;
                    }
                }
            }
            AppConfiguration.getInstance().setNumberOfGrainsAtX(sizeX);
            AppConfiguration.getInstance().setNumberOfGrainsAtY(sizeY);
            AppConfiguration.getInstance().setNumberOfInitialGrains(numOfInitialGrains);

            grainMap = new GrainMap(AppConfiguration.getInstance());

            //find id based on phases, set id of cells, and generate view
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {

                    Color currentColor = new Color(bmp.getRGB(i, j));
                    if (currentColor.equals(blackColor)) {   //inclusion
                        grainMap.currentStep[i][j] = new Cell(i, j, -2);
                    } else if (currentColor.equals(whiteColor)) {    //empty cell
                        grainMap.currentStep[i][j] = new Cell(i, j, -1);
                    } else if (currentColor.equals(magentaColor)) {
                        grainMap.currentStep[i][j] = new Cell(i, j, -3);
                    }
                    else {
                        grainMap.currentStep[i][j] = new Cell(i, j, listOfCountedColors.indexOf(currentColor));
                    }

                }
            }

            int[][] rgbToReturn = new int[listOfCountedColors.size()][1];
            for (int i = 0; i < listOfCountedColors.size(); i++) {
                int r = listOfCountedColors.get(i).getRed();
                int g = listOfCountedColors.get(i).getGreen();
                int b = listOfCountedColors.get(i).getBlue();
                rgbToReturn[i] = new int[]{r, g, b};
            }

            CanvasPrinter canvasPrinter = CanvasPrinter.getInstance(canvas, AppConfiguration.getInstance(), grainMap);
            canvasPrinter.setCellsRGB(rgbToReturn);
            canvasPrinter.generateView();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSelectingImmutablePhases() {
        canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, selectParticularGrainEventHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, selectImmutablePhasesEventHandler);
    }

    public void addSubStructures() {

        loadAppConfiguration();
        SubPhase subPhase = SubPhase.getInstance();
        subPhase.divideIntoRegions();
        subPhase.generateNewGrains();

        CanvasPrinter.getInstance().generateView();

        Executors.newFixedThreadPool(4).execute(() ->{
            while (subPhase.isSubStructureIncomplete()) {
                subPhase.nextSubPhaseStep();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(CanvasPrinter.getInstance()::generateView);
            }
        });

    }

    public void showHideAllBoundaries() {
        Boundaries.getInstance().drawBoundaries(Math.round((int) thicknessSlider.getValue()));

        if(clearedSpace) {
            clearSpace();
        }

        CanvasPrinter.getInstance().generateView();
    }

    public void showBoundariesOfParticularGrain() {
        canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, selectImmutablePhasesEventHandler);

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, selectParticularGrainEventHandler);
    }

    public void clearSpace() {
        clearedSpace = true;
        Boundaries.getInstance().clearAllGrains();
        CanvasPrinter.getInstance().generateView();
    }
}


