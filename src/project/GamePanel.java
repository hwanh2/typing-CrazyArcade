package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

class GamePanel extends JPanel {
    private ArrayList<Bubble> bubbleList = new ArrayList<Bubble>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ScorePanel scorePanel = null;
    private Words words = null;
    private MonsterPanel monsterPanel = new MonsterPanel();
    private BossPanel bossPanel = new BossPanel();
    private InputPanel inputPanel = new InputPanel();
    private WestPanel westPanel = new WestPanel();
    private EastPanel eastPanel = new EastPanel();
    private TypingGameFrame frame = null;
    private StartPanel startPanel = null;

    private ImageIcon icon = new ImageIcon("images/round.JPG");
    private Image img = icon.getImage();

    private ImageIcon monsterIcon = new ImageIcon("images/monster.JPG");
    private Image monsterImg = monsterIcon.getImage();

    private ImageIcon bossIcon = new ImageIcon("images/boss.JPG");
    private Image bossImg = bossIcon.getImage();

    private ImageIcon bossDeathIcon = new ImageIcon("images/bossDeath.JPG");
    private Image bossDeathImg = bossDeathIcon.getImage();

    private Timer timer;
    private Timer itemTimer;
    private int round = 1;

    private ImageIcon readyIcon = new ImageIcon("images/ready.JPG");
    private Image readyImg = readyIcon.getImage();

    private ImageIcon winIcon = new ImageIcon("images/win.JPG");
    private Image winImg = winIcon.getImage();

    private ImageIcon loseIcon = new ImageIcon("images/lose.JPG");
    private Image loseImg = loseIcon.getImage();

    private boolean showReady = false;
    private boolean showWin = false;
    private boolean showLose = false;

    private ImageIcon liveBazziIcon = new ImageIcon("images/liveBazzi.JPG");
    private Image liveBazziImg = liveBazziIcon.getImage();
    private ImageIcon deathBazziIcon = new ImageIcon("images/deathBazzi.JPG");
    private Image deathBazziImg = deathBazziIcon.getImage();

    private ImageIcon liveUniIcon = new ImageIcon("images/liveUni.JPG");
    private Image liveUniImg = liveUniIcon.getImage();
    private ImageIcon deathUniIcon = new ImageIcon("images/deathUni.JPG");
    private Image deathUniImg = deathUniIcon.getImage();

    private int live = 3;
    private int death = 0;
    private String id = null;
    private String character = null;

    private ImageIcon checkIcon = new ImageIcon("images/checkButton.JPG");

    private int level = 25;

    private int timeInterval = 1300;

    private SoundPlayer soundPlayer = null;

    private SoundPlayer soundEffect = new SoundPlayer();

    public void setId(String id) {
        this.id = id;
    }
    public void setCharacter(String character) {
        this.character = character;
    }

    public void death() { // 플레이어의 목숨 감소
        live--;
        death++;
        repaint();
        if(death==3) { // 3이 되면 게임 lose 표시와 함께 점수가 저장되고 확인창이 뜬다
            endGame("lose");
            saveLank();
            showScore();
            soundPlayer.stopSound();
            soundPlayer.loadSound("audio/loseSound.wav");
            soundPlayer.playSound();
        }
    }

    public void timerStart() { // 다른 패널에서 사용하기 위한 함수
        timer.start();
        itemTimer.start();
    }

