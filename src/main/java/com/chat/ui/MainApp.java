package com.chat.ui;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Optional: Set native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch WelcomeScreen and on success launch the next screen
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(() -> {
                // This is where you'd launch the next UI (e.g., LectureListView)
                // For now, we just show a placeholder dialog
                JOptionPane.showMessageDialog(null, "Welcome! Proceeding to main application...");
                new  LectureListView().setVisible(true);
                // Example: new LectureListView().setVisible(true);
            });
//            welcomeScreen.setVisible(true);
        });
    }
}
