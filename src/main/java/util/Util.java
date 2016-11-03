package util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * Various utility functions and helpers
 */
public class Util {

    /**
     * Creates a n x m matrix consisting of random float values
     * where each row sums up to 1.
     *
     * @param n
     * @param m
     * @return
     */
    public static float[][] initNormalizedRandomMatrix(int n, int m) {

        float[][] mat = new float[n][m];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < m; j++) {
                mat[i][j] = (float) Math.random();
            }

            mat[i] = normalize(mat[i]);

        }

        return mat;
    }

    /**
     * Normalizes a vector so that all values sum to 1.
     *
     * @param vector
     * @return
     */
    public static float[] normalize(float[] vector) {

        float sum = (float) IntStream.range(0, vector.length).mapToDouble(i -> vector[i]).parallel().sum();

        if (Math.abs(sum) == 0){
            throw new RuntimeException("Vector sum is 0");
        }

        float[] normalizedVector = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {

            if (vector[i] < 0){
                throw new RuntimeException("Vector element is < 0! (is "+vector[i]+")");
            }

            normalizedVector[i] = vector[i] / sum;
        }

        return normalizedVector;
    }


    /**
     * Prints a float matrix for debugging
     *
     * @param m matrix
     */
    public static void printFloatMatrix(float[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints a byte matrix for debugging
     *
     * @param m
     */
    public static void printByteMatrix(byte[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static DateFormat df = new SimpleDateFormat("HH:mm:ss");

    public static void log(String msg){
        System.out.println("["+df.format(new Date())+"] "+msg);
    }
}
