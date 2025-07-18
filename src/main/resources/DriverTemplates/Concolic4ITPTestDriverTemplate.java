import java.io.FileWriter;
//import ClonedProjects.*;

public class concolic4ITPTestDriver {

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

    public static void main(String[] args) {

        try
        {
            //Setup start
            //TODO: Initialize environment and parameter value
            //Setup end
            //TODO: Call test function
            //Tear down start
            //TODO: Clean up environment: freeing memory, file,...
            //Tear down end
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("concolic4ITPTestDriver starts...");

        writeDataToFile("", PATH_RESULT, false);

        long startRunTestTime = System.nanoTime();

        //Start function calling

        //Object output = getNthPowerSum(8, 8);

        %%UNIT_CALLING_PLACEHOLDER%%

        //Start function calling

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, PATH_RESULT, true);

        System.out.println("concolic4ITPTestDriver ends...");

    }

}
