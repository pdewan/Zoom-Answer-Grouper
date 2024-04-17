package com.Chat_Cluster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PromptChoiceView {
    public static void promptChoice(QuestionType type) {
        // Create a modal dialog. We pass null for the owner frame, making it a top-level dialog.
        final JDialog dialog = new JDialog((Frame) null, "Button Example", true);
        
        // Specify FlowLayout for the layout manager.
        dialog.setLayout(new FlowLayout());
        
        // Give the dialog an initial size.
        dialog.setSize(300, 100);
        
        // Create two buttons.
        JButton button1 = new JButton("Factual");
        JButton button2 = new JButton("Conceptual");
        
        // Add action listener for Button 1
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type.setType("Factual");
                dialog.dispose();
            }
        });
        
        // Add action listener for Button 2
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type.setType("Conceptual");
                dialog.dispose();
            }
        });
        
        // Add the buttons to the dialog's content pane.
        dialog.add(button1);
        dialog.add(button2);
        
        // Display the dialog.
        dialog.setVisible(true);
    }
}