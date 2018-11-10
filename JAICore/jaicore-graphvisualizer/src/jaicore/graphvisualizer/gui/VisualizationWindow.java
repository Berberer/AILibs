package jaicore.graphvisualizer.gui;

import jaicore.graph.IGraphAlgorithm;
import jaicore.graphvisualizer.TooltipGenerator;
import jaicore.graphvisualizer.gui.dataSupplier.ISupplier;
import jaicore.graphvisualizer.gui.dataSupplier.TooltipSupplier;
import jaicore.graphvisualizer.gui.dataVisualizer.IVisualizer;
import jaicore.graphvisualizer.gui.dataVisualizer.LandscapeVisualizer;
import javafx.application.Platform;

/**
 * Class which creates a thread and a VisualizationWindow.
 * 
 * @author jkoepe
 */
public class VisualizationWindow<V, E> {
	/**
	 * The Javafx-thread which contains the GUI
	 */
	static Thread fxThread;

	/**
	 * A recorder which is connected to the algorithm
	 */
	Recorder recorder;


	private TooltipSupplier tooltipSupplier;

	public VisualizationWindow(IGraphAlgorithm graphAlgorithm) {
		this(graphAlgorithm, "Visualizer for " + graphAlgorithm, new String[0]);
	}

	public VisualizationWindow(IGraphAlgorithm graphAlgorithm, String title) {
		this(graphAlgorithm, title, new String[0]);
	}

	public VisualizationWindow(IGraphAlgorithm graphAlgorithm, String[] visualizers) {
		this(graphAlgorithm, "Visualizer for " + graphAlgorithm, visualizers);
	}

	/**
	 * The construction of a new VisualizationWindow.
	 *
	 * @param observable The algorithm which should be observed
	 * @param title      The title of the window
	 */
	public VisualizationWindow(IGraphAlgorithm<?, ?, V, E> observable, String title, String[] visualizers) {
		this.tooltipSupplier = new TooltipSupplier();
		this.tooltipSupplier.setGenerator(getTooltipGenerator());
		if (fxThread == null) {
			try {
				fxThread = new Thread() {
					@Override
					public void run() {
						javafx.application.Application.launch(GuiApp.class);
					}
				};
				fxThread.start();
			} catch (IllegalStateException e) {
//                e.printStackTrace();
			}
		}

		// try to create a recorder and start the gui in the fxthread.
		// if it fails to create the recorder the system is exited.
		try {
			recorder = new Recorder(observable);
			Thread.sleep(500);
			Platform.runLater(() -> {
				System.out.println("Suspending vm");
//                GuiApp app = new GuiApp();
				System.out.println("Code");
				FXCode code = new FXCode(recorder, title);
				for (String v: visualizers) {
					if (v.equals("LandscapeVisualizer")) {
						code.addVisualizerInTab(new LandscapeVisualizer());
					}
					// Initialize more visualizers here.
				}
				System.out.println("Gui started");
			});

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// this.addDataSupplier(tooltipSupplier);
	}


	public void addDataSupplier(ISupplier supplier) {
		recorder.addDataSupplier(supplier);
	}

	public Recorder getRecorder() {
		return recorder;
	}

	private TooltipGenerator<V> getTooltipGenerator() {
		return new TooltipGenerator<V>() {
			@Override
			public String getTooltip(V node) {
				return node.toString();
			}
		};
	}

	public void setTooltipGenerator(TooltipGenerator<?> generator) {
		this.tooltipSupplier.setGenerator(generator);
	}

}
