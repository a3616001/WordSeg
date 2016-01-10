package benzhong.datastruct;

import java.util.HashSet;

/**
 * Created by BenZ on 16/1/10.
 */
public class Fearture {
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

    public static HashSet<String> getFeature(String cont, char preLab, char lab, int index) {
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

}