    public void timerStop() { // 다른 패널에서 사용하기 위한 함수
        timer.stop();
        itemTimer.stop();
    }
    // 게임 패널 생성자
    public GamePanel(TypingGameFrame frame,ScorePanel scorePanel,SoundPlayer soundPlayer) {
        this.frame = frame;
        this.scorePanel = scorePanel;
        this.soundPlayer = soundPlayer;
        this.startPanel = startPanel;

        words = new Words("Words.txt");
        setLayout(new BorderLayout());
        //패널에 여러 패널들을 붙인다.
        add(monsterPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);

        // 지정한 시간동안 물풍선을 계속 생산
        timer = new Timer(timeInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndStartBubble(); // 물풍선을 만든다.
            }
        });

        // 지정한 시간동안 아이템을 계속 생산
        itemTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndStartItem(); // 아이템을 만든다.
            }
        });

    }

    //물풍선을 만드는 함수
    private void createAndStartBubble() {
        String word = words.getRandomWord();
        if(word.length()>=1) {
            Bubble bubble = new Bubble(word,this,level);
            bubbleList.add(bubble); // 리스트에 물풍선 객체를 추가한다.
            if(round == 1) {
                monsterPanel.addBubble(bubble); // 패널에 버블 객체를 붙인다.
            }
            else if(round==2) {
                bossPanel.addBubble(bubble); // 패널에 버블 객체를 붙인다.
            }

        }
    }
    //아이템을 만드는 함수
    private void createAndStartItem() {
        String word = words.getRandomWord();
        if(word.length()>=1) {
            Item item = new Item(word,this,level);
            itemList.add(item); // 리스트에 아이템 객체를 추가한다.
            if(round == 1) {
                monsterPanel.addItem(item); // 패널에 버블 객체를 붙인다.
            }
            else if(round==2) {
                bossPanel.addItem(item); // 패널에 버블 객체를 붙인다.
            }

        }
    }

    // 다른 패널에서 사용할 물풍선을 움직이고 멈추게 하는 함수
    public void startBubble() {
        for(int i=0; i<bubbleList.size(); i++) {
            bubbleList.get(i).startFalling();
        }
    }
    public void stopBubble() {
        for(int i=0; i<bubbleList.size(); i++) {
            bubbleList.get(i).stopFalling();
        }
    }

    // 다른 패널에서 사용할 아이템을 움직이고 멈추게 하는 함수
    public void startItem() {
        for(int i=0; i<itemList.size(); i++) {
            itemList.get(i).startFalling();
        }
    }

    public void stopItem() {
        for(int i=0; i<itemList.size(); i++) {
            itemList.get(i).stopFalling();
        }
    }

    public void showWinImage() {
        showWin = true;
        repaint();
    }
    public void hideWinImage(){
        showWin = false;
        repaint();
    }
    public void showReadyImage() {
        showReady = true;
        repaint();
    }
    public void hideReadyImage() {
        showReady = false;
        repaint();
    }
    public void showLoseImage() {
        showLose = true;
        repaint();
    }
    public void hideLoseImage() {
        showLose = false;
        repaint();
    }

    // 게임패널 센터에 붙을 몬스터 클래스로 메인 게임 화면이다.
    class MonsterPanel extends JPanel {
        private int barSize = 8; // 몬스터의 체력

        public MonsterPanel() {
            setLayout(null);

        }

        public void addBubble(Bubble bubble) {
            add(bubble.getBubble()); // 패널에 버블 객체를 붙인다.
        }

        public void addItem(Item item) {
            add(item.getItem()); // 패널에 버블 객체를 붙인다.
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
            g.drawImage(monsterImg, 200, 0, 70, 70, null);
            g.setColor(Color.RED);
            g.fillRect(280, 20, barSize * 30, 25);
            g.setColor(Color.BLACK);
            g.fillRect(280 + barSize * 30, 20, (8 - barSize) * 30, 25);

            int x = 0;
            if(character.equals("bazzi")) {
                for(int i=0; i<live; i++) {
                    g.drawImage(liveBazziImg, 20+x, 590, 45, 45, null);
                    x+=60;
                }

                for(int i=0; i<death; i++) {
                    g.drawImage(deathBazziImg, 20+x, 585, 55, 55, null);
                    x+=60;
                }

            }
            else if(character.equals("uni")) {
                for(int i=0; i<live; i++) {
                    g.drawImage(liveUniImg, 20+x, 590, 45, 45, null);
                    x+=60;
                }

                for(int i=0; i<death; i++) {
                    g.drawImage(deathUniImg, 20+x, 585, 55, 55, null);
                    x+=60;
                }
            }
            if(showLose) {
                g.drawImage(loseImg, getWidth()/2 - loseImg.getWidth(this)/2, getHeight()/2 - loseImg.getHeight(this)/2-100, this);
            }
        }

        public void reduceBarSize() { // 타이핑을 하면 체력을 깎는다.
            barSize-=1;
            if(barSize<=0) { // 체력이 0이 되면 인터벌을 줄이고 보스패널로 바꾸고 사운드를 설정한다.
                level = 15;
                timeInterval = 900;
                timer.setDelay(timeInterval);
                switchBossPanel();
                soundPlayer.stopSound();
                soundPlayer.loadSound("audio/bossBackSound.wav");
                soundPlayer.loopSound();
                soundPlayer.playSound();
            }

            repaint();
        }
    }

    // 왼쪽 박스 패널
    class WestPanel extends JPanel {
        private JLabel[] boxLabel;

        public WestPanel() {
            setLayout(new GridLayout(10, 1));
            setBackground(new Color(101, 67, 33));
            ImageIcon img = new ImageIcon("images/box.jpg");
            boxLabel = new JLabel[10];
            for (int i = 0; i < boxLabel.length; i++) {
                boxLabel[i] = new JLabel(img);
                add(boxLabel[i]);
            }
        }
    }
    // 오른쪽 박스 패널
    class EastPanel extends JPanel {
        private JLabel[] boxLabel;

        public EastPanel() {
            setLayout(new GridLayout(10, 1));
            setBackground(new Color(101, 67, 33));
            ImageIcon img = new ImageIcon("images/box.jpg");
            boxLabel = new JLabel[10];
            for (int i = 0; i < boxLabel.length; i++) {
                boxLabel[i] = new JLabel(img);
                add(boxLabel[i]);
            }
        }
    }

    // 타이핑 하는 패널
    class InputPanel extends JPanel {
        private JTextField textField = new JTextField(15);

        public InputPanel() {

            setBackground(new Color(25, 25, 112));
            textField.setFont(new Font("SansSerif", Font.BOLD, 30));
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            add(textField);

            textField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField tf = (JTextField) e.getSource();
                    String text = tf.getText(); // 텍스트필드의 값을 가져온다.

                    synchronized (bubbleList) { // 하나의 스레드만 버블리스트에 접근할 수 있다.
                        for (int i = 0; i < bubbleList.size(); i++) {
                            if (text.equals(bubbleList.get(i).getBubbleWord())) {
                                bubbleList.get(i).showMotion();

                                soundEffect.loadSound("audio/waterSound.wav");
                                soundEffect.playSound();

                                bubbleList.remove(i);
                                if(round==1) { // 몬스터일 경우
                                    monsterPanel.reduceBarSize(); // 바 사이즈 감소
                                }
                                else if(round==2) { // 보스일 경우
                                    bossPanel.reduceBarSize();
                                }
                                tf.setText("");
                                break;
                            } else {
                                tf.setText("");
                            }
                        }
                    }

                    synchronized (itemList) { // 하나의 스레드만 버블리스트에 접근할 수 있다.
                        for (int i = 0; i < itemList.size(); i++) {
                            if (text.equals(itemList.get(i).getItemWord())) {


                                soundEffect.loadSound("audio/itemEat.wav");
                                soundEffect.playSound();
                                itemList.get(i).removeItem();
                                itemList.remove(i);
                                scorePanel.addScore();

                                tf.setText("");
                                break;
                            } else {
                                tf.setText("");
                            }
                        }
                    }
                }
            });
        }
    }

    // 여러 결과로 게임이 끝나면 실행되는 함수
    public void endGame(String result) {
        for (int i = bubbleList.size() - 1; i >= 0; i--) {
            bubbleList.get(i).removeBubble();
            bubbleList.remove(i);
        }
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).removeItem();
            itemList.remove(i);
        }
        timer.stop();
        itemTimer.stop();
        if(result.equals("win")) {
            new ResultThread(result).start();
        }
        else if(result.equals("ready")) {
            new ResultThread(result).start();
        }
        else if(result.equals("lose")) {
            new ResultThread(result).start();
        }
    }

    // 라운드 2 보스패널
    class BossPanel extends JPanel {
        private int barSize = 16;
        private boolean bossDeath = false;

        public BossPanel() {
            setLayout(null);

        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

            if (bossDeath) {
                g.drawImage(bossDeathImg, 115, 0, 120, 120, null);
            } else {
                g.drawImage(bossImg, 115, 0, 120, 120, null);
            }

            g.setColor(Color.RED);
            g.fillRect(240, 40, barSize * 30, 25);
            g.setColor(Color.BLACK);
            g.fillRect(240 + barSize * 30, 40, (15 - barSize) * 30, 25);

            int x = 0;

            if(character.equals("bazzi")) {
                for(int i=0; i<live; i++) {
                    g.drawImage(liveBazziImg, 20+x, 590, 45, 45, null);
                    x+=60;
                }

                for(int i=0; i<death; i++) {
                    g.drawImage(deathBazziImg, 20+x, 585, 55, 55, null);
                    x+=60;
                }

            }
            else if(character.equals("uni")) {
                for(int i=0; i<live; i++) {
                    g.drawImage(liveUniImg, 20+x, 590, 45, 45, null);
                    x+=60;
                }

                for(int i=0; i<death; i++) {
                    g.drawImage(deathUniImg, 20+x, 585, 55, 55, null);
                    x+=60;
                }
            }
            if (showReady) {
                g.drawImage(readyImg, getWidth()/2 - readyImg.getWidth(this)/2, getHeight()/2 - readyImg.getHeight(this)/2, this);
            }
            if(showWin) {
                g.drawImage(winImg, getWidth()/2 - winImg.getWidth(this)/2, getHeight()/2 - winImg.getHeight(this)/2-100, this);
            }
            if(showLose) {
                g.drawImage(loseImg, getWidth()/2 - loseImg.getWidth(this)/2, getHeight()/2 - loseImg.getHeight(this)/2-100, this);
            }
        }
        public void addBubble(Bubble bubble) {
            add(bubble.getBubble()); // 패널에 버블 객체를 붙인다.
        }
        public void addItem(Item item) {
            add(item.getItem()); // 패널에 버블 객체를 붙인다.
        }

        public void reduceBarSize() {
            barSize-=2;
            if(barSize<=0) {
                soundPlayer.stopSound();
                soundPlayer.loadSound("audio/winSound.wav");
                soundPlayer.playSound();
                scorePanel.clearBoss();
                bossDeath = true;
                endGame("win");
                saveLank();
                showScore();
            }
            repaint();

        }
    }

    public void removeBubble(Bubble bubble) {
        this.remove(bubble.getBubble());
        this.revalidate();
        this.repaint();
    }

    public void removeItem(Item item) {
        this.remove(item.getItem());
        this.revalidate();
        this.repaint();
    }

    public void showScore() {
        ShowScorePanel showScorePanel = new ShowScorePanel();
        showScorePanel.setBounds(205, 280, 350, 220);

        if (round == 1) {
            monsterPanel.add(showScorePanel);
            monsterPanel.revalidate(); // 레이아웃을 다시 계산
            monsterPanel.repaint(); // 컴포넌트를 다시 그리기
        } else if (round == 2) {
            bossPanel.add(showScorePanel);
            bossPanel.revalidate(); // 레이아웃을 다시 계산
            bossPanel.repaint(); // 컴포넌트를 다시 그리기
        }
    }

    // 게임이 끝나면 나오는 입력창 클래스
    class ShowScorePanel extends JPanel {
        public ShowScorePanel() {
            setLayout(null);
            setBackground(new Color(30, 120, 215));
            JLabel idLabel = new JLabel("ID: " + id);
            idLabel.setFont(new Font("", Font.BOLD, 30));
            idLabel.setForeground(new Color(135, 206, 250));
            idLabel.setSize(250,50);
            idLabel.setLocation(60,35);
            add(idLabel);

            JLabel scoreLabel = new JLabel("SCORE: "+scorePanel.getScore());
            scoreLabel.setFont(new Font("", Font.BOLD, 30));
            scoreLabel.setForeground(new Color(135, 206, 250));
            scoreLabel.setSize(250,50);
            scoreLabel.setLocation(60,90);
            add(scoreLabel);

            JButton checkButton = new JButton(checkIcon);
            int ButtonX = 100;
            int ButtonY = 150;
            int buttonWidth = 165;
            int buttonHeight = 50;

            Image scaledButtonImage = checkIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            checkButton.setBounds(ButtonX, ButtonY, buttonWidth, buttonHeight);

            checkButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    checkButton.setBounds(ButtonX - 8, ButtonY - 4, buttonWidth + 16, buttonHeight + 8);
                    Image img = ((ImageIcon) checkButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth + 20, buttonHeight + 10, Image.SCALE_SMOOTH);
                    checkButton.setIcon(new ImageIcon(scaledImg));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    checkButton.setBounds(ButtonX, ButtonY, buttonWidth, buttonHeight);
                    Image img = ((ImageIcon) checkButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                    checkButton.setIcon(new ImageIcon(scaledImg));

                }
                @Override
                public void mousePressed(MouseEvent e) { // 확인 버튼을 누르면 스타트패널로 돌아간다.
                    StartPanel sp = new StartPanel(frame);

                    boolean sound = false;
                    LodingPanel lp = new LodingPanel(frame, sp, soundPlayer,sound);

                    frame.switchToPanel(lp);


                }

            });

            add(checkButton);

        }
    }


    public void saveLank() {
        String text = id + " " + scorePanel.getScore() + "\n";
        try (FileWriter writer = new FileWriter("lank.txt", true)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 게임이 끝나면 결과를 1초간 보여주는 스레드
    class ResultThread extends Thread {
        private String result;
        public ResultThread(String result) {
            this.result = result;
        }
        @Override
        public void run() {
            if(result.equals("win")) {
                showWinImage();
                level = 10;
                timeInterval = 500;
                timer.setDelay(timeInterval);
            }
            else if(result.equals("ready")) {
                showReadyImage();
            }
            else if(result.equals("lose")) {
                showLoseImage();
                level = 10;
                timeInterval = 500;
                timer.setDelay(timeInterval);
            }
            try {
                Thread.sleep(2000); // 2초 동안 ready 이미지를 보여줍니다.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hideReadyImage();
            hideWinImage();
            hideLoseImage();
            if(result.equals("ready")) {
                timer.start();
                itemTimer.start();
            }

        }

    }

    // 게임패널의 몬스터 패널에서 보스패널로 바꿔주는 함수
    public void switchBossPanel() {
        timer.stop();
        itemTimer.stop();
        remove(monsterPanel);
        add(bossPanel,BorderLayout.CENTER);
        round++;
        revalidate();
        repaint();

        endGame("ready");


    }

}
