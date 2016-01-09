package benzhong.datastruct;

import java.util.ArrayList;

/**
 * Created by BenZ on 16/1/10.
 */
public class Sentence {
    String content;
    String label;
    public Sentence(String line) {
        content = "";
        label = "";

        String[] wdArr = line.split("  ");
        for (int i = 0; i < wdArr.length; i++) {
            if (wdArr[i].length() == 1) {
                label += "S";
            }
            else {
                label += "B";
                for (int j = 1; j < wdArr[i].length() - 1; j++) {
                    label += "M";
                }
                label += "E";
            }
            content += wdArr[i];
        }
    }
}
