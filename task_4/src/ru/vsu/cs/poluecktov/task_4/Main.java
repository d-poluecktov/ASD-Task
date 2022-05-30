package ru.vsu.cs.poluecktov.task_4;

import util.SwingUtils;

import javax.swing.*;
import java.util.*;

public class Main {

    public static void winMain() throws Exception {
        SwingUtils.setLookAndFeelByName("Windows");
        Locale.setDefault(Locale.ROOT);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtils.setDefaultFont("Microsoft Sans Serif", 18);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMain().setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        winMain();
    }
}
