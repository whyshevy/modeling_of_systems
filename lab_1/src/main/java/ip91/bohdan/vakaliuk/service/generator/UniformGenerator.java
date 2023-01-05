package ip91.bohdan.vakaliuk.service.generator;

import java.util.Random;
import java.util.stream.IntStream;

public class UniformGenerator {

  private double z = Integer.MIN_VALUE;

  public double[] generate(double a, double c, int size) {
    return IntStream.range(0, size)
        .mapToDouble((idx) -> generateSingleNum(a, c))
        .toArray();
  }

  private double generateSingleNum(double a, double c) {
      calculateZ(a, c);
      return z / c;
  }

  private void calculateZ(double a, double c) {
    if (z == Integer.MIN_VALUE) {
      z = new Random().nextInt((int) c);
      return;
    }

    z = a * z % c;
  }

}
