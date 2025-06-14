import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import ClonedProjects.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ITP_V0_TestDriver {

    public static final String PATH_RESULT = "%%EXECUTION_RESULT_PATH_PLACEHOLDER%%";
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
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //End utilities

    //Start testing method

    %%INSTRUMENTED_TESTING_UNIT_PLACEHOLDER%%

    //End testing method

    public static void ITP_TEST_EXECUTION() {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("E:\\IdeaProjects\\testDriver\\TestData.json"));

        String fileName = (String) jsonObject.get("fileName");
        String functionName = (String) jsonObject.get("functionName");

%%UNIT_CALLING_BLOCK_PLACEHOLDER%%

    }

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Concolic4ITPTestDriver starts...");

        writeDataToFile("", PATH_RESULT, false);

        long startRunTestTime = System.nanoTime();

        //Start test data file reading
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("%%TEST_DATA_FILE_PATH%%"));
//
//        String param0 = (String) jsonObject.get("n");
//        System.out.println("n = "  + param0);
//        String param1 = (String) jsonObject.get("p");
//        System.out.println("p = "  + param1);

        //End test data file reading

        //Start function calling

//        %%UNIT_CALLING_PLACEHOLDER%%

        ITP_TEST_EXECUTION();

        System.out.println("output = " + output);

        //Start function calling

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, PATH_RESULT, true);

        System.out.println("Concolic4ITPTestDriver ends...");

    }

}
