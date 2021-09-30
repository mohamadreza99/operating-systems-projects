import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author mohamadreza fereydooni
 * computer assignment number 3 for operating system fall 98
 */
public class Main {
    public static int findMinIndex(List<Integer> list, int position) {
        int minDistanceIndex = 0;
        int minDistance = Math.abs(position - list.get(0));
        for (int i = 1; i < list.size(); i++) {
            int temp = Math.abs(position - list.get(i));
            if (temp < minDistance) {
                minDistance = temp;
                minDistanceIndex = i;
            }
        }
        return minDistanceIndex;
    }

    public static void main(String[] args) {
        int InitialPosition;
        if (args.length == 0) {
            Random random = new Random();
            InitialPosition = random.nextInt(5000);
            System.out.println("initial disk position generated random because you didn't pass is : " + InitialPosition);
        } else {
            InitialPosition = Integer.valueOf(args[0]);
            if (InitialPosition > 4999) {
                System.err.println("you should enter a number less than 5000, program exits with error");
                return;
            }
            System.out.println("initial disk position given is : " + InitialPosition);
        }
        //generate random 1000 cylinder number for requests
        int[] requests = new int[1000];
        for (int i = 0; i < 1000; i++) {
            requests[i] = new Random().nextInt(5000);
        }

        int result = 0;
        int position = InitialPosition;
        //FCFS
        for (int request : requests) {
            result += Math.abs(position - request);
            position = request;
        }
        System.out.println("total disk movements for FCFS approach is : " + result);

        //SSTF
        result = 0;
        position = InitialPosition;
        List<Integer> list = Arrays.stream(requests).boxed().collect(Collectors.toList());
        list.sort(Integer::compareTo);
        int minDistanceIndex = findMinIndex(list, position);

        int counter = list.size();
        for (int i = 0; i < counter; i++) {
            minDistanceIndex = findMinIndex(list, position);
            int minItemValue = list.get(minDistanceIndex);
            result += Math.abs(position - minItemValue);
            position = minItemValue;
            list.remove(minDistanceIndex);
        }
        System.out.println("total disk movements for SSTF approach is : " + result);
        //SCAN
        //assuming header is going to cylinder number 0
        //calculating complete iteration of header movement means header comes to the end
        result = 0;
        position = InitialPosition;
        list = Arrays.stream(requests).boxed().collect(Collectors.toList());
        list.sort(Integer::compareTo);
        String dir = "downward";
        int minIndex = findMinIndex(list, position);
        while (minIndex >= 0 && list.get(minIndex) > position) {
            minIndex--;
        }
        if (minIndex == -1) {
            minIndex = 0;
            dir = "upward";
        }
        counter = list.size();
        for (int i = 0; i < counter; i++) {
            result += Math.abs(position - list.get(minIndex));
            position = list.get(minIndex);
            list.remove(minIndex);
            if (dir.equals("downward")) {
                minIndex--;
            }
            if (minIndex < 0) {
                minIndex = 0;
                dir = "upward";
                if (!list.isEmpty())
                    result += 2 * position;//distance of header from beginning of the disk
            }
        }
        System.out.println("total disk movements for SCAN approach is : " + result);


        //C-SCAN
        result = 0;
        position = InitialPosition;
        list = Arrays.stream(requests).boxed().collect(Collectors.toList());
        list.sort(Integer::compareTo);
        minIndex = findMinIndex(list, position);
        while (list.get(minIndex) < position && minIndex != (list.size() - 1)) {
            minIndex++;
        }
        if (minIndex == (list.size() - 1)) {//header is at the front of all of the requests
            result += 4999 - position;
            result += 4999;
            position = 0;
        }
        counter = list.size();
        for (int i = 0; i < counter; i++) {
            result += Math.abs(position - list.get(minIndex));
            position = list.get(minIndex);
            list.remove(minIndex);
            if (!list.isEmpty() && minIndex == list.size()) {
                minIndex = 0;
                result += 4999 - position;//distance until it reaches the end of the disk
                result += 4999;//distance that it should pass to reaches the beginning of the disk
                position = 0;
            }
        }
        System.out.println("total disk movements for C-SCAN approach is : " + result);


        //LOOK
        //assuming header is going to cylinder number 0
        //calculating complete iteration of header movement means header comes to the end
        result = 0;
        position = InitialPosition;
        list = Arrays.stream(requests).boxed().collect(Collectors.toList());
        list.sort(Integer::compareTo);
        dir = "downward";
        minIndex = findMinIndex(list, position);
        //loop to find the first request that is less than the current disk header position and nearest to it
        while (minIndex >= 0 && list.get(minIndex) > position) {
            minIndex--;
        }
        if (minIndex == -1) {
            minIndex = 0;
            dir = "upward";
        }
        counter = list.size();
        for (int i = 0; i < counter; i++) {
            result += Math.abs(position - list.get(minIndex));
            position = list.get(minIndex);
            list.remove(minIndex);
            if (dir.equals("downward")) {
                minIndex--;
            }
            if (minIndex < 0) {
                minIndex = 0;
                dir = "upward";
            }
        }
        System.out.println("total disk movements for LOOK approach is : " + result);

        //C-LOOK
        result = 0;
        position = InitialPosition;
        list = Arrays.stream(requests).boxed().collect(Collectors.toList());
        list.sort(Integer::compareTo);
        minIndex = findMinIndex(list, position);
        while (list.get(minIndex) < position && minIndex != (list.size() - 1)) {
            minIndex++;
        }
        if (minIndex == (list.size() - 1)) {//header is at the front of all of the requests
            minIndex = 0;
            result += position - list.get(minIndex);
            position = list.get(minIndex);
        }
        counter = list.size();
        for (int i = 0; i < counter; i++) {
            result += Math.abs(position - list.get(minIndex));
            position = list.get(minIndex);
            list.remove(minIndex);
            if (!list.isEmpty() && minIndex == list.size()) {
                minIndex = 0;
            }
        }
        System.out.println("total disk movements for C-LOOK approach is : " + result);
    }
}
