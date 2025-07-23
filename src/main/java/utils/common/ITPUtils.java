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
        //Refactored-TheAlgorithms-Java
        bypassMethodList.add("main");
        bypassMethodList.add("indexOfRightMostSetBit");
        bypassMethodList.add("bpR");
        bypassMethodList.add("binomialCoefficient");
        bypassMethodList.add("leonardoNumber");
        bypassMethodList.add("lucasSeries");
        bypassMethodList.add("pascal");
        bypassMethodList.add("TrinomialValue");
        bypassMethodList.add("printTrinomial");
        bypassMethodList.add("square_Root");
        bypassMethodList.add("getImage");
        bypassMethodList.add("fermatPrimeChecking");
        bypassMethodList.add("minTrials");
        bypassMethodList.add("perimeterIrregularPolygon");
        bypassMethodList.add("calculatePi");
        bypassMethodList.add("isOperator");

        //For LeetCode solution
        bypassMethodList.add("countTriples");
        bypassMethodList.add("generate");
        bypassMethodList.add("fizzBuzz");
        bypassMethodList.add("getRow");
        bypassMethodList.add("divisorGame");
        bypassMethodList.add("numOfArrays");
        bypassMethodList.add("kInversePairs");
        bypassMethodList.add("countAndSay");
        bypassMethodList.add("getMoneyAmount");
        bypassMethodList.add("knightDialer");
        bypassMethodList.add("integerBreak");
        bypassMethodList.add("knightProbability");
        bypassMethodList.add("findContestMatch");
        bypassMethodList.add("generateMatrix");
        bypassMethodList.add("numTrees");
        bypassMethodList.add("lexicalOrder");
        bypassMethodList.add("toHex");
        bypassMethodList.add("countEven");
        bypassMethodList.add("maxProduct");
        bypassMethodList.add("rangeBitwiseAnd");
        bypassMethodList.add("countGoodStrings");
        bypassMethodList.add("new21Game");
        bypassMethodList.add("numOfBurgers");
        bypassMethodList.add("smallestNumber");
        bypassMethodList.add("sumZero");
        bypassMethodList.add("minCuttingCost");
        bypassMethodList.add("isUgly");
        bypassMethodList.add("maxBottlesDrunk");
        bypassMethodList.add("constructRectangle");
        bypassMethodList.add("isThree");

        bypassMethodList.add("trailingZeroes");
        bypassMethodList.add("numberOfMatches");
        bypassMethodList.add("sumBase");
        bypassMethodList.add("numWaterBottles");
        bypassMethodList.add("arrangeCoins");
        bypassMethodList.add("minBitFlips");
        bypassMethodList.add("isPerfectSquare");
        bypassMethodList.add("brokenCalc");
        bypassMethodList.add("minMoves");
        bypassMethodList.add("mirrorReflection");
        bypassMethodList.add("kthFactor");
        bypassMethodList.add("alternateDigitSum");
        bypassMethodList.add("smallestRepunitDivByK");
        bypassMethodList.add("confusingNumber");

        //Exception unit For LeetCode solution
        bypassMethodList.add("hasAlternatingBits");
        bypassMethodList.add("categorizeBox");
        bypassMethodList.add("countOperations");
        bypassMethodList.add("countSymmetricIntegers");
        bypassMethodList.add("divideString");
        bypassMethodList.add("fib");
        bypassMethodList.add("pivotInteger");
        bypassMethodList.add("getMaximumGenerated");
        bypassMethodList.add("hammingDistance");
        bypassMethodList.add("countBalls");
        bypassMethodList.add("canBeTypedWords");
        bypassMethodList.add("tribonacci");
        bypassMethodList.add("passThePillow");
        bypassMethodList.add("powerfulIntegers");
        bypassMethodList.add("isPowerOfTwo");
        bypassMethodList.add("selfDividingNumbers");
        bypassMethodList.add("mySqrt");
        bypassMethodList.add("findKthNumber");
        bypassMethodList.add("withdraw");
        bypassMethodList.add("divide");
        bypassMethodList.add("maxA");
        bypassMethodList.add("primePalindrome");
        bypassMethodList.add("reverse");
        bypassMethodList.add("primePalindrome");
        bypassMethodList.add("isStrictlyPalindromic");
        bypassMethodList.add("judgeSquareSum");
        bypassMethodList.add("nthUglyNumber");
        bypassMethodList.add("primePalindrome");
        bypassMethodList.add("primePalindrome");
        bypassMethodList.add("coloredCells");
        bypassMethodList.add("numberOfChild");
        bypassMethodList.add("maxContainers");
        bypassMethodList.add("numberOfDays");

        //JAVA-Challenges-main
        bypassMethodList.add("calculateGCD");

        //java-algorithms-implementation-master
        bypassMethodList.add("polar");

        return bypassMethodList;
    }
}
