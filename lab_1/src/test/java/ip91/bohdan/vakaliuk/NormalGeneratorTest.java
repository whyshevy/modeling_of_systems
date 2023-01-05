package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.model.Interval;
import ip91.bohdan.vakaliuk.service.IntervalInfoUtil;
import ip91.bohdan.vakaliuk.service.SequenceInfoUtil;
import ip91.bohdan.vakaliuk.service.generator.NormalGenerator;
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
import static org.junit.jupiter.api.Assertions.*;

class NormalGeneratorTest {

  private static final int INTERVAL_COUNT = 20;
  private static final int SIZE = 10000;
  private static final int PARAM_COUNT = 2;

  private NormalGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new NormalGenerator();
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForExpTesting")
  void shouldSatisfyUniformDistributionBasedOnX2Criteria(double alpha, double sigma) {
    double[] sequence = generator.generate(alpha, sigma, SIZE);

    List<Integer> actualFr = getActualFrequencyList(sequence);
    List<Integer> expectedFr = getExpectedFrequencyList(sequence, alpha, sigma);

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

  private List<Integer> getExpectedFrequencyList(double[] sequence, double alpha, double sigma) {
    double minEl = SequenceInfoUtil.findMinEl(sequence);
    double maxEl = SequenceInfoUtil.findMaxEl(sequence);
    double intervalLength = SequenceInfoUtil.findIntervalLength(minEl, maxEl, INTERVAL_COUNT);
    List<Interval> intervals = IntervalInfoUtil.findIntervals(INTERVAL_COUNT, minEl, intervalLength);
    int[] frequency = new int[INTERVAL_COUNT];

    for (int idx = 0; idx < INTERVAL_COUNT; idx++) {
      Interval interval = intervals.get(idx);
      double x = (interval.getStart() + interval.getEnd()) / 2;
      double funcVal = (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(x - alpha, 2) / (2 * Math.pow(sigma, 2)));
      double integralVal = (interval.getEnd() - interval.getStart()) * (funcVal);
      frequency[idx] = (int) Math.round(SIZE * integralVal);
    }

    return Arrays.stream(frequency).boxed().collect(Collectors.toList());
  }

  private static Stream<Arguments> provideArgumentsForExpTesting() {
    return Stream.of(
        Arguments.of(3, 4),
        Arguments.of(8, 8),
        Arguments.of(2, 1),
        Arguments.of(100, 4),
        Arguments.of(4, 100)
    );
  }

}