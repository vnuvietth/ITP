package utils.common;

import java.io.FileWriter;
import java.util.ArrayList;

public class ITPUtils {

    public static void writeToFile(String data, String path, boolean append) {
        try {
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getByPassMethod()
    {

        ArrayList<String> bypassMethodList = new ArrayList<>();

        //commons-numbers-master
        bypassMethodList.add("of");
        bypassMethodList.add("left");
        bypassMethodList.add("right");
        bypassMethodList.add("updateLeft");
        bypassMethodList.add("updateRight");
        bypassMethodList.add("getDimension");
        bypassMethodList.add("toMulti");
        bypassMethodList.add("toUni");
        bypassMethodList.add("getSize");
        bypassMethodList.add("create");
        bypassMethodList.add("ofCartesian");
        bypassMethodList.add("ofCis");
        bypassMethodList.add("getReal");
        bypassMethodList.add("getImaginary");
        bypassMethodList.add("conj");
        bypassMethodList.add("negate");
        bypassMethodList.add("add");
        bypassMethodList.add("addImaginary");
        bypassMethodList.add("subtract");
        bypassMethodList.add("subtractImaginary");
        bypassMethodList.add("subtractFrom");
        bypassMethodList.add("subtractFromImaginary");
        bypassMethodList.add("multiply");
        bypassMethodList.add("multiplyImaginary");
        bypassMethodList.add("divide");
        bypassMethodList.add("divideImaginary");
        bypassMethodList.add("pow");
        bypassMethodList.add("pow");
        bypassMethodList.add("hi");
        bypassMethodList.add("lo");
        bypassMethodList.add("doubleValue");
        bypassMethodList.add("floatValue");
        bypassMethodList.add("negate");
        bypassMethodList.add("zero");
        bypassMethodList.add("isZero");
        bypassMethodList.add("one");
        bypassMethodList.add("isOne");
        bypassMethodList.add("equals");
        bypassMethodList.add("equals");
        bypassMethodList.add("create");
        bypassMethodList.add("get");
        bypassMethodList.add("one");
        bypassMethodList.add("zero");
        bypassMethodList.add("get");
        bypassMethodList.add("one");
        bypassMethodList.add("zero");
        bypassMethodList.add("negate");
        bypassMethodList.add("reciprocal");
        bypassMethodList.add("multiply");
        bypassMethodList.add("pow");
        bypassMethodList.add("zero");
        bypassMethodList.add("isZero");
        bypassMethodList.add("one");
        bypassMethodList.add("isOne");
        bypassMethodList.add("get");
        bypassMethodList.add("one");
        bypassMethodList.add("zero");
        bypassMethodList.add("getNumerator");
        bypassMethodList.add("getDenominator");
        bypassMethodList.add("getNumerator");
        bypassMethodList.add("getDenominator");
        bypassMethodList.add("value");
        bypassMethodList.add("g");
        bypassMethodList.add("twoProductLow");
        bypassMethodList.add("twoSquareLow");
        bypassMethodList.add("highPart");

        //Algorithms-master
        bypassMethodList.add("getAt");
        bypassMethodList.add("size");
        bypassMethodList.add("calculate1");
        bypassMethodList.add("divideIterative");
        bypassMethodList.add("merge");


        //java-algorithms-implementation-master
        bypassMethodList.add("addEdge");
        bypassMethodList.add("getNumberOfCoprimes");
        bypassMethodList.add("divisionUsingLoop");
        bypassMethodList.add("gcdUsingEuclides");
        bypassMethodList.add("modularAbs");
        bypassMethodList.add("multiplyUsingLoopWithIntegerInput");
        bypassMethodList.add("isPrime");
        bypassMethodList.add("toString");
        bypassMethodList.add("polar");
        bypassMethodList.add("powerOfTwoUsingBits");
        bypassMethodList.add("fibonacciSequenceUsingLoop");
        bypassMethodList.add("fibonacciSequenceUsingBinetsFormula");
        bypassMethodList.add("divisionUsingShift");



        return bypassMethodList;
    }

    public static ArrayList<String> getByPassFiles() {
        ArrayList<String> bypassFilesList = new ArrayList<>();

//        bypassFilesList.add("RedisOutputStream.java");

        return bypassFilesList;
    }

}
