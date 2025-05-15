package project;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ItemPanel extends JPanel {
    private ImageIcon icon = new ImageIcon("images/itemPanel.JPG");
    private Image img = icon.getImage();
    private ImageIcon bazziIcon = new ImageIcon("images/bazzi.JPG");
    private Image bazziImage = bazziIcon.getImage();
    private ImageIcon uniIcon = new ImageIcon("images/uni.JPG");
    private Image uniImage = uniIcon.getImage();
    private JLabel idLabel;
    private String character = null;

    public ItemPanel() {
        setLayout(null);

        idLabel = new JLabel();
        idLabel.setFont(new Font("", Font.BOLD, 20));
        idLabel.setBackground(new Color(25, 25, 112));
        idLabel.setOpaque(true);
        idLabel.setForeground(Color.WHITE);  // 글씨 색을 흰색으로 설정
        idLabel.setBounds(110, 22, 110, 45);  // 점수판의 위치와 크기 설정
        idLabel.setBorder(new LineBorder(new Color(0, 191, 255), 4, true)); // 색상, 두께, 둥근 모서리 설정

        // 패널에 점수판 레이블 추가
        add(idLabel);

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        if(character.equals("bazzi")) {
            g.drawImage(bazziImage, 47, 22, 57, 43, null);

        }
        else if(character.equals("uni")) {
            g.drawImage(uniImage, 47, 22, 57, 43, null);
        }

    }

    public void setId(String id) {
        idLabel.setText(id);
        repaint();
    }

    public void setCharacter(String character) {
        this.character = character;
        repaint();
    }
}