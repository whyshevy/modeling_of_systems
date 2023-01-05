package ip91.bohdan.vakaliuk.service.generator;

import java.util.stream.IntStream;

public class ExpGenerator {

  public double[] generate(double lambda, int size) {
    return IntStream.range(0, size)
        .mapToDouble((idx) -> generateSingleNum(lambda))
        .toArray();
  }

  private double generateSingleNum(double lambda) {
    double a = 0;
    while (a == 0) {
      a = Math.random();
    }

    return -1 / lambda * Math.log(a);
  }

}
