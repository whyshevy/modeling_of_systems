package ip91.bohdan.vakaliuk.service;

import ip91.bohdan.vakaliuk.model.Interval;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntervalInfoUtil {

  private IntervalInfoUtil() {}

  public static List<Integer> findFrequencyList(double[] array, int k, double minEl, double maxEl, double h) {
    int[] frequency = new int[k];

    for (double num : array) {
      if (num == maxEl) {
        frequency[k - 1]++;
        continue;
      }

      int intervalNum = (int) ((num - minEl) / h);
      frequency[intervalNum]++;
    }

    return Arrays.stream(frequency).boxed().collect(Collectors.toList());
  }

  public static List<Interval> findIntervals(int k, double minEl, double h) {
    return IntStream.range(0, k)
        .mapToObj(idx -> new Interval(minEl + h * idx, minEl + h * (idx + 1)))
        .collect(Collectors.toList());
  }

}
