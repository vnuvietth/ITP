import java.io.FileWriter;
//import ClonedProjects.*;

public class TestDriverTemplate {

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

    public static void main(String[] args) {

        writeDataToFile("", PATH_DATA, false);

        long startRunTestTime = System.nanoTime();

        //Start function calling

        //Object output = getNthPowerSum(8, 8);

                Object output = getNthPowerSum(8, 8        );


        //Start function calling

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, PATH_RESULT, true);

    }

}
