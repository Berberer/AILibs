package jaicore.graphvisualizer.gui.dataVisualizer;



import com.google.common.eventbus.Subscribe;

import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;



public class LandscapeVisualizer implements IVisualizer {

    ScatterChart<Number, Number> chart;
    XYChart.Series series;

    public LandscapeVisualizer() {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        y.setLabel("f");
        x.setLabel("Index");
        series = new XYChart.Series();
        series.setName("Landscape");
        chart = new ScatterChart<>(x, y);
        chart.getData().add(series);
    }

    @Override
    public Node getVisualization() {
        return chart;
    }

    @Subscribe
    public void receiveEnumeratedEvaluationEvent(EnumeratedEvaluationEvent event) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data(event.getIndex(), event.getEvaluation()));
        });
    }

    @Override
    public String getSupplier() {
        return "EnumeratedEvaluationsSupplier";
    }

    @Override
    public String getTitle() {
        return "Landscape";
    }
}

