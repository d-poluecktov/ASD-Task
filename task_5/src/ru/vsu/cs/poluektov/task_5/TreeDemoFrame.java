package ru.vsu.cs.poluektov.task_5;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TreeDemoFrame extends JFrame {
    private JPanel panelMain;
    private JButton buttonPreOrderTraverse;
    private JButton buttonInOrderTraverse;
    private JButton buttonPostOrderTraverse;
    private JButton buttonByLevelTraverse;
    private JTextArea textAreaSystemOut;
    private JTextField textFieldBracketNotationFirstTree;
    private JButton buttonMakeTree;
    private JSplitPane splitPaneMain;
    private JPanel firstPanelPaintArea;
    private JButton buttonSaveImage;
    private JCheckBox checkBoxTransparent;
    private JTextField textFieldBracketNotationSecondTree;
    private JButton checkEqualsButton;
    private JPanel secondPanelPaintArea;

    private JMenuBar menuBarMain;
    private JPanel paintPanel = null;
    private JPanel secondPaintPanel = null;
    private final JFileChooser fileChooserSave;

    BinaryTreeInterface<Integer> tree = new BinaryTree<>();
    BinaryTreeInterface<Integer> secondTree = new BinaryTree<>();


    public TreeDemoFrame() {
        this.setTitle("Двоичные деревья");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        createMenu();

        splitPaneMain.setDividerLocation(0.5);
        splitPaneMain.setResizeWeight(1.0);
        splitPaneMain.setBorder(null);

        paintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };

        secondPaintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(secondTree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };

        JScrollPane firstPaintJScrollPane = new JScrollPane(paintPanel);
        JScrollPane secondPaintJScrollPane = new JScrollPane(secondPaintPanel);
        firstPanelPaintArea.add(firstPaintJScrollPane);
        secondPanelPaintArea.add(secondPaintJScrollPane);
        fileChooserSave = new JFileChooser();
        fileChooserSave.setCurrentDirectory(new File("./images"));
        FileFilter filter = new FileNameExtensionFilter("SVG images", "svg");
        fileChooserSave.addChoosableFileFilter(filter);
        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");


        buttonMakeTree.addActionListener(actionEvent -> {
            try {
                BinaryTree<Integer> fTree = new BinaryTree<>(Integer::parseInt);
                fTree.fromBracketNotation(textFieldBracketNotationFirstTree.getText());
                this.tree = fTree;
                repaintFirstTree();
                BinaryTree<Integer> sTree = new BinaryTree<>(Integer::parseInt);
                sTree.fromBracketNotation(textFieldBracketNotationSecondTree.getText());
                this.secondTree = sTree;
                repaintSecondTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonSaveImage.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            try {
                if (fileChooserSave.showSaveDialog(TreeDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooserSave.getSelectedFile().getPath();
                    if (!filename.toLowerCase().endsWith(".svg")) {
                        filename += ".svg";
                    }
                    BinaryTreePainter.saveIntoFile(tree, filename, checkBoxTransparent.isSelected());
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonPreOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.preOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
        buttonInOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.inOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.inOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
        buttonPostOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.postOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.postOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
        buttonByLevelTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.byLevelVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.byLevelValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
        checkEqualsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BinaryTree<Integer> firstTree = new BinaryTree<>(Integer::parseInt);
                BinaryTree<Integer> secondTree = new BinaryTree<>(Integer::parseInt);
                try {
                    firstTree.fromBracketNotation(textFieldBracketNotationFirstTree.getText());
                    secondTree.fromBracketNotation(textFieldBracketNotationSecondTree.getText());
                    boolean isEquals = firstTree.treeEquals(secondTree);
                    if (isEquals) {
                        SwingUtils.showInfoMessageBox("Данные деревья эквивалентны!", "Эквивалентность деревьев");
                    } else {
                        SwingUtils.showInfoMessageBox("Данные деревья не являются эквивалентными!", "Эквивалентность деревьев");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Создание меню
     */
    private void createMenu() {
        JMenu menuTesting = new JMenu("Тестирование");


        menuBarMain = new JMenuBar();
        menuBarMain.add(menuTesting);
        setJMenuBar(menuBarMain);
    }

    /**
     * Перерисовка дерева
     */
    public void repaintFirstTree() {
        firstPanelPaintArea.repaint();
    }

    public void repaintSecondTree() {
        secondPanelPaintArea.repaint();
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        splitPaneMain = new JSplitPane();
        panelMain.add(splitPaneMain, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setLeftComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(520, 124), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Деревья в скобочной нотации:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldBracketNotationFirstTree = new JTextField();
        textFieldBracketNotationFirstTree.setText("8 (6 (4 (5), 6), 5 (, 5 (2, 8)))");
        panel2.add(textFieldBracketNotationFirstTree, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        textFieldBracketNotationSecondTree = new JTextField();
        textFieldBracketNotationSecondTree.setText("8 (6 (4 (5), 6), 5 (, 5 (2, 8)))");
        panel2.add(textFieldBracketNotationSecondTree, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonMakeTree = new JButton();
        buttonMakeTree.setText("Построить деревья");
        panel2.add(buttonMakeTree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkEqualsButton = new JButton();
        checkEqualsButton.setText("Проверить эквивалентность");
        panel2.add(checkEqualsButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstPanelPaintArea = new JPanel();
        firstPanelPaintArea.setLayout(new BorderLayout(0, 0));
        panel1.add(firstPanelPaintArea, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(520, 0), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(520, 42), null, 0, false));
        buttonSaveImage = new JButton();
        buttonSaveImage.setText("Сохранить изображение в SVG");
        panel3.add(buttonSaveImage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxTransparent = new JCheckBox();
        checkBoxTransparent.setText("прозрачность");
        panel3.add(checkBoxTransparent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        secondPanelPaintArea = new JPanel();
        secondPanelPaintArea.setLayout(new BorderLayout(0, 0));
        panel1.add(secondPanelPaintArea, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(520, 0), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setRightComponent(panel4);
        buttonPreOrderTraverse = new JButton();
        buttonPreOrderTraverse.setText("Прямой обход");
        panel4.add(buttonPreOrderTraverse, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonInOrderTraverse = new JButton();
        buttonInOrderTraverse.setText("Симметричный обход");
        panel4.add(buttonInOrderTraverse, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPostOrderTraverse = new JButton();
        buttonPostOrderTraverse.setText("Обратный обход");
        panel4.add(buttonPostOrderTraverse, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonByLevelTraverse = new JButton();
        buttonByLevelTraverse.setText("Обход в ширину");
        panel4.add(buttonByLevelTraverse, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSystemOut = new JTextArea();
        scrollPane1.setViewportView(textAreaSystemOut);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
