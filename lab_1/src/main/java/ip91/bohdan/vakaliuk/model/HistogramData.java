package ip91.bohdan.vakaliuk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class HistogramData {

  private final List<String> xData;
  private final List<Integer> yData;

}
