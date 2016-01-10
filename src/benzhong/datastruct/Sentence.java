package benzhong.datastruct;

import java.util.ArrayList;

/**
 * Created by BenZ on 16/1/10.
 */
public class Sentence {
    public String content;
    public String label;
    public Sentence(String line) {
        content = "";
        label = "";

        String[] wdArr = line.split("  ");

        for (int i = 0; i < wdArr.length; i++) {
            while (wdArr[i].length() > 0 && wdArr[i].charAt(0) == ' ') {
                //System.out.println(wdArr[i]);
                wdArr[i] = wdArr[i].substring(1);
            }
            if (wdArr[i].length() == 0)
                continue;
            if (wdArr[i].length() == 1) {
                label += "3";
            }
            else {
                label += "0";
                for (int j = 1; j < wdArr[i].length() - 1; j++) {
                    label += "1";
                }
                label += "2";
            }
            content += wdArr[i];
        }
    }
}
