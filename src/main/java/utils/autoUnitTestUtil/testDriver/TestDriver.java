package utils.autoUnitTestUtil.testDriver;
import java.io.FileWriter;

public class TestDriver {

    private static boolean mark(String statement, boolean isTrueCondition, boolean isFalseCondition) {
       StringBuilder markResult = new StringBuilder();

       markResult.append(statement).append("===");

       markResult.append(isTrueCondition).append("===");

       markResult.append(isFalseCondition).append("---end---");

       writeDataToFile(markResult.toString(), "src/main/java/utils/autoUnitTestUtil/concreteExecuteResult.txt", true);

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

    public static void main(String[] args) {
        writeDataToFile("", "core-engine/cfg/src/main/java/data/testDriverData/runTestDriverData.txt", false);

        long startRunTestTime = System.nanoTime();

        Object output = getNthPowerSum(8, 8        );
        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, "src/main/java/utils/autoUnitTestUtil/concreteExecuteResult.txt", true);

    }
}