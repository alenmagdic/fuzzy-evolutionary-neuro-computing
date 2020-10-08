package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.util.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestureDetectionWindow extends JFrame {
    public static final int MAX_OUTPUT_DISPLAY_SIZE_IN_PIXELS = 200;

    private GestureDetector gestureDetector;
    private List<GestureCanvas> outputCanvases = new ArrayList<>();


    public GestureDetectionWindow(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gesture detector");

        initGUI();

        pack();
        setLocationRelativeTo(null);
    }

    private void initGUI() {
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
        addCanvasToOutputPanel(outputPanel);
        outputPanel.setBackground(Color.WHITE);

        GestureCanvas inputGestureCanvas = new GestureCanvas(true, points -> {
            showOutput(gestureDetector.getDisplayableOutputsForInput(points),outputPanel);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputGestureCanvas, new JScrollPane(outputPanel));
        splitPane.setResizeWeight(0.8);
        cp.add(splitPane);
    }

    private void addCanvasToOutputPanel(JPanel outputPanel) {
        GestureCanvas newCanvas = new GestureCanvas(false);
        newCanvas.setPreferredSize(new Dimension(MAX_OUTPUT_DISPLAY_SIZE_IN_PIXELS,MAX_OUTPUT_DISPLAY_SIZE_IN_PIXELS));
        outputCanvases.add(newCanvas);
        outputPanel.add(new JSeparator());
        outputPanel.add(newCanvas);
    }

    private void showOutput(List<List<Point>> outputs, JPanel outputPanel) {
        while(outputCanvases.size() < outputs.size()) {
            addCanvasToOutputPanel(outputPanel);
        }

        for(int i = 0; i < outputCanvases.size(); i++) {
            if(i < outputs.size()) { //provjera za slucaj da nema viška canvasa u odnosu na outputa
                outputCanvases.get(i).setPoints(outputs.get(i));
            } else {
                outputCanvases.get(i).setPoints(new ArrayList<>());
            }
        }

        outputPanel.updateUI();
    }


}
