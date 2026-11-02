
package com.snap2pdf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConverterGUI {

    private final DefaultListModel<String> listModel =
            new DefaultListModel<>();

    private final List<File> selectedImages =
            new ArrayList<>();

    private final ImageToPdfConverter converter =
            new ImageToPdfConverter();

    private JLabel statusLabel;

    public void createAndShowGUI() {

        JFrame frame = new JFrame("Snap2PDF - Image to PDF Converter");

        frame.setSize(650, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));

        // Header
        JLabel title = new JLabel(
                "Snap2PDF - JPG to PDF Converter",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));

        frame.add(title, BorderLayout.NORTH);

        // Image list
        JList<String> imageList = new JList<>(listModel);

        JScrollPane scrollPane = new JScrollPane(imageList);

        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton addButton = new JButton("Add Images");
        JButton removeButton = new JButton("Remove Selected");
        JButton clearButton = new JButton("Clear All");
        JButton convertButton = new JButton("Convert to PDF");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(convertButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Status
        statusLabel = new JLabel(
                "Select JPG images to get started.",
                SwingConstants.CENTER
        );

        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 10, 5)
        );

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.remove(buttonPanel);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add images
        addButton.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            chooser.setMultiSelectionEnabled(true);

            chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                    "JPG Images", "jpg", "jpeg"
                )
            );

            int result = chooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {

                for (File file : chooser.getSelectedFiles()) {

                    String name = file.getName().toLowerCase();

                    if ((name.endsWith(".jpg") ||
                         name.endsWith(".jpeg"))
                            && !selectedImages.contains(file)) {

                        selectedImages.add(file);
                        listModel.addElement(file.getName());
                    }
                }

                statusLabel.setText(
                    selectedImages.size() + " image(s) selected."
                );
            }
        });

        // Remove selected image
        removeButton.addActionListener(e -> {

            int index = imageList.getSelectedIndex();

            if (index != -1) {

                selectedImages.remove(index);
                listModel.remove(index);

                statusLabel.setText(
                    selectedImages.size() + " image(s) remaining."
                );
            }
        });

        // Clear all
        clearButton.addActionListener(e -> {

            selectedImages.clear();
            listModel.clear();

            statusLabel.setText("All images cleared.");
        });

        // Convert images to PDF
        convertButton.addActionListener(e -> {

            if (selectedImages.isEmpty()) {

                JOptionPane.showMessageDialog(
                    frame,
                    "Please select images first."
                );

                return;
            }

            JFileChooser chooser = new JFileChooser();

            chooser.setSelectedFile(
                new File("Snap2PDF-Output.pdf")
            );

            int result = chooser.showSaveDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {

                File output = chooser.getSelectedFile();

                if (!output.getName().toLowerCase().endsWith(".pdf")) {
                    output = new File(output.getAbsolutePath() + ".pdf");
                }

                try {

                    converter.convert(selectedImages, output);

                    statusLabel.setText(
                        "PDF successfully created!"
                    );

                    JOptionPane.showMessageDialog(
                        frame,
                        "PDF created successfully!\n"
                            + output.getAbsolutePath()
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "Conversion failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        frame.setVisible(true);
    }
}
