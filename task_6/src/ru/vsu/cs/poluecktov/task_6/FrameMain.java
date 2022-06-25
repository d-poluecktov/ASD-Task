package ru.vsu.cs.poluecktov.task_6;

import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class FrameMain extends JFrame {
    private JPanel panelMain;
    private JTable tableInput;
    private JButton buttonLoadInputFromFile;
    private JButton buttonSaveInputInfoFile;
    private JButton addNewElement;
    private JTextField textFieldKey;
    private JTextField textFieldValue;
    private JTextField textFieldPrice;
    private JButton DeleteElementButton;
    private JButton getPriceButton;
    private JTextField numberTextField;
    private JTextField textFieldKeyDelete;


    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;
    private JMenuBar menuBarMain;
    private JMenu menuLookAndFeel;


    public FrameMain() {
        class Inner {
            void showInfo(RndBSTreeMap<Integer, String> map) {
                int i = 0;
                String[][] table = new String[map.size()][2];
                for(Integer key : map.keySet()) {
                    table[i][0] = String.valueOf(key);
                    table[i][1] = map.get(key);
                    i++;
                }
                JTableUtils.writeArrayToJTable(tableInput, table);
            }
        }
        this.setTitle("FrameMain");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableInput, 40, false, true, false, false);


        tableInput.setRowHeight(30);
        tableInput.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


        fileChooserOpen = new JFileChooser();
        fileChooserSave = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        fileChooserSave.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text files", "csv");
        fileChooserOpen.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filter);

        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        menuBarMain = new JMenuBar();
        setJMenuBar(menuBarMain);

        menuLookAndFeel = new JMenu();
        menuLookAndFeel.setText("Вид");
        menuBarMain.add(menuLookAndFeel);
        SwingUtils.initLookAndFeelMenu(menuLookAndFeel);


        this.pack();

        RndBSTreeMap<Integer, String> map = new RndBSTreeMap<>();


        buttonLoadInputFromFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooserOpen.getSelectedFile().getPath();
                    File file = new File(path);
                    FileReader fileReader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line = reader.readLine();
                    List<String> rows = new ArrayList<>();
                    while(line != null) {
                        rows.add(line);
                        line = reader.readLine();
                    }
                    String[][] arr = new String[rows.size()][2];
                    int i = 0;
                    for (String string: rows) {
                        String[] row = string.split(",");
                        arr[i][0] = row[0];
                        arr[i][1] = row[1];
                        i++;
                        map.put(Integer.parseInt(row[0]), row[1]);
                    }
                    JTableUtils.writeArrayToJTable(tableInput, arr);

                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonSaveInputInfoFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    String[][] matrix = JTableUtils.readStringMatrixFromJTable(tableInput);
                    String file = fileChooserSave.getSelectedFile().getPath();
                    if (!file.toLowerCase().endsWith(".csv")) {
                        file += ".csv";
                    }
                    com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(new FileWriter(file), ',', com.opencsv.CSVWriter.NO_QUOTE_CHARACTER,
                            com.opencsv.CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                            com.opencsv.CSVWriter.DEFAULT_LINE_END);
                    for(int i = 0; i < matrix.length; i++) {
                        writer.writeNext(matrix[i]);
                    }
                    writer.close();
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        addNewElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(textFieldKey.getText());
                String value = textFieldValue.getText();
                map.put(key, value);
                Inner inner = new Inner();
                inner.showInfo(map);
                textFieldKey.setText("");
                textFieldValue.setText("");
            }
        });
        DeleteElementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(textFieldKeyDelete.getText());
                if (!map.containsKey(key)) {
                    SwingUtils.showInfoMessageBox("Элемента с таким ключом нет");
                } else {
                    map.remove(key);
                    Inner inner = new Inner();
                    inner.showInfo(map);
                }
            }
        });

    }
}

