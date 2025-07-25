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
import testingMethod.Concolic4ITP;
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
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Concolic4ITPController implements Initializable {

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
    private Button btnRunConcolic4ITP;

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
        testCaseDetailVBox.setDisable(true);
        allTestCasesCoverageLabel.setDisable(true);
        testingTimeLabel.setDisable(true);
        usedMemoryLabel.setDisable(true);
        uploadFileButton.setDisable(false);


        cboChooseCoverage.getItems().addAll("Statement coverage", "Branch coverage");
        cboChooseCoverage.setOnAction(this::selectConcolic4ITPCoverage);
        cboChooseCoverage.setDisable(true);
        btnViewReport.setDisable(true);
        btnRunConcolic4ITP.setDisable(true);

        filePreview.setText(constants.PROJECT_ROOT_DRIVE + constants.TEST_FOLDER);

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
            btnRunConcolic4ITP.setDisable(true);
            btnViewReport.setDisable(true);
        } catch (Exception e) {
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("INVALID PROJECT ZIP FILE (eg: not a zip file, project's source code contains cases we haven't handled, ...)");


            System.out.printf("uploadFileButtonClicked. Exception: %s\n", e.getMessage());
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

    private void selectConcolic4ITPCoverage(ActionEvent actionEvent) {
        String coverage = cboChooseCoverage.getValue();
        if (coverage.equals("Statement coverage")) {
            choseCoverage = Coverage.STATEMENT;
            btnRunConcolic4ITP.setDisable(false);
            btnViewReport.setDisable(false);
        } else if (coverage.equals("Branch coverage")) {
            choseCoverage = Coverage.BRANCH;
            btnRunConcolic4ITP.setDisable(false);
            btnViewReport.setDisable(false);
        } else if (coverage.equals("")) {
            // do nothing!
        } else {
            throw new RuntimeException("Invalid coverage");
        }
    }

    @FXML
    void generateButtonClicked(MouseEvent event) {
//        resetTestCaseDetailVBox();
//        resetGeneratedTestCasesInfo();
//
//        ConcolicTestResult result;
//        try {
//            result = Concolic4ITP.runFullConcolic(choseUnit.getPath(), choseUnit.getMethodName(),
//                    choseUnit.getClassName(), choseCoverage);
//        } catch (Exception e) {
//            e.printStackTrace();
//            alertLabel.setTextFill(Paint.valueOf("red"));
//            alertLabel.setText("Examined unit contains cases we haven't handle yet!");
//            return;
//        }
//
//        allTestCasesCoverageLabel.setText("   All test cases coverage: " + result.getFullCoverage() + "%");
//        allTestCasesCoverageLabel.setDisable(false);
//
//        testingTimeLabel.setText("   Testing time: " + result.getTestingTime() + "ms");
//        testingTimeLabel.setDisable(false);
//
//        usedMemoryLabel.setText("   Used memory: " + result.getUsedMemory() + "MB");
//        usedMemoryLabel.setDisable(false);
//
//
//        testCaseListView.getItems().addAll(result.getFullTestData());
    }

    StringBuilder importStatement = new StringBuilder();

    @FXML
    void btnRunConcolic4ITPButtonClicked(MouseEvent event) {
        resetTestCaseDetailVBox();
        resetGeneratedTestCasesInfo();

        ConcolicTestResult result;
        try {
            String javaDirPath = CloneProjectUtil.getJavaDirPath(FilePath.uploadedProjectPath);
            if (javaDirPath.equals("")) throw new RuntimeException("Invalid project");

            Concolic4ITP.runConcolic4ITPForProject(javaDirPath, choseCoverage, importStatement);
        } catch (Exception e) {
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("Examined unit contains cases we haven't handle yet!");

            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

//        ConcolicTestResult result;
//        try {
//            result = Concolic4ITP.runConcolic4ITPForProject(choseUnit.getPath(), choseUnit.getMethodName(),
//                    choseUnit.getClassName(), choseCoverage);
//        } catch (Exception e) {
//            e.printStackTrace();
//            alertLabel.setTextFill(Paint.valueOf("red"));
//            alertLabel.setText("Examined unit contains cases we haven't handle yet!");
//            return;
//        }

        alertLabel.setText("Concolic4ITP: Finish testing the selected project.");
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
    //
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
