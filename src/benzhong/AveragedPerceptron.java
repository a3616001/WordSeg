package benzhong;

import benzhong.datastruct.Fearture;
import benzhong.datastruct.Sentence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by BenZ on 16/1/11.
 */
public class AveragedPerceptron {
    static HashMap<String, Double> parameters = new HashMap<>();
    static HashMap<String, Double> sumParameters = new HashMap<>();
    static ArrayList<Sentence> trainStc = new ArrayList();
    final static int loopCnt = 10;
    static int finished = 0;

    static double dot(HashSet<String> feature) {
        double s = 0;
        for (String f : feature) {
            s += parameters.getOrDefault(f, 0.0);
        }
        return s;
    }

    static double[][] f = new double[10005][Main.labelCnt];
    static int[][] p = new int[10005][Main.labelCnt];
    static String viterbi(String cont) {

        for (int j = 0; j < Main.labelCnt; j++) {
            f[0][j] = dot(Fearture.getFeature(cont, '-', (char) (j + '0'), 0));
        }

        for (int i = 1, len = cont.length(); i < len; i++) {
            for (int j = 0; j < Main.labelCnt; j++) {
                f[i][j] = -2147483647.0;
                for (int k = 0; k < Main.labelCnt; k++) {
                    double res = dot(Fearture.getFeature(cont, (char) (k + '0'), (char) (j + '0'), i));
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
        HashSet<String> feature = Fearture.getFeature(s.content, '-', lab.charAt(0), 0);
        Iterator<String> it = feature.iterator();
        while (it.hasNext()) {
            String f = it.next();
            Double val = parameters.getOrDefault(f, 0.0) + d;
            if (val != 0) {
                parameters.put(f, val);
            }
            else {
                parameters.remove(f);
            }
        }

        for (int i = 1, num = s.content.length(); i < num; i++) {
            feature = Fearture.getFeature(s.content, lab.charAt(i - 1), lab.charAt(i), i);
            it = feature.iterator();
            while (it.hasNext()) {
                String f = it.next();
                Double val = parameters.getOrDefault(f, 0.0) + d;
                if (val != 0) {
                    parameters.put(f, val);
                }
                else {
                    parameters.remove(f);
                }
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
                String decode = viterbi(curStc.content);
                System.out.println(decode + "\n" + curStc.label);
                updateParameters(curStc, decode, -1);
                updateParameters(curStc, curStc.label, 1);
            }

            for (String f : parameters.keySet()) {
                Double val = parameters.get(f) + sumParameters.getOrDefault(f, 0.0);
                if (val != 0) {
                    sumParameters.put(f, val);
                }
                else {
                    sumParameters.remove(f);
                }
            }
            ++finished;
        }

        final File tmpFile = new File("data/tmp.txt");
        BufferedWriter bw = new BufferedWriter(new PrintWriter(tmpFile));
        bw.write(parameters.size() + "\n");
        for (String str : parameters.keySet()) {
            bw.write(str + " " + parameters.get(str) + "\n");
        }
        bw.write(finished + "\n");
        bw.write(sumParameters.size() + "\n");
        for (String str : sumParameters.keySet()) {
            bw.write(str + " " + sumParameters.get(str) + "\n");
        }
        bw.close();
    }

    static void readParameters(String parameterFileName) throws IOException{
        final File parameterFile = new File(parameterFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(parameterFile)));
        String line;
        line = br.readLine();
        int tot = Integer.valueOf(line);
        for (int i = 0; i < tot; i++) {
            line = br.readLine();
            double val = Double.valueOf(line.substring(line.lastIndexOf(' ') + 1));
            line = line.substring(0, line.lastIndexOf(' '));
            parameters.put(line, val);
        }

        line = br.readLine();
        finished = Integer.valueOf(line);
        line = br.readLine();
        tot = Integer.valueOf(line);
        for (int i = 0; i < tot; i++) {
            line = br.readLine();
            double val = Double.valueOf(line.substring(line.lastIndexOf(' ') + 1));
            line = line.substring(0, line.lastIndexOf(' '));
            sumParameters.put(line, val);
        }
        br.close();
    }
    public static void runner(String trainFileName, String testFileName, String outputFileName) throws IOException {

        long startTime = System.currentTimeMillis();

        //readParameters("data/AP10_parameter.txt");
        train(trainFileName);

        for (String f : sumParameters.keySet()) {
            double val = sumParameters.get(f);
            val = val / finished;
            sumParameters.put(f, val);
        }
        parameters = sumParameters;

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
