# ITP: An Effective Concolic-Based Method for Generating Test Data in Unit Testing of Java Projects
## USER MANUAL
### Required environment
- Our implementation has been tested on the Windows, Processor	12th Gen Intel(R) Core(TM) i7-1255U, 1700 Mhz, 10 Core(s), 12 Logical Processor(s), 16GBs RAM memory.
- IntelliJ IDEA, which is available [here](https://www.jetbrains.com/idea/download/)
- [Java 11 SDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Download JavaFX SDK (Version: 17.0.10, Type: SDK)](https://gluonhq.com/products/javafx/)
- [z3 solver (Version >= 4.12.2)](https://github.com/Z3Prover/z3)
  - Extract the downloaded file
  - Add the path to the `bin` folder to the system environment variable `PATH` or `Path`
## How to Run?
1. Clone and Open the project in IntelliJ IDEA
2. Add JavaFX SDK to the project (File -> Project Structure -> Libraries -> Add -> Java -> Select the lib folder of JavaFX SDK)
3. Edit Configuration 
   - New Configuration -> Application
   - Main class: `gui.Main`
   - Modify options -> Add VM options: `--module-path <path-to-lib-folder-of-javafx-sdk> --add-modules javafx.controls,javafx.fxml` (TODO: Change the path to your JavaFX SDK lib folder)
4. Run the project, the tool will ask you to choose between two options:
   - **Option 1**: Concolic4ITP. This function implements the Concolic testing method.
   - **Option 2**: ITP4V0. This function implements the ITPv0 testing method.
   - **Option 3**: ITP. This function implements the ITP testing method.
![image](./src/main/resources/img/choose-method.png)
5. After choosing a method, the main screen looks like this:
!Concolic testing method: [image](./src/main/resources/img/Concolic.png)
!ITPv0 testing method: [image](./src/main/resources/img/ITPv0.png)
!ITP testing method: [image](./src/main/resources/img/ITP.png)
6. Upload the project by clicking the `Choose file` button, then select the project folder.\
Note:
   - We have provided some sample projects in the `sample` folder.
   - The project should be compressed into a zip file.
   - The project should contain the `java` folder, which contains the source code.
7. After the project is uploaded, start testing by clicking on the button "Start Concolic testing", "Start ITPv0 testing", or "Start ITP testing" for each chosen testing method.
8. Click the `View test report` button to check the test report when testing process finish. This will open the report using Notepad. 

