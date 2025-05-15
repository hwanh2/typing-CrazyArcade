package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class UserPanel extends JPanel {
    private ScorePanel scorePanel = null;
    private ImagePanel imagePanel = new ImagePanel();

    public UserPanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
        setLayout(new BorderLayout());
        makeSplit();
    }

    public void makeSplit() {
        JSplitPane vPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        vPane.setDividerLocation(300);
        vPane.setDividerSize(0);

        vPane.setTopComponent(imagePanel);
        vPane.setBottomComponent(scorePanel);

        add(vPane, BorderLayout.CENTER);
    }


}

class ImagePanel extends JPanel{
    public ImagePanel() {
        setBackground(Color.BLUE);

    }
}
