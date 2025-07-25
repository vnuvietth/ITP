import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import ClonedProjects.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class concolic4ITPTestDriver {

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
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //End utilities

    //Start testing method

    public static int getNthPowerSum(int n, int p)
    {
        mark("int sum=0;\n", false, false);
        int sum=0;
        while (((n > 0) && mark("n > 0", true, false)) || mark("n > 0", false, true)) {

            {
                mark("int temp=n % 10;\n", false, false);
                int temp=n % 10;
                mark("n/=10;\n", false, false);
                n/=10;
                mark("sum+=(int)Math.pow(temp,p);\n", false, false);
                sum+=(int)Math.pow(temp,p);
            }
        }

        mark("return sum;\n", false, false);
        return sum;
    }



    //End testing method

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Concolic4ITPTestDriver starts...");

        writeDataToFile("", PATH_RESULT, false);

        long startRunTestTime = System.nanoTime();

        //Start function calling

        JSONParser parser = new JSONParser();
//        JSONArray a = (JSONArray) parser.parse(new FileReader("C:\\Users\\HP\\IdeaProjects\\TestDriver\\src\\SampleTestDataFile.json"));

        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\Users\\HP\\IdeaProjects\\TestDriver\\src\\TestData.json"));

        String name = (String) jsonObject.get("n");
        System.out.println(name);

        String city = (String) jsonObject.get("p");
        System.out.println(city);

        Object output = getNthPowerSum(8, 8);

        System.out.println("output = " + output);

//        %%UNIT_CALLING_PLACEHOLDER%%

        //Start function calling

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

//        writeDataToFile(runTestDuration + "===" + output, PATH_RESULT, true);

        System.out.println("Concolic4ITPTestDriver ends...");

    }

}
