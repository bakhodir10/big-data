package assignment1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Pair {

    public static void main(String[] args) throws IOException {

        //store all the pairs
        List<Mapper<String, Integer>> pairs = new ArrayList<>();

        File file = new File("src/assignment1/input123.txt");
        Scanner sc = new Scanner(file);

        // read words in given the file by space by space
        while (sc.hasNext()) {
            String line = sc.next();
            pairs.add(new Mapper<>(line, 1));
        }

        // note that this is java 8 feature. If it doesn't work, please check your jdk version
        // and make sure the version is 8
        pairs.sort(Comparator.comparing(Mapper::getKey));

        // print all the pairs
        pairs.forEach(System.out::println);
    }
}
