package com.company;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    public static class Interval {
        int left;
        int right;

        public Interval(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) throws IOException {
        for (int file_num = 1; file_num <= 10; file_num++) {
            Scanner scanner = new Scanner(Paths.get(file_num + ".in"), "UTF-8");
            int n = scanner.nextInt();
            Interval[] intervals = new Interval[n];
            Integer[] endpoints = new Integer[n * 2];
            for (int i = 0; i < n; i++) {
                int left = scanner.nextInt();
                int right = scanner.nextInt();
                intervals[i] = new Interval(left, right);
                endpoints[i * 2] = left;
                endpoints[i * 2 + 1] = -right;
            }
            scanner.close();
            // We don't need to consider the case where two intervals have the same left endpoint, since
            // all endpoints differ
            Arrays.sort(intervals, Comparator.comparingInt(o -> o.left));
            Arrays.sort(endpoints, Comparator.comparingInt(Math::abs));
            int total_length = 0;
            int diff = 0;
            int curr_left = 0;
            int curr_right = 0;
            for (int i = 0; i < n * 2; i++) {
                if (diff == 0) {
                    curr_left = endpoints[i];
                }
                diff += (endpoints[i] >= 0) ? 1 : -1;
                if (diff == 0) {
                    curr_right = -endpoints[i];
                    total_length += curr_right - curr_left;
                }
            }
            Writer out = new FileWriter(file_num + ".out");
            boolean quit = false;
            for (int i = 1; i < n; i++) {
                if (intervals[i].right < intervals[i - 1].right) {
                    out.write(String.valueOf(total_length));
                    quit = true;
                    break;
                }
            }
            if (quit) {
                out.close();
                continue;
            }
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                int unique_length = intervals[i].right - intervals[i].left;
                if ((i > 0) && (intervals[i - 1].right > intervals[i].left)) {
                    unique_length -= intervals[i - 1].right - intervals[i].left;
                }
                if ((i < n - 1) && (intervals[i].right > intervals[i + 1].left)) {
                    unique_length -= intervals[i].right - intervals[i + 1].left;
                }
                min = Math.min(min, unique_length);
            }
            out.write(String.valueOf(total_length - Math.max(min, 0)));
            out.close();
        }
    }
}
