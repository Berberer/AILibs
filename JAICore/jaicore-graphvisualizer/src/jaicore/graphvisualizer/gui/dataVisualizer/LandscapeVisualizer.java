package jaicore.graphvisualizer.gui.dataVisualizer;



import com.google.common.eventbus.Subscribe;

import jaicore.graphvisualizer.enumerate.EnumeratedNode;
import jaicore.graphvisualizer.enumerate.ListEnumerator;
import jaicore.graphvisualizer.events.controlEvents.NodePushed;
import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.*;


public class LandscapeVisualizer implements IVisualizer {

    ScatterChart<String, Number> chart;

    XYChart.Series<String, Number> evaluatedSeries;
    XYChart.Series<String, Number> openSeries;


    public LandscapeVisualizer() {
        CategoryAxis x = new CategoryAxis();  // Extends Axis<String>
        NumberAxis y = new NumberAxis();
        y.setLabel("f");
        x.setLabel("Index");

        evaluatedSeries = new XYChart.Series<String, Number>();
        evaluatedSeries.setName("Landscape");

        chart = new ScatterChart<>(x, y);
        chart.setAnimated(false);
        chart.getData().add(evaluatedSeries);
    }

    @Override
    public Node getVisualization() {
        return chart;
    }

    @Subscribe
    public void receiveEnumeratedEvaluationEvent(EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList> event) {
        assert event.getIndex() instanceof ListEnumerator.EnumerationList;
        assert event.getEvaluation() instanceof Double;
        Platform.runLater(() -> {
            String x = event.getIndex().toString();
            evaluatedSeries.getData().add(
                    new XYChart.Data<>(x, event.getEvaluation())
            );
        });
    }

    @Subscribe
    public void nodePushedEvent(NodePushed<EnumeratedNode<?, ListEnumerator.EnumerationList>> event) {
        ListEnumerator.EnumerationList index = event.getNode().getIndex();
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

