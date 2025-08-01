package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.FilePath;
import testingMethod.ITP4Java;
import utils.common.constants;
import utils.autoUnitTestUtil.concolicResult.ConcolicParameterData;
import utils.autoUnitTestUtil.concolicResult.ConcolicTestData;
import utils.autoUnitTestUtil.concolicResult.ConcolicTestResult;
import utils.cloneProjectUtil.CloneProjectUtil;
import utils.cloneProjectUtil.projectTreeObjects.Folder;
import utils.cloneProjectUtil.projectTreeObjects.JavaFile;
import utils.cloneProjectUtil.projectTreeObjects.ProjectTreeObject;
import utils.cloneProjectUtil.projectTreeObjects.Unit;
import utils.uploadUtil.ConcolicUploadUtil;
import utils.uploadUtil.NTDUploadUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class ITP4JavaController implements Initializable {

    private FileChooser fileChooser = new FileChooser();
    private File choseFile;
    private Unit choseUnit;
    private Coverage choseCoverage;

    public enum Coverage {
        STATEMENT,
        BRANCH
    }

    @FXML
    private Label filePreview;

    @FXML
    private Button uploadFileButton;

    @FXML
    private TreeView<ProjectTreeObject> projectTreeView;

    @FXML
    private Button generateButton;

    @FXML
    private ChoiceBox<String> coverageChoiceBox;

    @FXML
    private ChoiceBox<String> cboChooseCoverage;

    @FXML
    private Button btnViewReport;

    @FXML
    private Button btnStartITPTesting;

    @FXML
    private ListView<ConcolicTestData> testCaseListView;

    @FXML
    private VBox testCaseDetailVBox;

    @FXML
    private Label executeTimeLabel;

    @FXML
    private Label sourceCodeCoverageLabel;

    @FXML
    private ListView<ConcolicParameterData> generatedTestDataListView;

    @FXML
    private Label outputLabel;

    @FXML
    private Label requireCoverageLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label testCaseIDLabel;

    @FXML
    private Label alertLabel;

    @FXML
    private Label allTestCasesCoverageLabel;

    @FXML
    private Label testingTimeLabel;

    @FXML
    private Label usedMemoryLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uploadFileButton.setDisable(true);
        generateButton.setDisable(true);
        coverageChoiceBox.setDisable(true);
        coverageChoiceBox.getItems().addAll("Statement coverage", "Branch coverage");
        coverageChoiceBox.setOnAction(this::selectCoverage);

        cboChooseCoverage.getItems().addAll("Statement coverage", "Branch coverage");
        cboChooseCoverage.setOnAction(this::selectITPCoverage);
        cboChooseCoverage.setDisable(true);
        btnViewReport.setDisable(true);
        btnStartITPTesting.setDisable(true);

        testCaseDetailVBox.setDisable(true);
        allTestCasesCoverageLabel.setDisable(true);
        testingTimeLabel.setDisable(true);
        usedMemoryLabel.setDisable(true);

        filePreview.setText(constants.PROJECT_ROOT_DRIVE + constants.TEST_FOLDER);

        uploadFileButton.setDisable(false);

        testCaseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConcolicTestData>() {
            @Override
            public void changed(ObservableValue<? extends ConcolicTestData> observableValue, ConcolicTestData concolicTestData, ConcolicTestData t1) {
                ConcolicTestData testData = testCaseListView.getSelectionModel().getSelectedItem();
                if (testData != null) {
                    setTestCaseDetail(testData);
                    testCaseDetailVBox.setDisable(false);
                }
            }
        });
    }

    @FXML
    void chooseFileButtonClicked(MouseEvent event) {
        choseFile = fileChooser.showOpenDialog(new Stage());
        if (choseFile != null) {
            filePreview.setText(choseFile.getAbsolutePath());
            uploadFileButton.setDisable(false);
        }
    }


    StringBuilder importStatement = new StringBuilder();

    @FXML
    void uploadFileButtonClicked(MouseEvent event) {
        reset();
        try {
            long startTime = System.nanoTime();

            CloneProjectUtil.deleteFilesInDirectory(FilePath.uploadedProjectPath);
//            NTDUploadUtil.javaUnzipFile(choseFile.getPath(), FilePath.uploadedProjectPath);
            NTDUploadUtil.javaUnzipFile(filePreview.getText(), FilePath.uploadedProjectPath);

            String javaDirPath = CloneProjectUtil.getJavaDirPath(FilePath.uploadedProjectPath);
            if (javaDirPath.equals("")) throw new RuntimeException("Invalid project");

            writeDataToFile("", constants.ITP_EXCEPTION_UNIT_FILEPATH, false);

            Folder folder1 = CloneProjectUtil.cloneProject4ITP(javaDirPath, FilePath.clonedProjectPath, importStatement);

            Folder folder = ConcolicUploadUtil.createProjectTree(javaDirPath);

            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            duration = (double) Math.round(duration * 100) / 100;

            TreeItem<ProjectTreeObject> rootFolder = switchToTreeItem(folder);

            projectTreeView.setRoot(rootFolder);
            alertLabel.setTextFill(Paint.valueOf("green"));
            alertLabel.setText("Upload time " + duration + "ms");

            projectTreeView.setDisable(true);

            cboChooseCoverage.setDisable(false);
            btnStartITPTesting.setDisable(true);
            btnViewReport.setDisable(true);


        } catch (Exception e) {
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("INVALID PROJECT ZIP FILE (eg: not a zip file, project's source code contains cases we haven't handled, ...)");
            e.printStackTrace();
        }
    }


    private static void writeDataToFile(String data, String path, boolean append) {
        try {
            if (!append)
            {
                Files.deleteIfExists(Paths.get(path));
            }
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void reset() {
        projectTreeView.setRoot(null);
        coverageChoiceBox.setValue("");
        coverageChoiceBox.setDisable(true);
        generateButton.setDisable(true);
        resetGeneratedTestCasesInfo();
        alertLabel.setText("");
        resetTestCaseDetailVBox();
    }

    private void resetGeneratedTestCasesInfo() {
        testCaseListView.getItems().clear();
        allTestCasesCoverageLabel.setText("   All test cases coverage:");
        allTestCasesCoverageLabel.setDisable(true);
        testingTimeLabel.setText("   Testing time:");
        testingTimeLabel.setDisable(true);
        usedMemoryLabel.setText("   Used memory:");
        usedMemoryLabel.setDisable(true);
    }

    private void resetTestCaseDetailVBox() {
        testCaseDetailVBox.setDisable(true);
        testCaseIDLabel.setText("   Test case ID:");
        sourceCodeCoverageLabel.setText("   Source code coverage:");
        requireCoverageLabel.setText("   Required coverage:");
        executeTimeLabel.setText("   Execute time:");
        outputLabel.setText("   Output:");
        statusLabel.setText("   Status:");
        generatedTestDataListView.getItems().clear();
    }

    private TreeItem<ProjectTreeObject> switchToTreeItem(ProjectTreeObject treeObject) {
        if (treeObject instanceof Folder) {
            TreeItem<ProjectTreeObject> item = new TreeItem<>(treeObject, new ImageView(new Image("\\img\\folder_icon.png")));
            List<ProjectTreeObject> children = ((Folder) treeObject).getChildren();
            for (ProjectTreeObject child : children) {
                item.getChildren().add(switchToTreeItem(child));
            }
            return item;
        } else if (treeObject instanceof JavaFile) {
            TreeItem<ProjectTreeObject> item = new TreeItem<>(treeObject, new ImageView(new Image("\\img\\java_file_icon.png")));
            List<Unit> units = ((JavaFile) treeObject).getUnits();
            for (Unit unit : units) {
                item.getChildren().add(switchToTreeItem(unit));
            }
            return item;
        } else if (treeObject instanceof Unit) {
            return new TreeItem<>(treeObject, new ImageView(new Image("\\img\\unit_icon.png")));
        } else {
            throw new RuntimeException("Invalid ProjectTreeObject");
        }
    }

    @FXML
    void selectUnit(MouseEvent event) {
        TreeItem<ProjectTreeObject> selectedItem = projectTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ProjectTreeObject treeObject = selectedItem.getValue();
            if (treeObject instanceof Unit) {
                choseUnit = (Unit) treeObject;
                coverageChoiceBox.setDisable(false);
                coverageChoiceBox.setValue("");
                generateButton.setDisable(true);
            } else {
                choseUnit = null;
                coverageChoiceBox.setDisable(true);
                coverageChoiceBox.setValue("");
                generateButton.setDisable(true);
            }
        }
    }

    private void selectCoverage(ActionEvent actionEvent) {
        generateButton.setDisable(false);

        String coverage = coverageChoiceBox.getValue();
        if (coverage.equals("Statement coverage")) {
            choseCoverage = Coverage.STATEMENT;
        } else if (coverage.equals("Branch coverage")) {
            choseCoverage = Coverage.BRANCH;
        } else if (coverage.equals("")) {
            // do nothing!
        } else {
            throw new RuntimeException("Invalid coverage");
        }
    }


    private void selectITPCoverage(ActionEvent actionEvent) {
        String coverage = cboChooseCoverage.getValue();
        if (coverage.equals("Statement coverage")) {
            choseCoverage = Coverage.STATEMENT;
            btnStartITPTesting.setDisable(false);
            btnViewReport.setDisable(false);
        } else if (coverage.equals("Branch coverage")) {
            choseCoverage = Coverage.BRANCH;
            btnStartITPTesting.setDisable(false);
            btnViewReport.setDisable(false);
        } else if (coverage.equals("")) {
            // do nothing!
        } else {
            throw new RuntimeException("Invalid coverage");
        }
    }

    @FXML
    void generateButtonClicked(MouseEvent event) {
        resetTestCaseDetailVBox();
        resetGeneratedTestCasesInfo();
        alertLabel.setText("");

        ConcolicTestResult result;
        try {

            String javaDirPath = CloneProjectUtil.getJavaDirPath(FilePath.uploadedProjectPath);
            if (javaDirPath.equals("")) throw new RuntimeException("Invalid project");

//            result = ITP4Java.runITP4Project(FilePath.uploadedProjectPath, Coverage.STATEMENT);
        } catch (Exception e) {
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("Examined unit contains cases we haven't handle yet!");
            return;
        }

//        allTestCasesCoverageLabel.setText("   All test cases coverage: " + result.getFullCoverage() + "%");
        allTestCasesCoverageLabel.setDisable(false);

//        testingTimeLabel.setText("   Testing time: " + result.getTestingTime() + "ms");
        testingTimeLabel.setDisable(false);

//        usedMemoryLabel.setText("   Used memory: " + result.getUsedMemory() + "MB");
        usedMemoryLabel.setDisable(false);


//        testCaseListView.getItems().addAll(result.getFullTestData());
    }


    @FXML
    void btnStartITPTestingButtonClicked(MouseEvent event) {
        resetTestCaseDetailVBox();
        resetGeneratedTestCasesInfo();
        alertLabel.setText("");

        ConcolicTestResult result;
        try {
            String javaDirPath = CloneProjectUtil.getJavaDirPath(FilePath.uploadedProjectPath);
            if (javaDirPath.equals("")) throw new RuntimeException("Invalid project");

            //result = ITP4Java.runITP4Project(javaDirPath, Coverage.STATEMENT, importStatement);

            result = ITP4Java.runITP4Project(javaDirPath, choseCoverage, importStatement);

            btnViewReport.setDisable(false);

        } catch (Exception e) {
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("Examined unit contains cases we haven't handle yet!");

            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        //allTestCasesCoverageLabel.setText("   All test cases coverage: " + result.getFullCoverage() + "%");

        alertLabel.setText("ITP: Finish testing the selected project.");

        allTestCasesCoverageLabel.setText("ITP: Finished testing");

        allTestCasesCoverageLabel.setDisable(false);

//        testingTimeLabel.setText("   Testing time: " + result.getTestingTime() + "ms");
        testingTimeLabel.setDisable(false);

//        usedMemoryLabel.setText("   Used memory: " + result.getUsedMemory() + "MB");
        usedMemoryLabel.setDisable(false);


//        testCaseListView.getItems().addAll(result.getFullTestData());
    }

    @FXML
    void btnViewTestReportButtonClicked(MouseEvent event) {
        try {
            // Create a File object to check existence
            File file = new File(constants.ITP_TEST_RESULT_FILEPATH);
            if (!file.exists()) {
                System.err.println("The file does not exist: " + constants.ITP_TEST_RESULT_FILEPATH);
                return;
            }

            // Use Runtime to open the file in Notepad
            Process process = Runtime.getRuntime().exec("notepad \"" + constants.ITP_TEST_RESULT_FILEPATH + "\"");
            System.out.println("File opened in Notepad.");
        } catch (IOException e) {
            System.err.println("Failed to open file in Notepad: " + e.getMessage());
        }
    }

    private void setTestCaseDetail(ConcolicTestData testData) {
        testCaseIDLabel.setText("   Test case ID: " + testData.getTestCaseID());
        sourceCodeCoverageLabel.setText("   Source code coverage: " + testData.getSourceCodeCoverage());
        requireCoverageLabel.setText("   Required coverage: " + testData.getRequiredCoverage());
        executeTimeLabel.setText("   Execute time: " + testData.getExecuteTime());
        outputLabel.setText("   Output: " + testData.getOutput());
        statusLabel.setText("   Status: " + testData.getStatus());

        generatedTestDataListView.getItems().clear();
        generatedTestDataListView.getItems().addAll(testData.getParameterDataList());
    }
}
