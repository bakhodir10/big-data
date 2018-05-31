package assignment3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class WordCount {

    private static final int MAPPERS_COUNT = 3;
    private static final int REDUCERS_COUNT = 4;

    public static void main(String[] args) throws Exception {

        List<Mapper<String, Integer>> mapperInputs = new ArrayList<>(MAPPERS_COUNT);
        String basePath = "src/assignment3/";

        for (int i = 0; i < MAPPERS_COUNT; i++) {
            String filePath = basePath.concat("text" + i + ".txt");
            Mapper<String, Integer> mapper = makeMapperInputs(filePath);
            mapperInputs.add(mapper);
        }

        for (int i = 0; i < mapperInputs.size(); i++) {
            System.out.println("Mapper " + i + " Output \n" + mapperInputs.get(i));
        }

        System.out.println("******************************************************");

        List<Reducer<String, Integer>> shuffledList = shuffle(mapperInputs);

        for (int i = 0; i < shuffledList.size(); i++) {
            System.out.println("Shuffled List " + i + " Output \n" + shuffledList.get(i));
        }

        System.out.println("******************************************************");

        List<GroupByPair<String, Integer>> reduceInput = makeReduceInput(shuffledList);

        for (int i = 0; i < reduceInput.size(); i++) {
            System.out.println(reduceInput.get(i));
        }

        List<Pair<String, Integer>> reduceOutput = makeReduceOutput(reduceInput);

        System.out.println("******************************************************");

        for (int i = 0; i < reduceOutput.size(); i++) {
            System.out.println(reduceOutput.get(i));
        }
    }

    private static Mapper<String, Integer> makeMapperInputs(String filePath) throws Exception {

        List<Pair<String, Integer>> pairs = new ArrayList<>();
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.next();
            pairs.add(new Pair<>(line, 1));
        }
        return new Mapper<>(file.getName(), pairs);
    }

    private static List<Reducer<String, Integer>> shuffle(List<Mapper<String, Integer>> mappers) {

        List<Reducer<String, Integer>> shuffledList = new ArrayList<>(REDUCERS_COUNT);
        for (int i = 0; i < REDUCERS_COUNT; i++) shuffledList.add(new Reducer<>(new ArrayList<>()));

        for (int i = 0; i < mappers.size(); i++) {
            List<Pair<String, Integer>> pairs = mappers.get(i).getPairs();
            for (int j = 0; j < pairs.size(); j++) {
                int partition = getPartition(pairs.get(j).getKey());
                Reducer<String, Integer> reducer = shuffledList.get(partition);
                reducer.addPair(pairs.get(j));
            }
        }

        return shuffledList;
    }

    private static List<GroupByPair<String, Integer>> makeReduceInput(List<Reducer<String, Integer>> reducers) {
        List<GroupByPair<String, Integer>> groupByPair = new ArrayList<>();

        for (Reducer<String, Integer> reducer : reducers) {
            for (Pair<String, Integer> pair : reducer.getPairs()) {
                List<Integer> count = new ArrayList<>();
                count.add(1);

                GroupByPair<String, Integer> newGroupByPair = new GroupByPair<>(pair.getKey(), count);

                if (!groupByPair.contains(newGroupByPair)) groupByPair.add(newGroupByPair);
                else {
                    for (GroupByPair<String, Integer> p : groupByPair) {
                        if (p.equals(newGroupByPair)) p.getValues().add(1);
                    }
                }
            }
        }
        return groupByPair;
    }

    private static List<Pair<String, Integer>> makeReduceOutput(List<GroupByPair<String, Integer>> groupByPairs) {
        List<Pair<String, Integer>> reducerOutput = new ArrayList<>();
        for (GroupByPair<String, Integer> g : groupByPairs) {
            int count = 0;
            for (int c : g.getValues()) count += c;
            reducerOutput.add(new Pair<>(g.getKey(), count));
        }
        return reducerOutput;
    }

    private static int getPartition(String key) {
        return Math.abs(key.hashCode()) % REDUCERS_COUNT;
    }
}
