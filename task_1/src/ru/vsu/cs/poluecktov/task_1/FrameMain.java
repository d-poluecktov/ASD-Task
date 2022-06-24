package ru.vsu.cs.poluecktov.task_1;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;


public class FrameMain extends JFrame {
    private JPanel panelMain;
    private JTable tableInput;
    private JButton buttonLoadInputFromFile;
    private JButton buttonSaveInputInfoFile;
    private JButton addTariffButton;
    private JTextField textFieldName;
    private JTextField textFieldPrefix;
    private JTextField textFieldPrice;
    private JButton DeleteTariffButton;
    private JSpinner spinnerTariffIndex;
    private JButton modifyTariffButton;
    private JSpinner spinnerIndexes;
    private JTextField textFieldNewValue;
    private JComboBox comboBoxParams;


    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;
    private JMenuBar menuBarMain;
    private JMenu menuLookAndFeel;


    public FrameMain() {
        class Inner {
            void showInfo(TariffsList list) {
                String[][] table = new String[list.size()][4];
                for (int i = 1; i <= list.size(); i++) {
                    table[i - 1][0] = Integer.toString(i);
                    TariffsList.Tariff tariff = list.getTariffByIndex(i - 1);
                    table[i - 1][1] = tariff.getNameDirection();
                    table[i - 1][2] = Integer.toString(tariff.getCode());
                    table[i - 1][3] = Double.toString(tariff.getPrice());
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
        comboBoxParams.addItem(new String("Направление"));
        comboBoxParams.addItem(new String("Префикс"));
        comboBoxParams.addItem(new String("Цена"));

        this.pack();

        TariffsList tariffsList = new TariffsList();


        buttonLoadInputFromFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    String file = fileChooserOpen.getSelectedFile().getPath();
                    CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
                    CSVReader reader = new com.opencsv.CSVReaderBuilder(new FileReader(file)).withCSVParser(csvParser).withSkipLines(1).build();
                    List<String[]> rows = reader.readAll();
                    String[][] arr = new String[rows.size()][4];
                    int i = 0;
                    for (String[] row: rows) {
                        arr[i][0] = row[0];
                        arr[i][1] = row[1];
                        arr[i][2] = row[2];
                        arr[i][3] = row[3];
                        i++;
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
                    CSVWriter writer = new CSVWriter(new FileWriter(file));
                    for(int i = 0; i < matrix.length; i++) {
                        writer.writeNext(matrix[i]);
                    }
                    writer.close();
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        addTariffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textFieldName.getText();
                int prefix = Integer.parseInt(textFieldPrefix.getText());
                double price = Double.parseDouble(textFieldPrice.getText());
                tariffsList.addTariff(name, prefix, price);
                Inner inner = new Inner();
                inner.showInfo(tariffsList);
                textFieldName.setText("");
                textFieldPrefix.setText("");
                textFieldPrice.setText("");
            }
        });
        DeleteTariffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = ((Number) spinnerTariffIndex.getValue()).intValue();
                if (index > tariffsList.size() || index < 1) {
                    SwingUtils.showInfoMessageBox("Тарифа с таким номером нет");
                } else {
                    tariffsList.removeTariff(index - 1);
                    Inner inner = new Inner();
                    inner.showInfo(tariffsList);
                }
            }
        });
        modifyTariffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String param = comboBoxParams.getSelectedItem().toString();
                int index = ((Number) spinnerIndexes.getValue()).intValue();
                if(index <= tariffsList.size() && index >= 1) {
                    if (param.equals("Направление")) {
                        String name = textFieldNewValue.getText();
                        tariffsList.modifyDirectionName(index-1, name);
                    }
                    if(param.equals("Префикс")) {
                        int prefix = Integer.parseInt(textFieldNewValue.getText());
                        tariffsList.modifyCode(index - 1, prefix);
                    }
                    if(param.equals("Цена")) {
                        double price = Double.parseDouble(textFieldNewValue.getText());
                        tariffsList.modifyPrice(index - 1, price);
                    }
                    textFieldNewValue.setText("");
                    Inner inner = new Inner();
                    inner.showInfo(tariffsList);
                } else {
                    SwingUtils.showInfoMessageBox("Тарифа с таким номером нет");
                }
            }
        });
    }
}

