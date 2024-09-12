package test.lib;

import java.util.Random;

public class DataGenerator {
    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
