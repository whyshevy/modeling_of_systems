package ip91.bohdan.vakaliuk.hospital.model;

import ip91.bohdan.vakaliuk.hospital.service.element.base.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ElementChancePair {

  private final Element element;
  private final double chance;

}
