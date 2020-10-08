package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.util.Range;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Vector;

public class GestureDetectorTrainingSetCreatorApp extends JFrame {
    private static final int DEFAULT_REPRESENTATIVE_DOTS = 20;
    private GestureDetectorTrainingSetBuilder setBuilder = new GestureDetectorTrainingSetBuilder();

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(
                () -> new GestureDetectorTrainingSetCreatorApp().setVisible(true));
    }

    public GestureDetectorTrainingSetCreatorApp() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gesture detector training set creator");

        initGUI();

        pack();
        setLocationRelativeTo(null);
    }

    private void initGUI() {
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        GestureCanvas gc = new GestureCanvas(true);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout());

        JPanel classPanel = new JPanel();
        classPanel.setLayout(new BoxLayout(classPanel,BoxLayout.X_AXIS));
        JSpinner classSpinner = new JSpinner();
        classSpinner.setValue(1);
        classSpinner.setPreferredSize(new Dimension(50,30));
        classPanel.add(new JLabel("Razred geste: "));
        classPanel.add(classSpinner);
        formPanel.add(classPanel);

        JButton addGestureButton = new JButton("Dodaj gestu u skup");
        formPanel.add(addGestureButton);

        JPanel dotsParamPanel = new JPanel();
        dotsParamPanel.setLayout(new BoxLayout(dotsParamPanel,BoxLayout.X_AXIS));
        dotsParamPanel.add(new JLabel("Broj reprezentativnih točaka: "));
        JTextField dotsParamInput = new JTextField();
        dotsParamInput.setPreferredSize(new Dimension(70,30));
        dotsParamInput.setText(Integer.toString(DEFAULT_REPRESENTATIVE_DOTS));
        dotsParamPanel.add(dotsParamInput);
        formPanel.add(dotsParamPanel);

        JButton saveButton = new JButton("Spremi");
        formPanel.add(saveButton);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Razred");
        columnNames.add("Broj uzoraka");
        DefaultTableModel tableModel = new DefaultTableModel(columnNames,0);
        sidePanel.add(new JScrollPane(new JTable(tableModel)),BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gc,BorderLayout.CENTER);
        mainPanel.add(formPanel,BorderLayout.PAGE_END);
        cp.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(mainPanel),sidePanel));

        addGestureButton.addActionListener(e -> {
            int classNumber = (int)classSpinner.getValue();
            setBuilder.addSample(classNumber,gc.getPoints());
            updateGestureSamplesTableWithNewSample(tableModel,classNumber);
        });

        saveButton.addActionListener( e -> onSaveButtonClicked(dotsParamInput));
    }

    private void onSaveButtonClicked(JTextField dotsParamInput) {
        JFileChooser fc = new JFileChooser(".");
        int retValue = fc.showSaveDialog(this);
        if(retValue != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path exportFilePath = fc.getSelectedFile().toPath();
        Range dotsRange;
        try {
            dotsRange = parseRange(dotsParamInput.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Vrijednost raspona reprezentativnih točaka je neispravna.");
            return;
        }

        try {
            setBuilder.exportSamples(dotsRange,exportFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Došlo je do pogreške pri spremanju podataka.");
            return;
        }
        JOptionPane.showMessageDialog(this,"Podaci uspješno pohranjeni.");
    }

    private Range parseRange(String input) throws ParseException {
        String[] inputParts = input.split("\\.");
        try {
            if(inputParts.length == 1) {
                return new Range(Integer.parseInt(inputParts[0]));
            } else if (inputParts.length == 2) {
                return new Range(Integer.parseInt(inputParts[0]), Integer.parseInt(inputParts[1]));
            } else if (inputParts.length == 3) {
                return new Range(Integer.parseInt(inputParts[0]), Integer.parseInt(inputParts[1]), Integer.parseInt(inputParts[2]));
            } else {
                throw new ParseException("Input sadrzi previse podataka", 0);
            }
        } catch (NumberFormatException ex) {
            throw new ParseException("Input je neispravan.",0);
        }
    }

    private void updateGestureSamplesTableWithNewSample(DefaultTableModel tableModel, int newSampleClassNumber) {
        boolean isExistingClass = false;
        for(int row = 0; row < tableModel.getRowCount(); row++) {
            int classNum = (int)tableModel.getValueAt(row,0);
            if(classNum == newSampleClassNumber) {
                int oldNumberOfSamplesInClass = (int)tableModel.getValueAt(row,1);
                tableModel.setValueAt(oldNumberOfSamplesInClass+1,row,1);
                isExistingClass = true;
                break;
            }
        }

        if(!isExistingClass) {
            tableModel.addRow(new Object[]{newSampleClassNumber,1});
        }

    }


}
