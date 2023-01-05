package ip91.bohdan.vakaliuk.model;

import ip91.bohdan.vakaliuk.service.element.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ElementChancePair {

  private final Element element;
  private final double chance;

}
