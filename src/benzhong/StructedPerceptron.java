package benzhong;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import benzhong.datastruct.Sentence;

/**
 * Created by BenZ on 16/1/10.
 */
public class StructedPerceptron {
    static HashMap<String, Integer> parameters = new HashMap<>();
    static ArrayList<Sentence> trainStc = new ArrayList();
    final static int loopCnt = 10;

    final static String punc = "．，！（）.?‘’“”…、。〈〉《》『』〔〕";
    final static String number = "０１２３４５６７８９";
    final static String chiNum = "零一二三四五六七八九十百千万亿";
    final static String eng = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";

    static char getType(char ch) {
        for (int i = 0, len = punc.length(); i < len; i++) {
            if (punc.charAt(i) == ch)
                return '1';
        }
        for (int i = 0, len = number.length(); i < len; i++) {
            if (number.charAt(i) == ch)
                return '2';
        }
        for (int i = 0, len = chiNum.length(); i < len; i++) {
            if (chiNum.charAt(i) == ch)
                return '3';
        }
        for (int i = 0, len = eng.length(); i < len; i++) {
            if (eng.charAt(i) == ch)
                return '4';
        }
        return '0';
    }

    static HashSet<String> getFeature(String cont, char preLab, char lab, int index) {
        HashSet<String> feature = new HashSet<>();

        feature.add("1" + cont.charAt(index) + lab);
        if (index > 0) {
            feature.add("2" + cont.charAt(index - 1) + lab);
        }
        if (index < cont.length() - 1) {
            feature.add("3" + cont.charAt(index + 1) + lab);
        }

        if (index > 0) {
            feature.add("4" + cont.substring(index - 1, index + 1) + lab);
        }
        if (index < cont.length() - 1) {
            feature.add("5" + cont.substring(index, index + 2) + lab);
        }

        /*if (index - 1 > 0) {
            feature.add("6" + cont.substring(index - 2 ,index + 1));
        }*/
        if (index > 0 && index < cont.length() - 1) {
            feature.add("7" + cont.substring(index - 1, index + 2) + lab);
        }
        /*if (index < cont.length() - 2) {
            feature.add("8" + cont.substring(index, index + 3));
        }*/

        char type = getType(cont.charAt(index));
        feature.add("9" + type + lab);

        /*if (index > 0) {
            feature.add("10" + preLab);
        }*/
        //feature.add("11" + lab);
        feature.add("12" + preLab + lab);

        //feature.add("13" + cont.charAt(index) + lab);
        //feature.add("14" + type + lab);


        return feature;
    }

    static int dot(HashSet<String> feature) {
        int s = 0;
        for (String f : feature) {
            s += parameters.getOrDefault(f, 0);
        }
        return s;
    }

    static int[][] f = new int[10005][Main.labelCnt];
    static int[][] p = new int[10005][Main.labelCnt];
    static String viterbi(String cont) {
        //int [][] f = new int[cont.length()][Main.labelCnt];
        //int [][] p = new int[cont.length()][Main.labelCnt];

        for (int j = 0; j < Main.labelCnt; j++) {
            f[0][j] = dot(getFeature(cont, '-', (char)(j + '0'), 0));
        }

        for (int i = 1, len = cont.length(); i < len; i++) {
            for (int j = 0; j < Main.labelCnt; j++) {
                f[i][j] = -2147483647;
                for (int k = 0; k < Main.labelCnt; k++) {
                    int res = dot(getFeature(cont, (char)(k + '0'), (char)(j + '0'), i));
                    if (f[i - 1][k] + res > f[i][j]) {
                        f[i][j] = f[i - 1][k] + res;
                        p[i][j] = k;
                    }
                }
            }
        }

        int x, y;
        x = cont.length() - 1; y = 0;
        for (int j = 0; j < Main.labelCnt; j++) {
            if (f[x][j] > f[x][y]) {
                y = j;
            }
        }

        String rev = "";
        while (x >= 0) {
            rev += (char)(y + '0');
            y = p[x][y];
            --x;
        }
        String s = "";
        for (int i = rev.length() - 1; i >= 0; i--) {
            s += rev.charAt(i);
        }

        return s;
    }

    static void updateParameters(Sentence s, String lab, int d) {
        HashSet<String> feature = getFeature(s.content, '-', lab.charAt(0), 0);
        Iterator<String> it = feature.iterator();
        while (it.hasNext()) {
            String f = it.next();
            Integer val = parameters.getOrDefault(f, 0) + d;
            if (val != 0) {
                parameters.put(f, val);
            }
            else {
                parameters.remove(f);
            }
        }

        for (int i = 1, num = s.content.length(); i < num; i++) {
            feature = getFeature(s.content, lab.charAt(i - 1), lab.charAt(i), i);
            it = feature.iterator();
            while (it.hasNext()) {
                String f = it.next();
                Integer val = parameters.getOrDefault(f, 0) + d;
                if (val != 0) {
                    parameters.put(f, val);
                }
                else {
                    parameters.remove(f);
                }
            }
        }
    }

    public static void runner(String trainFileName, String testFileName, String outputFileName) throws IOException {

        long startTime = System.currentTimeMillis();

        final File trainFile = new File(trainFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile)));

        String line;
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            trainStc.add(new Sentence(line));
        }
        System.out.println(trainStc.size());
        br.close();

        for (int loop = 0; loop < loopCnt; loop++) {
            int num = trainStc.size();
            for (int i = 0; i < num; i++) {
                System.out.println(loop + " - " + i);
                Sentence curStc = trainStc.get(i);
                if (curStc.content.length() == 0)
                    continue;
                String decode = viterbi(curStc.content);
                System.out.println(decode + "\n" + curStc.label);
                updateParameters(curStc, decode, -1);
                updateParameters(curStc, curStc.label, 1);
            }
        }

        final File parameterFile = new File("data/parameter.txt");
        BufferedWriter bw = new BufferedWriter(new PrintWriter(parameterFile));
        for (String str : parameters.keySet()) {
            //System.out.println(str);
            bw.write(str + " " + parameters.get(str) + "\n");
        }
        bw.close();

        final File testFile = new File(testFileName);
        br = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        final File outputFile = new File(outputFileName);
        bw = new BufferedWriter(new PrintWriter(outputFile));
        while ((line = br.readLine()) != null) {
            String decode = viterbi(line);
            for (int i = 0, len = line.length(); i < len; i++) {
                if ((decode.charAt(i) == '0' || decode.charAt(i) == '3') && i > 0) {
                    bw.write("  ");
                }
                bw.write(line.charAt(i));
            }
            bw.write("\n");
        }
        br.close();
        bw.close();

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time = " + elapsedTime / 1000.0 + " secs");

        System.gc();
        long memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L * 1024L);
        System.out.println("Memory in use after the analysis: " + memUsed + " MB");

    }
}
