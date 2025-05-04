import java.io.FileWriter;
//import ClonedProjects.*;

public class TestDriver {

    public static final String PATH_RESULT = "concreteExecuteResult.txt";
    public static final String PATH_DATA = "runTestDriverData.txt";

    //Start utilities
    private static boolean mark(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        StringBuilder markResult = new StringBuilder();

        markResult.append(statement).append("===");

        markResult.append(isTrueCondition).append("===");

        markResult.append(isFalseCondition).append("---end---");

        writeDataToFile(markResult.toString(), PATH_RESULT, true);

        if (!isTrueCondition && !isFalseCondition)
            return true;

        return !isFalseCondition;

    }

    private static void writeDataToFile(String data, String path, boolean append) {
        try {
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        writeDataToFile("", "core-engine/cfg/src/main/java/data/testDriverData/runTestDriverData.txt", false);

        long startRunTestTime = System.nanoTime();

//        Object output = getNthPowerSum(8, 8);
        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

//        writeDataToFile(runTestDuration + "===" + output, "src/main/java/utils/autoUnitTestUtil/concreteExecuteResult.txt", true);

    }
}