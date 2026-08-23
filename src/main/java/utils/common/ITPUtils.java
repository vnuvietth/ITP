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













//        //Refactored-TheAlgorithms-Java
//        bypassMethodList.add("main");
//        bypassMethodList.add("indexOfRightMostSetBit");
//        bypassMethodList.add("bpR");
//        bypassMethodList.add("binomialCoefficient");
//        bypassMethodList.add("leonardoNumber");
//        bypassMethodList.add("lucasSeries");
//        bypassMethodList.add("pascal");
//        bypassMethodList.add("TrinomialValue");
//        bypassMethodList.add("printTrinomial");
//        bypassMethodList.add("square_Root");
//        bypassMethodList.add("getImage");
//        bypassMethodList.add("fermatPrimeChecking");
//        bypassMethodList.add("minTrials");
//        bypassMethodList.add("perimeterIrregularPolygon");
//        bypassMethodList.add("calculatePi");
//        bypassMethodList.add("isOperator");
//
//        bypassMethodList.add("compareTo");
//
//        // Not processed data types in Refactored-TheAlgorithms-Java
//        bypassMethodList.add("bruteforce");
//        bypassMethodList.add("valOfChar");
//        bypassMethodList.add("nthManShanksPrime");
//        bypassMethodList.add("uniquePaths2");
//        bypassMethodList.add("getMaxValue");
//        bypassMethodList.add("surfaceAreaCube");
//        bypassMethodList.add("surfaceAreaSphere");
//        bypassMethodList.add("surfaceAreaRectangle");
//        bypassMethodList.add("surfaceAreaCylinder");
//        bypassMethodList.add("surfaceAreaSquare");
//        bypassMethodList.add("surfaceAreaTriangle");
//        bypassMethodList.add("surfaceAreaParallelogram");
//        bypassMethodList.add("surfaceAreaTrapezium");
//        bypassMethodList.add("surfaceAreaCircle");
//        bypassMethodList.add("surfaceAreaHemisphere");
//        bypassMethodList.add("surfaceAreaCone");
//        bypassMethodList.add("binPow");
//        bypassMethodList.add("ceil");
//        bypassMethodList.add("factorial");
//        bypassMethodList.add("combinationsOptimized");
//        bypassMethodList.add("floor");
//        bypassMethodList.add("isHarshad");
//        bypassMethodList.add("lcm");
//        bypassMethodList.add("millerRabin");
//        bypassMethodList.add("isPrime");
//        bypassMethodList.add("sumOfSeries");
//        bypassMethodList.add("isPaired");
//        bypassMethodList.add("appendCount");
//        bypassMethodList.add("isDudeney");
//        bypassMethodList.add("getRandomScores");
//        bypassMethodList.add("isPalindrome");
//
//        //For LeetCode solution
//        bypassMethodList.add("countTriples");
//        bypassMethodList.add("generate");
//        bypassMethodList.add("fizzBuzz");
//        bypassMethodList.add("getRow");
//        bypassMethodList.add("divisorGame");
//        bypassMethodList.add("numOfArrays");
//        bypassMethodList.add("kInversePairs");
//        bypassMethodList.add("countAndSay");
//        bypassMethodList.add("getMoneyAmount");
//        bypassMethodList.add("knightDialer");
//        bypassMethodList.add("integerBreak");
//        bypassMethodList.add("knightProbability");
//        bypassMethodList.add("findContestMatch");
//        bypassMethodList.add("generateMatrix");
//        bypassMethodList.add("numTrees");
//        bypassMethodList.add("lexicalOrder");
//        bypassMethodList.add("toHex");
//        bypassMethodList.add("countEven");
//        bypassMethodList.add("maxProduct");
//        bypassMethodList.add("rangeBitwiseAnd");
//        bypassMethodList.add("countGoodStrings");
//        bypassMethodList.add("new21Game");
//        bypassMethodList.add("numOfBurgers");
//        bypassMethodList.add("smallestNumber");
//        bypassMethodList.add("sumZero");
//        bypassMethodList.add("minCuttingCost");
//        bypassMethodList.add("isUgly");
//        bypassMethodList.add("maxBottlesDrunk");
//        bypassMethodList.add("constructRectangle");
//        bypassMethodList.add("isThree");
//
//        bypassMethodList.add("trailingZeroes");
//        bypassMethodList.add("numberOfMatches");
//        bypassMethodList.add("sumBase");
//        bypassMethodList.add("numWaterBottles");
//        bypassMethodList.add("arrangeCoins");
//        bypassMethodList.add("minBitFlips");
//        bypassMethodList.add("isPerfectSquare");
//        bypassMethodList.add("brokenCalc");
//        bypassMethodList.add("minMoves");
//        bypassMethodList.add("mirrorReflection");
//        bypassMethodList.add("kthFactor");
//        bypassMethodList.add("alternateDigitSum");
//        bypassMethodList.add("smallestRepunitDivByK");
//        bypassMethodList.add("confusingNumber");
//
//        //Not processed data types For LeetCode solution
//        bypassMethodList.add("hasAlternatingBits");
//        bypassMethodList.add("categorizeBox");
//        bypassMethodList.add("countOperations");
//        bypassMethodList.add("countSymmetricIntegers");
//        bypassMethodList.add("divideString");
//        bypassMethodList.add("fib");
//        bypassMethodList.add("pivotInteger");
//        bypassMethodList.add("getMaximumGenerated");
//        bypassMethodList.add("hammingDistance");
//        bypassMethodList.add("countBalls");
//        bypassMethodList.add("canBeTypedWords");
//        bypassMethodList.add("tribonacci");
//        bypassMethodList.add("passThePillow");
//        bypassMethodList.add("powerfulIntegers");
//        bypassMethodList.add("isPowerOfTwo");
//        bypassMethodList.add("selfDividingNumbers");
//        bypassMethodList.add("mySqrt");
//        bypassMethodList.add("findKthNumber");
//        bypassMethodList.add("withdraw");
//        bypassMethodList.add("divide");
//        bypassMethodList.add("maxA");
//        bypassMethodList.add("primePalindrome");
//        bypassMethodList.add("reverse");
//        bypassMethodList.add("primePalindrome");
//        bypassMethodList.add("isStrictlyPalindromic");
//        bypassMethodList.add("judgeSquareSum");
//        bypassMethodList.add("nthUglyNumber");
//        bypassMethodList.add("primePalindrome");
//        bypassMethodList.add("primePalindrome");
//        bypassMethodList.add("coloredCells");
//        bypassMethodList.add("numberOfChild");
//        bypassMethodList.add("maxContainers");
//        bypassMethodList.add("numberOfDays");
//        bypassMethodList.add("concatenatedBinary");
//
//        //java-algorithms-implementation-master
//        bypassMethodList.add("polar");
//
//        //Not processed data types For java-algorithms-implementation-master
//        bypassMethodList.add("getNumberOfCoprimes");
//        bypassMethodList.add("gcdUsingEuclides");
//        bypassMethodList.add("powerOfTwoUsingBits");
//        bypassMethodList.add("fibonacciSequenceUsingLoop");
//        bypassMethodList.add("fibonacciSequenceUsingBinetsFormula");
//        bypassMethodList.add("powerOfTwoUsingLoop");
//
//
//        //JAVA-Challenges-main
//        bypassMethodList.add("calculateGCD");
//
//        //Not processed data types For JAVA-Challenges-main
//        bypassMethodList.add("numberOfUniquePaths");
//        bypassMethodList.add("findSteps");
//        bypassMethodList.add("parenthesesCorrespondantes");
//
////        Algorithms-master
////        bypassMethodList.add("calculate");
//        bypassMethodList.add("equalsWithRelativeTolerance");
//        bypassMethodList.add("evaluate");
//
//        //Algorithms-master
//        bypassMethodList.add("value");


        return bypassMethodList;
    }

    public static ArrayList<String> getByPassFiles() {
        ArrayList<String> bypassFilesList = new ArrayList<>();

//        bypassFilesList.add("RedisOutputStream.java");

        return bypassFilesList;
    }

}
