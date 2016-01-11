package benzhong;

import benzhong.datastruct.Sentence;

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
        int stdSplCnt = 0, evaSplCnt = 0, splCorrect = 0;
        while ((stdLine = stdBr.readLine()) != null) {
            evaLine = evaBr.readLine();

            String stdLab = new Sentence(stdLine).label;
            String evaLab = new Sentence(evaLine).label;
            System.out.println(stdLine);
            System.out.println(evaLine);
            System.out.println(stdLab + "\n" + evaLab);
            for (int i = 0; i < stdLab.length(); i++) {
                if (stdLab.charAt(i) == '0' || stdLab.charAt(i) == '3') {
                    ++stdSplCnt;
                }
                if (evaLab.charAt(i) == '0' || evaLab.charAt(i) == '3') {
                    ++evaSplCnt;
                }
                if ((stdLab.charAt(i) == '0' || stdLab.charAt(i) == '3') && (evaLab.charAt(i) == '0' || evaLab.charAt(i) == '3')) {
                    ++splCorrect;
                }
            }

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
        System.out.println("For splited label:");
        double p = ((double) splCorrect) / evaSplCnt, r = ((double)splCorrect) / stdSplCnt;
        double f = 2 * p * r / (p + r);
        System.out.printf("Precision:\n%d / %d = %.1f%%\n", splCorrect, evaSplCnt, p * 100);
        System.out.printf("Recall:\n%d / %d = %.1f%%\n", splCorrect, stdSplCnt, r * 100);
        System.out.printf("F-Score:\n %.1f%%\n", f * 100);

        System.out.println("For words:");
        p = ((double) correct) / evaCnt; r = ((double)correct)/stdCnt;
        f = 2 * p * r / (p + r);
        System.out.printf("Precision:\n%d / %d = %.1f%%\n", correct, evaCnt, p * 100);
        System.out.printf("Recall:\n%d / %d = %.1f%%\n", correct, stdCnt, r * 100);
        System.out.printf("F-Score:\n %.1f%%\n", f * 100);
    }

    public static void main(String[] args) throws IOException {
        String answerFileName = "data/test.answer.txt";
        String outputFileName = "data/output2.txt";

        evaluation(answerFileName, outputFileName);
    }
}
