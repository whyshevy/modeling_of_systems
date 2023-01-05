package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.model.GenType;
import ip91.bohdan.vakaliuk.model.HistogramData;
import ip91.bohdan.vakaliuk.model.Interval;
import ip91.bohdan.vakaliuk.service.Histogram;
import ip91.bohdan.vakaliuk.service.HistogramUtil;
import ip91.bohdan.vakaliuk.service.IntervalInfoUtil;
import ip91.bohdan.vakaliuk.service.SequenceInfoUtil;
import ip91.bohdan.vakaliuk.service.generator.ExpGenerator;
import ip91.bohdan.vakaliuk.service.generator.NormalGenerator;
import ip91.bohdan.vakaliuk.service.generator.UniformGenerator;

import java.util.List;

import static ip91.bohdan.vakaliuk.config.Config.*;

public class App {

  private final Histogram histogram;

  public App() {
    histogram = new Histogram();
  }

  public void run(GenType genType) {
    double[] sequence = generate(genType);

    System.out.println("Average num: " + SequenceInfoUtil.findAverageNumber(sequence));
    System.out.println("Dispersion: " + SequenceInfoUtil.findDispersion(sequence));

    HistogramData histogramData = prepareData(sequence);

    histogram.draw(histogramData);
  }

  private double[] generate(GenType genType) {
    return switch (genType) {
      case EXP -> new ExpGenerator().generate(EXP_LAMBDA, SEQ_SIZE);
      case NORMAL -> new NormalGenerator().generate(NORMAL_ALPHA, NORMAL_SIGMA, SEQ_SIZE);
      case UNIFORM -> new UniformGenerator().generate(UNIFORM_A, UNIFORM_C, SEQ_SIZE);
    };
  }

  private HistogramData prepareData(double[] sequence) {
    final double minEl = SequenceInfoUtil.findMinEl(sequence);
    final double maxEl = SequenceInfoUtil.findMaxEl(sequence);
    final double intervalLength = SequenceInfoUtil.findIntervalLength(minEl, maxEl, INTERVAL_COUNT);

    List<Interval> intervals = IntervalInfoUtil.findIntervals(INTERVAL_COUNT, minEl, intervalLength);
    List<Integer> frequency = IntervalInfoUtil.findFrequencyList(sequence, INTERVAL_COUNT, minEl, maxEl, intervalLength);

    return HistogramUtil.prepareHistogramData(intervals, frequency);
  }

}
