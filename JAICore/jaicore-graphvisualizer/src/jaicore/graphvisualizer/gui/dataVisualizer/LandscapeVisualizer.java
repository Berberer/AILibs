package jaicore.graphvisualizer.gui.dataVisualizer;



import com.google.common.eventbus.Subscribe;

import jaicore.graphvisualizer.enumerate.EnumeratedNode;
import jaicore.graphvisualizer.enumerate.ListEnumerator;
import jaicore.graphvisualizer.events.controlEvents.NodePushed;
import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Objects;

/**
 * How to parametrize this LandscapeVisualizer if its get loaded/instanciated from the classpath (not easy possible).
 *
 * Using V in GraphVisualizer. But this is not the V used in search (for evaluation)...confusing.
 *
 * Everything is confusing here.
 *
 * The node push event contains a package jaicore.search.model.travesaltree.Node;
 * Since jaicore.search depends on jaicore.graphvisuakizer i can not access the external lable here.
 *
 * Think about a central storage for everything that might be interesting for the GUI (also let the click events
 * flow through this storage). Then every component can subscribe to/take the data from the storage and access it
 * (Thats how its done in WebDevelopment (redux patter) at least and I think its can be beneficial here as well (seperated model-view
 * and keeps every view-component consistent)
 *
 * The javafx charts (espacilly CategoryAxes) seems to be not that performant with big data.
 *
 * Calculating the places where there is a possible unexplored sibling between to node is useless, since there
 * is almost always one -> everywhere are scatters and you wont see anything.
 */

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

    @Subscribe
    public void receiveEnumeratedEvaluationEvent(EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList> event) {
//        assert event.getIndex() instanceof ListEnumerator.EnumerationList;
//        assert event.getEvaluation() instanceof Double;
        // Buffer the events.
        buffer.add(event);

        if (buffer.size() == 25) {
            ArrayList<EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList>> eventsToProcess = new ArrayList<>(buffer);
            buffer.clear();
            Platform.runLater(() -> {
                for (EnumeratedEvaluationEvent<Double, ListEnumerator.EnumerationList> e: eventsToProcess) {
                    evaluatedSeries.getData().add(new XYChart.Data<>(event.getIndex().toString(), event.getEvaluation()));
                    indices.add(e.getIndex());
                }
                calculateOpenSeries();
            });
        }
    }

    private void calculateOpenSeries() {
        openSeries.getData().clear();
        for(int i=0; i<indices.size()-1; i++) {
            ListEnumerator.EnumerationList index = indices.get(i);
            ListEnumerator.EnumerationList index_ = indices.get(i+1);
            if(index.siblingClone().compareTo(index_) < 0) {
                openSeries.getData().add(new XYChart.Data<>(index.toString(), 0));
            }
        }
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

