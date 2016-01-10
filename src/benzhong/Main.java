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
        String outputFileName = "data/NP2_output.txt";

        UnstructedPerceptron.runner(trainFileName, testFileName, outputFileName);
        //StructedPerceptron.runner(trainFileName, testFileName, outputFileName);
        //MaxMatch.runner(trainFileName, testFileName, outputFileName);
        Evaluation.evaluation(answerFileName, outputFileName);

    }
}
