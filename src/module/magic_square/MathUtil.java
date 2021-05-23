package module.magic_square;

public class MathUtil {
    public static double sum(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++)
            sum = sum + data[i];
        return sum;
    }

    public static double mean(double[] data) {
        double mean = 0;
        mean = sum(data) / data.length;
        return mean;
    }

    // population variance
    public static double popVariance(double[] data) {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - mean(data)), 2));
        }
        variance = variance / data.length;
        return variance;
    }

    // population standard deviation
    public static double popStdDev(double[] data) {
        double std_dev;
        std_dev = Math.sqrt(popVariance(data));
        return std_dev;
    }

    //sample variance
    public static double sampleVariance(double[] data) {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - mean(data)), 2));
        }
        variance = variance / (data.length - 1);
        return variance;
    }

    // sample standard deviation
    public static double sampleStdDev(double[] data) {
        double std_dev;
        std_dev = Math.sqrt(sampleVariance(data));
        return std_dev;
    }

}