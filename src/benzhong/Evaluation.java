package benzhong;

import java.io.*;

/**
 * Created by BenZ on 16/1/9.
 */
public class Evaluation {
    public static void evaluation(String stdFileName, String evaFileName) throws IOException{
        System.out.println("Evaluate " + stdFileName + "  " + evaFileName + " ...");

        File stdFile = new File(stdFileName);
        File evaFile = new File(evaFileName);
        BufferedReader stdBr = new BufferedReader(new InputStreamReader(new FileInputStream(stdFile)));
        BufferedReader evaBr = new BufferedReader(new InputStreamReader(new FileInputStream(evaFile)));

        String stdLine, evaLine;
        int stdCnt = 0, evaCnt = 0, correct = 0;
        while ((stdLine = stdBr.readLine()) != null) {
            evaLine = evaBr.readLine();
            int stdP = 0, evaP = 0, i = 0, j = 0;
            String[] stdArr, evaArr;
            stdArr = stdLine.split("  ");
            evaArr = evaLine.split("  ");
            stdCnt += stdArr.length;
            evaCnt += evaArr.length;
            while (i < stdArr.length && j < evaArr.length) {
                if (stdP == evaP && stdArr[i].equals(evaArr[j])) {
                    ++correct;
                    stdP += stdArr[i].length();
                    evaP += evaArr[j].length();
                    ++i; ++j;
                }
                else {
                    if (stdP + stdArr[i].length() < evaP + evaArr[j].length()) {
                        stdP += stdArr[i].length();
                        ++i;
                    }
                    else {
                        evaP += evaArr[j].length();
                        ++j;
                    }
                }
            }
        }

        double p = ((double) correct) / evaCnt, r = ((double)correct)/stdCnt;
        double f = 2 * p * r / (p + r);
        System.out.printf("Precision:\n%d / %d = %.2f%%\n", correct, evaCnt, p * 100);
        System.out.printf("Recall:\n%d / %d = %.2f%%\n", correct, stdCnt, r * 100);
        System.out.printf("F-Score:\n %.2f%%\n", f * 100);
    }
}
