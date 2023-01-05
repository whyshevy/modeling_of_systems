package ip91.bohdan.vakaliuk.service;

import java.util.Arrays;

public class SequenceInfoUtil {

  public static double findAverageNumber(double[] sequence) {
    return Arrays.stream(sequence).average().orElseThrow(IllegalArgumentException::new);
  }

  public static double findDispersion(double[] sequence) {
    double mu = findAverageNumber(sequence);

    return Arrays.stream(sequence)
        .map(num -> Math.pow(num - mu, 2))
        .sum() / (sequence.length - 1);
  }

  public static double findMaxEl(double[] sequence) {
    return Arrays.stream(sequence).max().orElseThrow(IllegalArgumentException::new);
  }

  public static double findMinEl(double[] sequence) {
    return Arrays.stream(sequence).min().orElseThrow(IllegalArgumentException::new);
  }

  public static double findIntervalLength(double minEl, double maxEl, int k) {
    return (maxEl - minEl) / k;
  }

}
