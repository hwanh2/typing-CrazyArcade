package project;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

class ScorePanel extends JPanel{
    private int score = 0;
    private JLabel scoreLabel = new JLabel(Integer.toString(score));
    public ScorePanel() {
        setLayout(null);
        setBackground(new Color(30, 120, 215));

        JLabel scoreTextLabel = new JLabel("점수");
        scoreTextLabel.setForeground(new Color(255, 255, 130));
        scoreTextLabel.setBounds(30, 10, 200, 50);
        scoreTextLabel.setFont(new Font("", Font.BOLD, 25));

        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(25, 25, 170));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setBounds(120, 10, 100, 50);
        scoreLabel.setFont(new Font("", Font.BOLD, 25));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setVerticalAlignment(JLabel.CENTER);
        scoreLabel.setBorder(new LineBorder(new Color(255, 255, 130), 7, true)); // 색상, 두께, 둥근 모서리 설정

        add(scoreTextLabel);
        add(scoreLabel);
    }
    public void addScore() {
        score+=10;
        scoreLabel.setText(Integer.toString(score));
    }
    public void clearBoss() {
        score+=100;
    }
    public int getScore() {
        return score;
    }

}