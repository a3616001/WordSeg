package benzhong;

import benzhong.datastruct.Fearture;
import benzhong.datastruct.Sentence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by BenZ on 16/1/10.
 */
public class UnstructedPerceptron {
    static HashMap<String, Integer> parameters = new HashMap<>();
    static ArrayList<Sentence> trainStc = new ArrayList();
    final static int loopCnt = 10;

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

        for (int j = 0; j < Main.labelCnt; j++) {
            f[0][j] = dot(Fearture.getFeature(cont, '-', (char) (j + '0'), 0));
        }

        for (int i = 1, len = cont.length(); i < len; i++) {
            for (int j = 0; j < Main.labelCnt; j++) {
                f[i][j] = -2147483647;
                for (int k = 0; k < Main.labelCnt; k++) {
                    int res = dot(Fearture.getFeature(cont, (char) (k + '0'), (char) (j + '0'), i));
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

    static char getBestLab(String cont, char preLab, int index) {
        char best = '-';
        int val = -2147483647;
        for (char i = '0'; i < '4'; i++) {
            int tmp = dot(Fearture.getFeature(cont, preLab, i, index));
            if (tmp > val) {
                best = i;
                val = tmp;
            }
        }
        return best;
    }

    static void addParameters(HashSet<String> feature, int d) {
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
    }

    static void train(String trainFileName) throws IOException{
        final File trainFile = new File(trainFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile)));
        String line;
        while ((line = br.readLine()) != null) {
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

                char preLab = '-';
                for (int j = 0, len = curStc.content.length(); j < len; j++) {
                    char best = getBestLab(curStc.content, preLab, j);
                    if (best != curStc.label.charAt(j)) {
                        addParameters(Fearture.getFeature(curStc.content, preLab, best, j), -1);
                        addParameters(Fearture.getFeature(curStc.content, preLab, curStc.label.charAt(j), j), 1);
                    }
                    preLab = curStc.label.charAt(j);
                }
            }
        }

        final File tmpFile = new File("data/tmp.txt");
        BufferedWriter bw = new BufferedWriter(new PrintWriter(tmpFile));
        for (String str : parameters.keySet()) {
            bw.write(str + " " + parameters.get(str) + "\n");
        }
        bw.close();
    }

    static void readParameters(String parameterFileName) throws IOException{
        final File parameterFile = new File(parameterFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(parameterFile)));
        String line;
        while ((line = br.readLine()) != null) {
            int val = Integer.valueOf(line.substring(line.lastIndexOf(' ') + 1));
            line = line.substring(0, line.lastIndexOf(' '));
            parameters.put(line, val);
        }
        br.close();
    }

    public static void runner(String trainFileName, String testFileName, String outputFileName) throws IOException {

        long startTime = System.currentTimeMillis();

        //readParameters("data/parameter_10.txt");
        train(trainFileName);

        final File testFile = new File(testFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        final File outputFile = new File(outputFileName);
        BufferedWriter bw = new BufferedWriter(new PrintWriter(outputFile));
        String line;

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
