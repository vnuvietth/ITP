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
    public static int getNumOfDays(int month, boolean isLeapYear)
    {
        mark("int numOfDays=0;\n", false, false);
int numOfDays=0;
        mark("int i=1", false, false);
    for (int i=1; ((i < month) && mark("i < month", true, false)) || mark("i < month", false, true); mark("i++", false, false),
i++    ) {

            {
            if (((i == 2) && mark("i == 2", true, false)) || mark("i == 2", false, true))
    {
            {
            if (((isLeapYear) && mark("isLeapYear", true, false)) || mark("isLeapYear", false, true))
    {
            {
        mark("numOfDays+=daysInMonth.get(i - 1) + 1;\n", false, false);
numOfDays+=daysInMonth.get(i - 1) + 1;
    }
    }
    else {
mark("numOfDays+=daysInMonth.get(i - 1);\n", false, false);
numOfDays+=daysInMonth.get(i - 1);
}
    }
    }
    else {
    {
        mark("numOfDays+=daysInMonth.get(i - 1);\n", false, false);
numOfDays+=daysInMonth.get(i - 1);
    }
}
    }
    }

        mark("return numOfDays;\n", false, false);
return numOfDays;
    }

    public static void main(String[] args) {
        writeDataToFile("", "core-engine/cfg/src/main/java/data/testDriverData/runTestDriverData.txt", false);

        long startRunTestTime = System.nanoTime();

        Object output = getNumOfDays(8, false        );
        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, "src/main/java/utils/autoUnitTestUtil/concreteExecuteResult.txt", true);

    }
}