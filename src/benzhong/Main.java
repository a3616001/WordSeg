package benzhong;

import java.io.*;
import java.util.Iterator;
import java.util.TreeSet;

public class Main {
    public static int labelCnt = 4;
    public static void main(String[] args) throws IOException{
        String trainFileName = "data/train.txt";
        String testFileName = "data/test.txt";
        String answerFileName = "data/test.answer.txt";
        String outputFileName = "data/output_sp_10.txt";

        //StructedPerceptron.runner(trainFileName, testFileName, outputFileName);
        //MaxMatch.runner(trainFileName, testFileName, outputFileName);
        Evaluation.evaluation(answerFileName, outputFileName);

        /*final File testFile = new File(testFileName);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        final File tmpFile = new File("data/matching_result1.txt");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile)));
        final File outputFile = new File("data/result2.txt");
        BufferedWriter bw = new BufferedWriter(new PrintWriter(outputFile));
        String line, l2;
        while ((line = br1.readLine()) != null) {
            l2 = br2.readLine();
            for (int i = 0; i < line.length(); i++) {
                if (i > 0 && l2.charAt(2 * i) == '1') {
                    bw.write("  ");
                }
                bw.write(line.charAt(i));
            }
            bw.write("\n");
        }
        bw.close();*/
    }
}
