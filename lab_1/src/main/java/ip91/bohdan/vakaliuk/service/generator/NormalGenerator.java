package ip91.bohdan.vakaliuk.service.generator;

import java.util.stream.IntStream;

public class NormalGenerator {

  public double[] generate(double alpha, double sigma, int size) {
    return IntStream.range(0, size)
        .mapToDouble((idx) -> generateSingleNum(alpha, sigma))
        .toArray();
  }

  private double generateSingleNum(double alpha, double sigma) {
    final double mu = calculateMu();

    return sigma * mu + alpha;
  }

  private double calculateMu() {
    return IntStream.range(1, 13)
        .mapToDouble((idx) -> {
          double a = 0;
          while (a == 0) {
            a = Math.random();
          }
          return a;
        })
        .sum() - 6;
  }

}
