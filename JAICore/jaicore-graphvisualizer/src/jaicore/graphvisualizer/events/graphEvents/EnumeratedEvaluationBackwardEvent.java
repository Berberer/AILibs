package jaicore.graphvisualizer.events.graphEvents;

public class EnumeratedEvaluationBackwardEvent implements GraphEvent {
    final int steps;

    public EnumeratedEvaluationBackwardEvent(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
