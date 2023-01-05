package ip91.bohdan.vakaliuk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Interval {

  private final double start;
  private final double end;

}
