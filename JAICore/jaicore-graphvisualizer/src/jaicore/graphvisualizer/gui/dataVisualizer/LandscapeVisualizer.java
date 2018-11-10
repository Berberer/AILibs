package jaicore.graphvisualizer.gui.dataVisualizer;



import com.google.common.eventbus.Subscribe;

import jaicore.graphvisualizer.enumerate.ListEnumerator;
import jaicore.graphvisualizer.events.controlEvents.NodePushed;
import jaicore.graphvisualizer.events.graphEvents.EnumeratedEvaluationBackwardEvent;
import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class LandscapeVisualizer implements IVisualizer {


    private final ScatterChart<String, Number> chart;
    private final XYChart.Series<String, Number> evaluatedSeries = new XYChart.Series<String, Number>();
    private final XYChart.Series<String, Number> openSeries = new XYChart.Series<String, Number>();
    private final XYChart.Series<String, Number> pressedSeries = new XYChart.Series<String, Number>();

    private final ArrayList<EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList>> buffer = new ArrayList<>(25);

    private final ArrayList<ListEnumerator.EnumerationList> indices = new ArrayList<>();


    public LandscapeVisualizer() {
        final CategoryAxis x = new CategoryAxis();  // Extends Axis<String>
        final NumberAxis y = new NumberAxis();
        y.setLabel("f");
        x.setLabel("Index");

        evaluatedSeries.setName("Landscape");
        openSeries.setName("Open");
        pressedSeries.setName("Pressed");

        chart = new ScatterChart<>(x, y);
        chart.setAnimated(false);
        chart.getData().add(evaluatedSeries);
        chart.getData().add(openSeries);
        chart.getData().add(pressedSeries);
    }

    @Override
    public Node getVisualization() {
        return chart;
    }

    /**
     * Deprecated. See `recieveEnumeratedEvaluationEventBatch`.
     * @param event
     */
    @Subscribe
    public void receiveEnumeratedEvaluationEvent(EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList> event) {
        // Buffer the events.
        buffer.add(event);
        if (buffer.size() == 25) {
            ArrayList<EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList>> batch = new ArrayList<>(buffer);
            buffer.clear();
            processBatch(batch);
        }
    }

    @Subscribe
    public void receiveEnumeratedEvaluationEventBatch(ArrayList<EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList>> batch) {
        processBatch(batch);
    }

    @Subscribe
    public void receiveEnumeratedEvaluationsBackwardEvent(EnumeratedEvaluationBackwardEvent event) {
        System.out.println("Backward");
        // Remove latest data.
        Platform.runLater(()->{
            int dataSize = evaluatedSeries.getData().size();
            evaluatedSeries.getData().remove(dataSize-event.getSteps(), dataSize);
        });
        // Remove latest indices and replot open series.
        List<ListEnumerator.EnumerationList> indicesToRemove = indices.subList(indices.size()-event.getSteps(), indices.size());
        indices.removeAll(indicesToRemove);
        calculateOpenSeries();
    }

    private void processBatch(ArrayList<EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList>> batch) {
        List<XYChart.Data> newData = batch
                .stream()
                .map(e->new XYChart.Data<>(e.getIndex().toString(), e.getEvaluation()))
                .collect(Collectors.toList());
        XYChart.Data[] newDataArray = newData.toArray(new XYChart.Data[newData.size()]);
        List<ListEnumerator.EnumerationList> newIndices = batch
                .stream()
                .map(e->(ListEnumerator.EnumerationList)e.getIndex())
                .collect(Collectors.toList());
        Platform.runLater(() -> {
            evaluatedSeries.getData().addAll(newDataArray);
            evaluatedSeries.getData().sort(new Comparator<XYChart.Data<String, Number>>() {
                @Override
                public int compare(XYChart.Data<String, Number> o1, XYChart.Data<String, Number> o2) {
                    return o1.getXValue().compareTo(o2.getXValue());
                }
            });
            indices.addAll(newIndices);
            calculateOpenSeries();
        });
    }



    /**
     * Calculates and displays the open series, respec. displays points, if there is
     * a possible node between any pair of nodes of the current displayed nodes.
     */
    private void calculateOpenSeries() {
        // Clear current open series.
        openSeries.getData().clear();
        // Calculate new open series and collect the new data points in batch.
        List<XYChart.Data> batch = new ArrayList<>();
        for(int i=0; i<indices.size()-1; i++) {
            ListEnumerator.EnumerationList index = indices.get(i);
            ListEnumerator.EnumerationList index_ = indices.get(i+1);
            if(index.siblingClone().compareTo(index_) != 0) {
                batch.add(new XYChart.Data<>(index.toString(), 0));
            }
        }
        // Add the batch to the series.
        XYChart.Data[] batchArray = batch.toArray(new XYChart.Data[batch.size()]);
        openSeries.getData().addAll(batchArray);
    }

    @Subscribe
    public void nodePushedEvent(NodePushed event) {
        // The node push event contains a package jaicore.search.model.travesaltree.Node;
        // Since jaicore.search depends on jaicore.graphvisuakizer i can not access the external lable here.
        // Why do I receive a Node here???? It should be the external label.
//        assert event.getNode() instanceof EnumeratedNode;
//        assert ((EnumeratedNode) event.getNode()).getIndex() instanceof ListEnumerator.EnumerationList;
//        Platform.runLater(() -> {
//            ListEnumerator.EnumerationList index = (ListEnumerator.EnumerationList)((EnumeratedNode) event.getNode()).getIndex();
//            ListEnumerator.EnumerationList index_ = index.siblingClone();
//
//            XYChart.Data<String, Number> start = new XYChart.Data<>(index.toString(), 0);
//            XYChart.Data<String, Number> end = new XYChart.Data<>(index_.toString(), 0);
//
//            pressedSeries.getData().add(start);
//            pressedSeries.getData().add(end);
//        });
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

