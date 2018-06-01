package assignment4;

import java.io.File;
import java.util.*;

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
            System.out.print("Mapper " + i + " Output \n" + mapperInputs.get(i));
        }

        System.out.println("******************************************************");
//
        List<Reducer<String, Integer>> shuffledList = shuffle(mapperInputs);

        System.out.println("******************************************************");

        Map<Integer, List<GroupByPair<String, Integer>>> reduceInput = makeReduceInput(shuffledList);

        for (int i = 0; i < reduceInput.size(); i++) {
            System.out.println("Reducer " + i + " input");
            for (GroupByPair<String, Integer> pair : reduceInput.get(i)) {
                System.out.println(pair);
            }
        }

        Map<Integer, List<Pair<String, Integer>>> reduceOutput = makeReduceOutput(reduceInput);

        System.out.println("******************************************************");

        for (int i = 0; i < reduceOutput.size(); i++) {
            System.out.println("Reducer output " + i);
            for (Pair<String, Integer> pair : reduceOutput.get(i)) {
                System.out.println(pair);
            }
        }
    }

    private static Mapper<String, Integer> makeMapperInputs(String filePath) throws Exception {

        Map<String, Pair<String, Integer>> pairs = new HashMap<>();
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.next();
            Pair<String, Integer> pair = new Pair<>(line, 1);
            if (pairs.containsKey(line)) {
                int val = pairs.get(line).getValue();
                pairs.get(line).setValue(++val);
            } else pairs.put(line, pair);
        }
        return new Mapper<>(file.getName(), pairs);
    }

    private static List<Reducer<String, Integer>> shuffle(List<Mapper<String, Integer>> mappers) {

        List<Reducer<String, Integer>> shuffledList = new ArrayList<>();

        Map<Integer, Map<Integer, List<Pair<String, Integer>>>> logs = new HashMap<>();

        for (int i = 0; i < REDUCERS_COUNT; i++) shuffledList.add(new Reducer<>(new ArrayList<>()));

        for (int i = 0; i < mappers.size(); i++) {
            Map<String, Pair<String, Integer>> pairs = mappers.get(i).getPairs();
            for (Pair<String, Integer> pair : pairs.values()) {
                int partition = getPartition(pair.getKey());
                Reducer<String, Integer> reducer = shuffledList.get(partition);
                reducer.addPair(pair);

                if (!logs.containsKey(i)) logs.put(i, new HashMap<>());
                if (!logs.get(i).containsKey(partition)) logs.get(i).put(partition, new ArrayList<>());
                logs.get(i).get(partition).add(pair);
            }
        }

        for (int i = 0; i < logs.size(); i++) {
            for (int j = 0; j < logs.get(i).size(); j++) {
                System.out.println("Pair send from Mapper " + i + " Reducer " + j);
                for (Pair<String, Integer> pair : logs.get(i).get(j)) {
                    System.out.println(pair);
                }
            }

        }
        return shuffledList;
    }


    private static Map<Integer, List<GroupByPair<String, Integer>>> makeReduceInput(List<Reducer<String, Integer>> reducers) {
        Map<Integer, List<GroupByPair<String, Integer>>> groupByPair = new HashMap<>();

        for (int i = 0; i < reducers.size(); i++) {
            for (Pair<String, Integer> pair : reducers.get(i).getPairs()) {
                List<Integer> count = new ArrayList<>();
                count.add(pair.getValue());

                GroupByPair<String, Integer> newGroupByPair = new GroupByPair<>(pair.getKey(), count);
                if (!groupByPair.containsKey(i)) groupByPair.put(i, new ArrayList<>());

                if (!groupByPair.get(i).contains(newGroupByPair)) {
                    groupByPair.get(i).add(newGroupByPair);
                } else {
                    for (GroupByPair<String, Integer> p : groupByPair.get(i)) {
                        if (p.equals(newGroupByPair)) p.getValues().add(1);
                    }
                }
            }
        }
        return groupByPair;
    }


    private static Map<Integer, List<Pair<String, Integer>>> makeReduceOutput(Map<Integer, List<GroupByPair<String, Integer>>> groupByPairs) {
        Map<Integer, List<Pair<String, Integer>>> reducerOutput = new HashMap<>();
        for (int i = 0; i < groupByPairs.size(); i++) {
            List<GroupByPair<String, Integer>> pairs = groupByPairs.get(i);

            for (GroupByPair<String, Integer> g : pairs) {
                int count = 0;
                for (int c : g.getValues()) count += c;
                if (!reducerOutput.containsKey(i)) reducerOutput.put(i, new ArrayList<>());
                reducerOutput.get(i).add(new Pair<>(g.getKey(), count));
            }
        }
        return reducerOutput;
    }

    private static int getPartition(String key) {
        return Math.abs(key.hashCode()) % REDUCERS_COUNT;
    }
}
