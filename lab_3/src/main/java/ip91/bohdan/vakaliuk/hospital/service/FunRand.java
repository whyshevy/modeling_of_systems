package ip91.bohdan.vakaliuk.hospital.service;

import java.util.Random;

public class FunRand {


  /**
   * Generates a random value according to an exponential distribution
   *
   * @param timeMean mean value
   * @return a random value according to an exponential distribution
   */
  public static double Exp(double timeMean) {
    double a = 0;
    while (a == 0) {
      a = Math.random();
    }
    a = -timeMean * Math.log(a);

    return a;
  }

  /**
   * Generates a random value according to a uniform distribution
   *
   * @param timeMin an "a" parameter based on formula
   * @param timeMax a "c" parameter based on formula
   * @return a random value according to a uniform distribution
   */
  public static double Unif(double timeMin, double timeMax) {
    double a = 0;
    while (a == 0) {
      a = Math.random();
    }
    a = timeMin + a * (timeMax - timeMin);

    return a;
  }

  /**
   * Generates a random value according to a normal (Gauss) distribution
   *
   * @param timeMean an "a" parameter based on formula
   * @param timeDeviation a "ksi" parameter based of formula
   * @return a random value according to a normal (Gauss) distribution
   */
  public static double Norm(double timeMean, double timeDeviation) {
    double a;
    Random r = new Random();
    a = timeMean + timeDeviation * r.nextGaussian();

    return a;
  }

  public static double Erlang(double timeMean, int k) {
    double a = 0;
    for (int i = 0; i < k; i++) {
      a += Exp(timeMean);
    }

    return a;
  }

}
