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

public class Item {
    private JLabel item;
    private static final String Path = "images/item";
    private int posX;
    private int posY = 10;
    private FallingItemThread fallingItemThread;
    private GamePanel gamePanel = null;
    private int level;


    public Item(String text,GamePanel gamePanel,int level) {
        this.gamePanel = gamePanel;
        this.level = level;
        initItem(text);
        this.fallingItemThread = new FallingItemThread(this,level);
        this.fallingItemThread.start();
    }

    public JLabel getItem() {
        return item;
    }

    public void down() {
        item.setBounds(posX, posY++, 100, 50);
        if(posY == 500) {
            removeItem();
        }
    }

    public String getItemWord() {
        return item.getText();
    }

    public void removeItem() {
        item.setVisible(false);
        gamePanel.removeItem(this);
        stopFalling();
    }

    private void initItem(String text) {
        int i = (int)(Math.random() * 6) + 1;
        ImageIcon itemIcon = new ImageIcon("images/item.jpg");
        Image itemImg = itemIcon.getImage();
        Image scaledImg = itemImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        itemIcon = new ImageIcon(scaledImg);

        item = new JLabel(itemIcon);
        item.setText(text);
        item.setHorizontalTextPosition(JLabel.CENTER);
        item.setVerticalTextPosition(JLabel.CENTER);
        item.setForeground(Color.WHITE);
        item.setFont(new Font("SansSerif", Font.BOLD, 24));
        item.setPreferredSize(new Dimension(50, 50));

        posX = (int)(Math.random() * 650);
        if(posX>=100 && posX<=580) {
            posY+=50;
        }
        else {
            posY = 10;
            item.setBounds(posX, posY, 100, 50);
        }
    }

    public void startFalling() {
        if (fallingItemThread != null) {
            fallingItemThread.startItem();

        }
    }
    public void stopFalling() {
        if (fallingItemThread != null) {
            fallingItemThread.stopItem();

        }
    }
}

class FallingItemThread extends Thread {
    private Item item;
    private int sleepTime;
    private boolean itemFlag = true;
    private int level;

    public FallingItemThread(Item item,int level) {
        this.item = item;
        this.level = level;
        this.sleepTime = (int) (Math.random() * level) + 5;
    }

    @Override
    public void run() {
        while (true) {
            if(itemFlag==true) {
                item.down();
            }
            try {
                sleep(sleepTime); // 처음에 결정된 랜덤한 시간동안 물풍선이 떨어짐
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void startItem() {
        itemFlag = true;
    }

    public void stopItem() {
        itemFlag = false;
    }
}
