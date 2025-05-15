package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

// 틀을 짜주는 클래스
public class Round1Panel extends JPanel {
    private TypingGameFrame frame = null;
    private ScorePanel scorePanel = null;
    private GamePanel gamePanel = null;
    private UserPanel userPanel = null;
    private ItemPanel itemPanel = null;

    private SoundPlayer soundPlayer = null;

    private String id = null;

    private boolean bubbleFlag = false;

    private boolean sound = true;

    private boolean backSoundFlag = true;

    public Round1Panel(TypingGameFrame frame,ScorePanel scorePanel,GamePanel gamePanel,SoundPlayer soundPlayer) {
        this.frame = frame;
        this.scorePanel = scorePanel;
        this.userPanel = new UserPanel(scorePanel);
        this.itemPanel = new ItemPanel();
        this.gamePanel = gamePanel;
        this.soundPlayer = soundPlayer;
        setLayout(new BorderLayout());
        makeSplit(); // 패널의 구역을 나눈다.
    }

    public JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar(); // 메뉴바 생성
        mb.setBackground(new Color(0, 120, 215));
        mb.setBorder(BorderFactory.createLineBorder(new Color(25, 30, 112),3));

        ImageIcon menuIcon = new ImageIcon("images/menu.jpg");
        ImageIcon backSoundIcon = new ImageIcon("images/backSound.jpg");
        ImageIcon forSoundIcon = new ImageIcon("images/forSound.jpg");
        ImageIcon backIcon = new ImageIcon("images/back.jpg");
        ImageIcon exitIcon = new ImageIcon("images/exit.jpg");

        JMenu menu = new JMenu(); //메뉴 생성
        menu.setIcon(menuIcon);

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(bubbleFlag==false) {
                    gamePanel.stopBubble();
                    gamePanel.stopItem();
                    timerStop();
                    bubbleFlag = true;
                }
                else {
                    gamePanel.startBubble();
                    gamePanel.startItem();
                    timerStart();
                    bubbleFlag = false;
                }

            }
        });

        JMenuItem backSound = new JMenuItem(backSoundIcon);
        backSound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(backSoundFlag) {
                    soundPlayer.stopSound();
                    backSoundFlag = false;
                }
                else {
                    soundPlayer.loadSound("audio/monsterBackSound.wav");
                    soundPlayer.loopSound();
                    soundPlayer.playSound();
                    backSoundFlag = true;
                }
                bubbleFlag = false;
                gamePanel.startBubble();
                timerStart();

            }
        });


        JMenuItem forSound = new JMenuItem(forSoundIcon);

        JMenuItem back = new JMenuItem(backIcon);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.startBubble();
                timerStart();
            }
        });

        JMenuItem exit = new JMenuItem(exitIcon);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundPlayer.stopSound();
                StartPanel sp = new StartPanel(frame);

                boolean sound = false;
                LodingPanel lp = new LodingPanel(frame, sp, soundPlayer,sound);

                frame.switchToPanel(lp);

            }
        });

        backSound.setBackground(new Color(0, 120, 215));
        forSound.setBackground(new Color(0, 120, 215));
        back.setBackground(new Color(0, 120, 215));
        exit.setBackground(new Color(0, 120, 215));

        menu.add(backSound);
        menu.add(forSound);
        menu.add(back);
        menu.add(exit);

        mb.add(menu);

        ImageIcon buttonIcon = new ImageIcon("images/exitButton.jpg");
        JButton imageButton = new JButton(buttonIcon);
        imageButton.setBorderPainted(false);
        imageButton.setContentAreaFilled(false);
        imageButton.setFocusPainted(false);

        mb.add(Box.createHorizontalGlue());
        mb.add(imageButton);

        return mb;
    }

    // 다른 패널에서 사용하기 위해 설정
    public void timerStart() {
        gamePanel.timerStart();

    }

    public void timerStop() {
        gamePanel.timerStop();

    }
    // 구역을 나눈다.
    private void makeSplit() {
        JSplitPane hPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        hPane.setDividerLocation(900);
        hPane.setDividerSize(0);
        hPane.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112), 10)); // 진한 남색 테두리

        JSplitPane vPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        vPane.setDividerLocation(380);
        vPane.setDividerSize(0);
        vPane.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 230), 10)); // 진한 남색 테두리

        hPane.setLeftComponent(gamePanel); // 왼쪽에는 게임패널을 붙인다.
        hPane.setRightComponent(vPane);

        vPane.setTopComponent(userPanel); // 오른쪽 위에는 유저패널을 붙인다.
        vPane.setBottomComponent(itemPanel); // 오른쪽 아래에는 아이템패널을 붙인다.

        add(hPane, BorderLayout.CENTER); // hPane을 패널에 붙인다.
    }
    public void setId(String id) {
        itemPanel.setId(id);
        gamePanel.setId(id);
    }
    public void setCharacter(String character) {
        gamePanel.setCharacter(character);
        itemPanel.setCharacter(character);
    }
}


