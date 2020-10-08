package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.util.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GestureCanvas extends JComponent {
    private static final Dimension DEFAULT_PREFERRED_DIMENSION = new Dimension(800,600);
    private Dimension preferredDimension = DEFAULT_PREFERRED_DIMENSION;
    private List<hr.fer.zemris.nenr.dz5.util.Point> points;
    private GestureCanvasListener listener;

    public GestureCanvas(boolean drawable) {
        this(drawable,null);
    }

    public GestureCanvas(boolean drawable,GestureCanvasListener listener) {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        if(drawable) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    points.clear();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if(listener!=null) {
                        listener.onGestureDrawn(getPoints());
                    }
                }
            });
            addMouseMotionListener(
                    new MouseMotionListener() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            Point p = new Point(e.getX(), e.getY());
                            points.add(p);
                            repaint();
                        }

                        @Override
                        public void mouseMoved(MouseEvent e) {
                        }
                    }
            );
        }

        points = new ArrayList<>();
    }

    public void setPoints(List<Point> points) {
        this.points = new ArrayList<>(points);
        repaint();
    }

    public List<Point> getPoints() {
        return new ArrayList<>(points);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension dim = getSize();
        Insets ins = getInsets();

        g.setColor(getBackground());
        g.fillRect(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);

        g.setColor(getForeground());
        for(int i = 1, n = points.size(); i < n; i++) {
            hr.fer.zemris.nenr.dz5.util.Point previousPoint = points.get(i-1);
            Point point = points.get(i);
            g.drawLine(previousPoint.getX(),previousPoint.getY(),point.getX(),point.getY());
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredDimension = preferredSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredDimension;
    }

    public interface GestureCanvasListener {
        void onGestureDrawn(List<Point> points);
    }
}
