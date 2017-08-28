package co.digdaya.kindis.live.helper;

import java.util.Random;

/**
 * Created by DELL on 6/3/2017.
 */

public class RandomString {
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    Random random;

    public RandomString() {
        random = new Random();
    }

    public String getRandomString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
