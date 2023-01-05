package ip91.bohdan.vakaliuk.service;

import ip91.bohdan.vakaliuk.model.HistogramData;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

public class Histogram {

  private static final int CHART_HEIGHT = 600;
  private static final int CHART_WIDTH = 1000;
  private static final String CHART_TITLE = "Numbers distribution";
  private static final String X_AXIS_TITLE = "Value Group";
  private static final String Y_AXIS_TITLE = "Frequency";
  private static final String LEGEND_TITLE = "Value group";
  private static final double CHART_AVAILABLE_SPACE_FILL = 0.99;

  public void draw(HistogramData data) {
    CategoryChart chart = new CategoryChartBuilder().width(CHART_WIDTH).height(CHART_HEIGHT)
        .title(CHART_TITLE)
        .xAxisTitle(X_AXIS_TITLE)
        .yAxisTitle(Y_AXIS_TITLE)
        .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
    chart.getStyler().setOverlapped(true);
    chart.getStyler().setAvailableSpaceFill(CHART_AVAILABLE_SPACE_FILL);

    chart.addSeries(LEGEND_TITLE, data.getXData(), data.getYData());

    new SwingWrapper<>(chart).displayChart();
  }

}
