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
        bypassMethodList.add("checkIndex");



        //Algorithms-master
        bypassMethodList.add("getAt");
        bypassMethodList.add("size");
        bypassMethodList.add("calculate1");
        bypassMethodList.add("divideIterative");
        bypassMethodList.add("merge");
        bypassMethodList.add("calculate");


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


        //JAVA-Challenges-main
        bypassMethodList.add("fibonacciSequence");
        bypassMethodList.add("numberOfUniquePaths");
        bypassMethodList.add("parenthesesCorrespondantes");
        bypassMethodList.add("performOperation");
        bypassMethodList.add("isOperator");
        bypassMethodList.add("findSteps");


        //LeetCode-Java-Solutions-master
        bypassMethodList.add("hasAlternatingBits");
        bypassMethodList.add("categorizeBox");
        bypassMethodList.add("constructRectangle");
        bypassMethodList.add("countEven");
        bypassMethodList.add("countTriples");
        bypassMethodList.add("countSymmetricIntegers");
        bypassMethodList.add("addCar");
        bypassMethodList.add("distMoney");
        bypassMethodList.add("divideString");
        bypassMethodList.add("minCuttingCost");
        bypassMethodList.add("sumZero");
        bypassMethodList.add("pivotInteger");
        bypassMethodList.add("fizzBuzz");
        bypassMethodList.add("getMaximumGenerated");
        bypassMethodList.add("countBalls");
        bypassMethodList.add("canBeTypedWords");
        bypassMethodList.add("generate");
        bypassMethodList.add("getRow");
        bypassMethodList.add("powerfulIntegers");
        bypassMethodList.add("isPowerOfTwo");
        bypassMethodList.add("selfDividingNumbers");
        bypassMethodList.add("mySqrt");
        bypassMethodList.add("findMedian");
        bypassMethodList.add("countNumsWithPrefix");
        bypassMethodList.add("numWays");
        bypassMethodList.add("countGoodStrings");
        bypassMethodList.add("withdraw");
        bypassMethodList.add("countUnexpiredTokens");
        bypassMethodList.add("enQueue");
        bypassMethodList.add("deQueue");
        bypassMethodList.add("Front");
        bypassMethodList.add("Rear");
        bypassMethodList.add("isEmpty");
        bypassMethodList.add("isFull");
        bypassMethodList.add("popFront");
        bypassMethodList.add("popBack");
        bypassMethodList.add("check");
        bypassMethodList.add("release");
        bypassMethodList.add("isReachableAtTime");
        bypassMethodList.add("distributeCandies");
        bypassMethodList.add("consec");
        bypassMethodList.add("knightDialer");
        bypassMethodList.add("knightProbability");
        bypassMethodList.add("lexicalOrder");
        bypassMethodList.add("upload");
        bypassMethodList.add("longest");
        bypassMethodList.add("findSum");
        bypassMethodList.add("top");
        bypassMethodList.add("getMin");
        bypassMethodList.add("numOfBurgers");
        bypassMethodList.add("peek");
        bypassMethodList.add("primePalindrome");
        bypassMethodList.add("pick");
        bypassMethodList.add("sumRegion");
        bypassMethodList.add("computeArea");
        bypassMethodList.add("reverse");
        bypassMethodList.add("rotatedDigits");
        bypassMethodList.add("reset");
        bypassMethodList.add("smallestNumber");
        bypassMethodList.add("snap");
        bypassMethodList.add("isStrictlyPalindromic");
        bypassMethodList.add("judgeSquareSum");
        bypassMethodList.add("fib");
        bypassMethodList.add("passThePillow");
        bypassMethodList.add("findKthNumber");
        bypassMethodList.add("champagneTower");
        bypassMethodList.add("new21Game");
        bypassMethodList.add("nthUglyNumber");
        bypassMethodList.add("maxBottlesDrunk");
        bypassMethodList.add("integerBreak");
        bypassMethodList.add("countOperations");
        bypassMethodList.add("numberOfChild");
        bypassMethodList.add("hammingDistance");
        bypassMethodList.add("tribonacci");
        bypassMethodList.add("rangeBitwiseAnd");
        bypassMethodList.add("numberOfMatches");
        bypassMethodList.add("findDelayedArrivalTime");
        bypassMethodList.add("canBeEqual");
        bypassMethodList.add("squareIsWhite");
        bypassMethodList.add("findLUSlength");
        bypassMethodList.add("minimizedStringLength");
        bypassMethodList.add("isUgly");
        bypassMethodList.add("poorPigs");
        bypassMethodList.add("bulbSwitch");
        bypassMethodList.add("concatenatedBinary");
        bypassMethodList.add("getMoneyAmount");
        bypassMethodList.add("findContestMatch");
        bypassMethodList.add("helper");
        bypassMethodList.add("commonFactors");
        bypassMethodList.add("getShiftedChar");
        bypassMethodList.add("trailingZeroes");
        bypassMethodList.add("divisorGame");
        bypassMethodList.add("generateMatrix");
        bypassMethodList.add("countAndSay");
        bypassMethodList.add("isVowel");
        bypassMethodList.add("maxA");
        bypassMethodList.add("numTrees");







        //Refactored-TheAlgorithms-Java
        bypassMethodList.add("indexOfRightMostSetBit1");
        bypassMethodList.add("binomialCoefficient");
        bypassMethodList.add("process");
        bypassMethodList.add("countNeighbors");
        bypassMethodList.add("printResult");
        bypassMethodList.add("decrypt");
        bypassMethodList.add("valOfChar");
        bypassMethodList.add("getMaxValue");
        bypassMethodList.add("ceil");
        bypassMethodList.add("isDudeney");
        bypassMethodList.add("factorial");
        bypassMethodList.add("floor");
        bypassMethodList.add("lcm");
        bypassMethodList.add("millerRabin");
        bypassMethodList.add("mobius");
        bypassMethodList.add("perimeterIrregularPolygon");
        bypassMethodList.add("fermatPrimeChecking");
        bypassMethodList.add("getEuler");
        bypassMethodList.add("getImage");
        bypassMethodList.add("getScores");
        bypassMethodList.add("getHeight");
        bypassMethodList.add("find");
        bypassMethodList.add("isPaired");
        bypassMethodList.add("checkLetter");
        bypassMethodList.add("getLastComparisons");
        bypassMethodList.add("appendCount");
        bypassMethodList.add("uniquePaths2");
        bypassMethodList.add("pascal");
        bypassMethodList.add("power");
        bypassMethodList.add("checkChar");
        bypassMethodList.add("leonardoNumber");
        bypassMethodList.add("bruteforce");
        bypassMethodList.add("getKey");
        bypassMethodList.add("bitwiseConversion");
        bypassMethodList.add("getProcessId");
        bypassMethodList.add("getArrivalTime");
        bypassMethodList.add("getBurstTime");
        bypassMethodList.add("getWaitingTime");
        bypassMethodList.add("getTurnAroundTimeTime");
        bypassMethodList.add("setProcessId");
        bypassMethodList.add("setArrivalTime");
        bypassMethodList.add("setBurstTime");
        bypassMethodList.add("setWaitingTime");
        bypassMethodList.add("setTurnAroundTimeTime");
        bypassMethodList.add("minTrials");
        bypassMethodList.add("getCost");
        bypassMethodList.add("printTrinomial");
        bypassMethodList.add("getWrongMess");
        bypassMethodList.add("getWrongMessCaught");
        bypassMethodList.add("getWrongMessNotCaught");
        bypassMethodList.add("getCorrectMess");
        bypassMethodList.add("refactor");
        bypassMethodList.add("generateRandomMess");
        bypassMethodList.add("getRandomScores");
        bypassMethodList.add("isCapitalLatinLetter");
        bypassMethodList.add("isSmallLatinLetter");
        bypassMethodList.add("findNthCatalan");
        bypassMethodList.add("nthManShanksPrime");


        //intro-to-java-programming-master
        bypassMethodList.add("isPrime3");
        bypassMethodList.add("zellersAlgo");
        bypassMethodList.add("getCardValue");
        bypassMethodList.add("checkIfinside");
        bypassMethodList.add("getHand");
        bypassMethodList.add("getGameStatus");
        bypassMethodList.add("roleDie1");
        bypassMethodList.add("roleDie2");
        bypassMethodList.add("roleDie1");
        bypassMethodList.add("roleDie2");
        bypassMethodList.add("startGames");
        bypassMethodList.add("convertGMT2EST");
        bypassMethodList.add("getNumLeapYears");
        bypassMethodList.add("monthName");
        bypassMethodList.add("daysInMonth");
        bypassMethodList.add("getRandomCharacter");
        bypassMethodList.add("gcd7");
        bypassMethodList.add("getShuffledDeck");
        bypassMethodList.add("makeDeck");
        bypassMethodList.add("getRandom");
        bypassMethodList.add("valueOf");
        bypassMethodList.add("charValue");
        bypassMethodList.add("isUpperCase");
        bypassMethodList.add("isLetter");
        bypassMethodList.add("compare");
        bypassMethodList.add("checkHexChar");
        bypassMethodList.add("getMonthName");
        bypassMethodList.add("getTotalNumberOfDays");
        bypassMethodList.add("isValid");
        bypassMethodList.add("isPair");
        bypassMethodList.add("operate2");
        bypassMethodList.add("getOp");
        bypassMethodList.add("getExpressionString");
        bypassMethodList.add("calcFibIndexes40to45");
        bypassMethodList.add("isLowerCase");
        bypassMethodList.add("isDigit");
        bypassMethodList.add("hashCode");
        bypassMethodList.add("hexCharToDecimal");
        bypassMethodList.add("displayPattern");
        bypassMethodList.add("numberOfDaysInAYear");
        bypassMethodList.add("printMatrix");
        bypassMethodList.add("numberOfDaysInAYear");
        bypassMethodList.add("getNumberOfDaysInMonth");
        bypassMethodList.add("getNumberOfDaysInMonth");
        bypassMethodList.add("checkPrime1");
        bypassMethodList.add("gcd8");
        bypassMethodList.add("gcd2");

        return bypassMethodList;
    }

    public static ArrayList<String> getByPassFiles() {
        ArrayList<String> bypassFilesList = new ArrayList<>();

//        bypassFilesList.add("RedisOutputStream.java");

        return bypassFilesList;
    }

}
