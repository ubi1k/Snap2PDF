
package com.snap2pdf;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        System.out.println("Snap2PDF is starting...");

        SwingUtilities.invokeLater(() -> {
            ConverterGUI gui = new ConverterGUI();
            gui.createAndShowGUI();
        });

    }
}
