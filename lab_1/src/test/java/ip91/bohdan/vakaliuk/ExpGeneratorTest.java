package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.model.Interval;
import ip91.bohdan.vakaliuk.service.IntervalInfoUtil;
import ip91.bohdan.vakaliuk.service.SequenceInfoUtil;
import ip91.bohdan.vakaliuk.service.generator.ExpGenerator;
import ip91.bohdan.vakaliuk.util.X2Critical;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ip91.bohdan.vakaliuk.util.UtilTest.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpGeneratorTest {

  private static final int INTERVAL_COUNT = 20;
  private static final int SIZE = 10000;
  private static final int PARAM_COUNT = 1;

  private ExpGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new ExpGenerator();
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForExpTesting")
  void shouldSatisfyExpDistributionBasedOnX2Criteria(double lambda) {
    double[] sequence = generator.generate(lambda, SIZE);

    List<Integer> actualFr = getActualFrequencyList(sequence);
    List<Integer> expectedFr = getExpectedFrequencyList(sequence, lambda);

    System.out.println("------------------");
    System.out.println("actualFr (before joining): " + actualFr);
    System.out.println("expectedFr (before joining): " + expectedFr);

    Map<String, List<Integer>> joinedFr = joinIntervalsIfNeed(actualFr, expectedFr);
    final int NEW_INTERVAL_COUNT = joinedFr.get(JOINED_ACTUAL_FR).size();

    System.out.println("actualFr (after joining): " + joinedFr.get(JOINED_ACTUAL_FR));
    System.out.println("expectedFr (after joining): " + joinedFr.get(JOINED_EXPECTED_FR));

    final double X2 = processX2(joinedFr.get(JOINED_ACTUAL_FR), joinedFr.get(JOINED_EXPECTED_FR));
    final double X2_CRITICAL = X2Critical.getX2Critical(NEW_INTERVAL_COUNT, PARAM_COUNT);

    System.out.println("X2: " + X2 + " | X2_CRITICAL: " + X2_CRITICAL);

    assertTrue(X2 < X2_CRITICAL);
  }

  private List<Integer> getActualFrequencyList(double[] sequence) {
    double minEl = SequenceInfoUtil.findMinEl(sequence);
    double maxEl = SequenceInfoUtil.findMaxEl(sequence);
    double intervalLength = SequenceInfoUtil.findIntervalLength(minEl, maxEl, INTERVAL_COUNT);

    return IntervalInfoUtil.findFrequencyList(sequence, INTERVAL_COUNT, minEl, maxEl, intervalLength);
  }

  private static List<Integer> getExpectedFrequencyList(double[] sequence, double lambda) {
    double minEl = SequenceInfoUtil.findMinEl(sequence);
    double maxEl = SequenceInfoUtil.findMaxEl(sequence);
    double intervalLength = SequenceInfoUtil.findIntervalLength(minEl, maxEl, INTERVAL_COUNT);
    List<Interval> intervals = IntervalInfoUtil.findIntervals(INTERVAL_COUNT, minEl, intervalLength);
    int[] frequency = new int[INTERVAL_COUNT];

    for (int idx = 0; idx < INTERVAL_COUNT; idx++) {
      Interval interval = intervals.get(idx);
      frequency[idx] = (int) Math.round(SIZE * (Math.exp(-lambda * interval.getStart()) - Math.exp(-lambda * interval.getEnd())));
    }

    return Arrays.stream(frequency).boxed().collect(Collectors.toList());
  }

  private static Stream<Arguments> provideArgumentsForExpTesting() {
    return Stream.of(
        Arguments.of(2),
        Arguments.of(5),
        Arguments.of(10),
        Arguments.of(20),
        Arguments.of(0.5)
    );
  }

}