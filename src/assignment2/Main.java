package assignment2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class Main {

    public static void main(String[] args) throws IOException {

        //store all the pairs
        List<Mapper<String, Integer>> pairs = new ArrayList<>();

        File file = new File("src/assignment2/input123.txt");
        Scanner sc = new Scanner(file);

        // read words in given the file by space by space
        while (sc.hasNext()) {
            String line = sc.next().toLowerCase();
            pairs.add(new Mapper<>(line, 1));
        }

        // note that this is java 8 feature. If it doesn't work, please check your jdk version
        // and make sure the version is 8
        pairs.sort(Comparator.comparing(Mapper::getKey));

        // make reduce Input
        List<GroupByPair<String, Integer>> reducerInput = new ArrayList<>();

        for (Mapper<String, Integer> mapper : pairs) {
            List<Integer> count = new ArrayList<>();
            count.add(1);

            GroupByPair<String, Integer> newGroupByPair = new GroupByPair<>(mapper.getKey(), count);

            if (!reducerInput.contains(newGroupByPair)) reducerInput.add(newGroupByPair);
            else {
                for (GroupByPair<String, Integer> p : reducerInput) {
                    if (p.equals(newGroupByPair)) p.getValues().add(1);
                }
            }
        }

//        reducerInput.forEach(System.out::println);

        // make reduce Output
        List<Reducer> reducerOutput = new ArrayList<>();
        for (GroupByPair<String, Integer> g : reducerInput) {
            int count = 0;
            for (int c : g.getValues()) count += c;
            reducerOutput.add(new Reducer<>(g.getKey(), count));
        }

        reducerOutput.forEach(System.out::println);
    }
}
