public class ClonedTestingClass {

    public ClonedTestingClass()
    {

    }

    public static int getNthPowerSum(int n, int p)
    {
        mark("int sum=0;\n", false, false);
        int sum=0;
        while (((n > 0) && mark("n > 0", true, false)) || mark("n > 0", false, true)) {
            mark("int temp=n % 10;\n", false, false);
            int temp=n % 10;
            mark("n/=10;\n", false, false);
            n/=10;
            mark("sum+=(int)Math.pow(temp,p);\n", false, false);
            sum+=(int)Math.pow(temp,p);
        }

        mark("return sum;\n", false, false);
        return sum;
    }



}