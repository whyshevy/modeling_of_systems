package ip91.bohdan.vakaliuk.util;

import java.util.HashMap;
import java.util.Map;

public class X2Critical {

  private static final Map<Integer, Double> TABLE = new HashMap<>();

  static {
    TABLE.put(1, 3.8);
    TABLE.put(2, 6.0);
    TABLE.put(3, 7.8);
    TABLE.put(4, 9.5);
    TABLE.put(5, 11.1);
    TABLE.put(6, 12.6);
    TABLE.put(7, 14.1);
    TABLE.put(8, 15.5);
    TABLE.put(9, 16.9);
    TABLE.put(10, 18.3);
    TABLE.put(11, 19.7);
    TABLE.put(12, 21.0);
    TABLE.put(13, 22.4);
    TABLE.put(14, 23.7);
    TABLE.put(15, 25.0);
    TABLE.put(16, 26.3);
    TABLE.put(17, 27.6);
    TABLE.put(18, 28.9);
    TABLE.put(19, 30.1);
  }

  private X2Critical() {}

  public static double getX2Critical(int k, int paramCount) {
    final int freedomDegree = k - 1 - paramCount;

    if (freedomDegree < 1 || freedomDegree > 19) {
      throw new RuntimeException("Can't find X2_CRITICAL: check number of generated intervals!");
    }

    return TABLE.get(freedomDegree);
  }

}
