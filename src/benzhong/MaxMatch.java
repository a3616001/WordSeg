package benzhong;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by BenZ on 16/1/9.
 */
public class MaxMatch {
    static HashSet<String> dic = new HashSet<>();
    final static int maxLen = 5;

    public static void runner(String trainFileName, String testFileName, String outputFileName) throws IOException {
        final File trainFile = new File(trainFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] dicArr = line.split("  ");
            for (int i = 0; i < dicArr.length; i++) {
                while (dicArr[i].length() > 0 && dicArr[i].charAt(0) == ' ') {
                    dicArr[i] = dicArr[i].substring(1);
                }
                if (dicArr[i].length() == 0)
                    continue;
                dic.add(dicArr[i]);
            }
        }
        br.close();

        final File testFile = new File(testFileName);
        br = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        final File outputFile = new File(outputFileName);
        BufferedWriter bw = new BufferedWriter(new PrintWriter(outputFile));
        while ((line = br.readLine()) != null) {
            while (line.length() > 0) {
                int curLen = Math.min(maxLen, line.length());
                String cur;
                while (curLen > 1) {
                    cur = line.substring(0, curLen);
                    if (dic.contains(cur)) break;
                    --curLen;
                }
                cur = line.substring(0, curLen);
                bw.write(cur + "  ");
                //System.out.print(cur + "  ");
                line = line.substring(curLen);
            }
            bw.write('\n');
            //System.out.print("\n");
        }
        br.close();
        bw.close();
    }
}
