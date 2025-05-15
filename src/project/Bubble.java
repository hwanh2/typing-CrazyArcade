package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Bubble {
    private JLabel bubble;
    private static final String Path = "images/bubble";
    private int posX;
    private int posY = 10;
    private FallingThread fallingThread;
    private GamePanel gamePanel = null;
    private int level;

    private ImageIcon motionIcon = new ImageIcon("images/motion.jpg");
    private Image motionImg = motionIcon.getImage();
    private Image scaledImg = motionImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);

    public Bubble(String text,GamePanel gamePanel,int level) {
        this.gamePanel = gamePanel;
        this.level = level;
        initBubble(text);
        this.fallingThread = new FallingThread(this,level);
        this.fallingThread.start();
    }

    public JLabel getBubble() {
        return bubble;
    }

    public void down() {
        bubble.setBounds(posX, posY++, 100, 50);
        if(posY == 500) {
            removeBubble();
            gamePanel.death();
        }
    }

    public String getBubbleWord() {
        return bubble.getText();
    }

    public void removeBubble() {
        bubble.setVisible(false);
        gamePanel.removeBubble(this);
        stopFalling();
    }

    private void initBubble(String text) { // 물풍선 객체를 생성한다.
        int i = (int)(Math.random() * 7) + 1;
        ImageIcon bubbleIcon = new ImageIcon(Path + i + ".jpg");
        Image bubbleImg = bubbleIcon.getImage();
        Image scaledImg = bubbleImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        bubbleIcon = new ImageIcon(scaledImg);

        bubble = new JLabel(bubbleIcon);
        bubble.setText(text);
        bubble.setHorizontalTextPosition(JLabel.CENTER);
        bubble.setVerticalTextPosition(JLabel.CENTER);
        bubble.setForeground(Color.WHITE);
        bubble.setFont(new Font("SansSerif", Font.BOLD, 24));
        bubble.setPreferredSize(new Dimension(50, 50));

        posX = (int)(Math.random() * 650);
        if(posX>=100 && posX<=580) {
            posY+=50;
        }
        else {
            posY = 10;
            bubble.setBounds(posX, posY, 100, 50);
        }
    }

    public void showMotion() { // 물풍선이 터지는 모션 함수
        stopFalling();
        bubble.setBounds(posX,posY,230,230);
        bubble.setIcon(motionIcon);
        bubble.setText("");
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBubble();
            }
        }).start();
    }

    public void startFalling() {
        if (fallingThread != null) {
            fallingThread.startBubble();

        }
    }
    public void stopFalling() {
        if (fallingThread != null) {
            fallingThread.stopBubble();

        }
    }
}

class FallingThread extends Thread {
    private Bubble bubble;
    private int sleepTime;
    private boolean bubbleFlag = true;
    private int level;

    public FallingThread(Bubble bubble,int level) {
        this.bubble = bubble;
        this.level = level;
        this.sleepTime = (int) (Math.random() * level) + 10;
    }

    @Override
    public void run() {
        while (true) {
            if(bubbleFlag==true) {
                bubble.down();
            }
            try {
                sleep(sleepTime); // 처음에 결정된 랜덤한 시간동안 물풍선이 떨어짐
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void startBubble() {
        bubbleFlag = true;
    }

    public void stopBubble() {
        bubbleFlag = false;
    }
}
