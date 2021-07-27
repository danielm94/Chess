package chessgui.gui;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {
    Component component;

    public BoardFrame() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Chess");
        this.setResizable(false);
        component = new BoardComponent();
        this.add(component, BorderLayout.CENTER);

        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }
}
