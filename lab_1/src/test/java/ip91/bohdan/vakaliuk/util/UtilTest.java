package ip91.bohdan.vakaliuk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilTest {

  private static final int MIN_INTERVAL_LENGTH = 5;
  public static final String JOINED_ACTUAL_FR = "joinedActualFr";
  public static final String JOINED_EXPECTED_FR = "joinedExpectedFr";

  public static double processX2(List<Integer> actualFr, List<Integer> expectedFr) {
    double X2 = 0;

    for (int idx = 0; idx < actualFr.size(); idx++) {
      X2 += Math.pow(actualFr.get(idx) - expectedFr.get(idx), 2) / expectedFr.get(idx);
    }

    return X2;
  }

  public static Map<String, List<Integer>> joinIntervalsIfNeed(List<Integer> actualFr, List<Integer> expectedFr) {
    Map<String, List<Integer>> result = new HashMap<>();
    List<Integer> joinedActualFr = new ArrayList<>();
    List<Integer> joinedExpectedFr = new ArrayList<>();

    int joinedIdx = 0;
    for (int idx = 0; idx < actualFr.size(); idx++) {
      if (joinedActualFr.size() <= joinedIdx) {
        joinedActualFr.add(actualFr.get(idx));
        joinedExpectedFr.add(expectedFr.get(idx));
      } else {
        joinedActualFr.set(joinedIdx, joinedActualFr.get(joinedIdx) + actualFr.get(idx));
        joinedExpectedFr.set(joinedIdx, joinedExpectedFr.get(joinedIdx) + expectedFr.get(idx));
      }
      joinedIdx = joinedActualFr.get(joinedIdx) < MIN_INTERVAL_LENGTH ? joinedIdx : joinedIdx + 1;
    }

    if (joinedActualFr.get(joinedActualFr.size() - 1) < MIN_INTERVAL_LENGTH) {
      final int size = joinedActualFr.size();
      joinedActualFr.set(size - 2, joinedActualFr.get(size - 2) + joinedActualFr.remove(size - 1));
      joinedExpectedFr.set(size - 2, joinedExpectedFr.get(size - 2) + joinedExpectedFr.remove(size - 1));
    }

    result.put(JOINED_ACTUAL_FR, joinedActualFr);
    result.put(JOINED_EXPECTED_FR, joinedExpectedFr);
    return result;
  }

}
