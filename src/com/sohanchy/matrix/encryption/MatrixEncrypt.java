package com.sohanchy.matrix.encryption;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sohan Chowdhury on 12/4/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
final class MatrixEncrypt {

    private final static int CAPS_HAND_DECODEABLE = 0; //64 FOR A=1, 0 FOR FULL ASCII SUPPORT

    private MatrixEncrypt() {
        //STATIC CLASS CAN NOT BE CONSTRUCTED
    }

    static String encrypt(String msg, String multiplyMe, double dim) {

//        System.out.println(msg.length());

        ArrayList<double[][]> msgMatrixList = stringToOneDMatrixList(msg, dim);

        double[][] multiplyMeMatrix = scanMultiplyMeMatrix(multiplyMe, (int) dim);

        if (multiplyMeMatrix != null) {
            ArrayList<double[][]> resultMatrixList = multiplyMatrixList(multiplyMeMatrix, msgMatrixList);

            return stringMatrixList(resultMatrixList, false);
        }

        return "";

    }

    static String decrypt(String encMsg, String multiplyMe, double dim) {

//        System.out.println(msg.length());

        ArrayList<double[][]> encMsgMatrixList = scanEncMsgToMatrixList(encMsg, dim);

        double[][] multiplyMeMatrix = scanMultiplyMeMatrix(multiplyMe, (int) dim);


        if (multiplyMeMatrix != null) {
            invertMatrix(multiplyMeMatrix);

            ArrayList<double[][]> resultMatrixList = multiplyMatrixList(multiplyMeMatrix, encMsgMatrixList);

            StringBuilder decryptedMsg = new StringBuilder();

            for (double[][] arr : resultMatrixList) {
                for (double[] anArr : arr) {
                    for (double anAnArr : anArr) {
                        decryptedMsg.append((char) (anAnArr + CAPS_HAND_DECODEABLE));
                    }
                }
            }


            return decryptedMsg.toString();

        }

        return "";

    }

    /**
     * Inverts a matrix by Gaussian reduction (slow but very general).
     *
     * @param mat matrix to be inverted
     */
    private static void invertMatrix(double[][] mat) {
        int N = mat.length;
        double t;

        double[][] tmp = new double[mat.length][mat[0].length];

        for (int i = 0; i < mat.length; i++) {
            System.arraycopy(mat[i], 0, tmp[i], 0, mat[i].length);
        }

        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat.length; j++)
                mat[i][j] = (i == j ? 1 : 0);


        for (int i = 0; i < N; i++) {
            if ((t = tmp[i][i]) == 0)
                break;
            for (int j = 0; j < N; j++) {
                tmp[i][j] = tmp[i][j] / t;
                mat[i][j] = mat[i][j] / t;
            }
            for (int k = 0; k < N; k++)
                if (k != i) {
                    t = tmp[k][i];
                    for (int j = 0; j < N; j++) {
                        tmp[k][j] = tmp[k][j] - t * tmp[i][j];
                        mat[k][j] = mat[k][j] - t * mat[i][j];
                    }
                }
        }
    }


    private static ArrayList<double[][]> scanEncMsgToMatrixList(String encMsg, double dim) {

        ArrayList<Double> encMsgList = new ArrayList<>();

        Scanner scanner = new Scanner(encMsg);

        while (scanner.hasNextDouble()) {
            encMsgList.add(scanner.nextDouble());
        }

        ArrayList<double[][]> msgMatrixList = new ArrayList<>();

        if (encMsgList.size() % dim != 0) {
            showWarning("Encrypted Code Error", "Keys May Be Missing in Encrypted Code");
            return msgMatrixList;
        }

        int matrixNeeded = (int) Math.ceil(encMsgList.size() / dim);
        int dimension = (int) dim;


        int listIter = 0;
        for (int i = 0; i < matrixNeeded; i++) {

            double[][] msgMatrix = new double[dimension][1];

            for (int j = 0; j < dimension; j++) {

                msgMatrix[j][0] = encMsgList.get(listIter) - CAPS_HAND_DECODEABLE;
                listIter++;
            }

            msgMatrixList.add(msgMatrix);

        }

        return msgMatrixList;

    }

    private static String stringMatrixList(ArrayList<double[][]> matrixList, @SuppressWarnings("SameParameterValue") boolean castChar) {

        //print matrixList
        StringBuilder str = new StringBuilder();

        for (double[][] msgMatrix : matrixList) {

            for (double[] aMsgMatrix : msgMatrix) {

                if (castChar) {
                    str.append((char) aMsgMatrix[0]).append(",");
                } else {
                    str.append(aMsgMatrix[0]).append(" ");
                }
            }
            str.append("\n");
        }

        return str.toString();

    }


    private static ArrayList<double[][]> multiplyMatrixList(double[][] multiplyMe, ArrayList<double[][]> matrixList) {
        ArrayList<double[][]> resultMatrixList = new ArrayList<>();

        for (double[][] currMatrix : matrixList) {

            double[][] mulRes = matrixMul(multiplyMe, currMatrix);

            resultMatrixList.add(mulRes);
        }

        return resultMatrixList;

    }

    private static ArrayList<double[][]> stringToOneDMatrixList(String msg, double dim) {

        int dimension = (int) dim;

        int matrixNeeded = (int) Math.ceil(msg.length() / dim);

        ArrayList<double[][]> msgMatrixList = new ArrayList<>();

        int stringIter = 0;
        for (int i = 0; i < matrixNeeded; i++) {

            double[][] msgMatrix = new double[dimension][1];

            for (int j = 0; j < dimension; j++) {

                int c = 32; //32 is ASCII FOR SPACE
                if (stringIter < msg.length()) {
                    c = (int) msg.charAt(stringIter) - CAPS_HAND_DECODEABLE;
                    stringIter++;
                }

                msgMatrix[j][0] = c;
            }

            msgMatrixList.add(msgMatrix);

        }

        return msgMatrixList;
    }


    private static double[][] matrixMul(double[][] matrixA, double[][] matrixB) {

        double[][] matrixResult = new double[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixA[0].length; k++) {
                    matrixResult[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return matrixResult;
    }


    private static double[][] scanMultiplyMeMatrix(String multiplyMe, int dim) {
        Scanner scanner = new Scanner(multiplyMe);

        double[][] scannedMatrix = new double[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                if (scanner.hasNextDouble()) {
                    scannedMatrix[i][j] = scanner.nextDouble();
                } else {
                    showWarning("Invalid Matrix", ("Please enter a " + dim + " x " + dim + " matrix."));
                    return null;
                }
        }

        return scannedMatrix;
    }

    static void showWarning(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
