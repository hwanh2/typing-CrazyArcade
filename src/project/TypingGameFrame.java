package project;

import javax.sound.sampled.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

// 메인 클래스
public class TypingGameFrame extends JFrame {

    private StartPanel sp = null;
    private Clip clip; // 오디오 클립 객체

    public TypingGameFrame() {
        setTitle("TypingGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        sp = new StartPanel(this); // StartPanel을 생성한다.
        setContentPane(sp); // 처음에 StartPanel로 컨텐트팬을 지정한다.

        setResizable(false);

        setVisible(true);


    }


    public StartPanel getStartPanel() { // 다른 패널에서 현재 스타트패널을 받기 위한 함수
        return sp;
    }

    // 패널 전환 함수
    public void switchToPanel(JPanel panel) {
        setContentPane(panel); // 컨텐트팬에 새로운 패널을 설정한다.

        // 패널에 따라 메뉴바 설정
        if (panel instanceof Round1Panel) {
            setJMenuBar(((Round1Panel) panel).createMenuBar()); // Round1Panel에서는 menuBar를 지정한다.
        } else {
            setJMenuBar(null); // 다른 패널(로딩패널 등)은 메뉴바 제거한다.
        }

        revalidate(); // 컨테이너에 부착된 컴포넌트의 재배치 지시
        repaint();    // 화면 다시 그리기
    }


    public static void main(String[] args) {
        new TypingGameFrame(); // JFrame 생성
    }

}

class StartPanel extends JPanel {
    private TypingGameFrame frame;
    private ScorePanel scorePanel = new ScorePanel();
    private GamePanel gamePanel;
    private Round1Panel round1Panel;
    private ImageIcon icon = new ImageIcon("images/startBackground.JPG");
    private Image img = icon.getImage();
    private boolean sound = true;

    private JButton editWordButton;

    SoundPlayer soundPlayer = new SoundPlayer();

    public StartPanel(TypingGameFrame frame) {
        this.frame = frame;
        gamePanel = new GamePanel(frame,scorePanel,soundPlayer); // 게임패널을 만들고 프레임과 스코어패널과 사운드객체를 전달한다.
        this.round1Panel = new Round1Panel(frame,scorePanel,gamePanel,soundPlayer); // 라운드1패널도 동일하다.

        setLayout(null);

        soundPlayer.loadSound("audio/backSound.wav");
        soundPlayer.loopSound();
        soundPlayer.playSound();

        JLabel titleLabel = new JLabel("TypingGame");
        titleLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 90));
        titleLabel.setForeground(new Color(255, 255, 200));  // 글자 색상 설정
        titleLabel.setSize(700, 110);  // 크기 설정
        titleLabel.setLocation(300,20);
        add(titleLabel);

        //시작 버튼
        //시작 버튼의 사이즈와 이벤트를 지정해서 StartPanel에 붙인다.
        ImageIcon startImage = new ImageIcon("images/startButton.JPG");
        JButton startButton = new JButton(startImage);

        int startButtonWidth = 250;  // 버튼의 가로 크기
        int startButtonHeight = 80;  // 버튼의 세로 크기
        int startButtonX = (1200 - startButtonWidth) / 2; // 패널 가로 중앙
        int startButtonY = (800 - startButtonHeight) / 2 + 70;
        startButton.setBounds(startButtonX, startButtonY, startButtonWidth, startButtonHeight);

        // 버튼과 이미지가 같이 커져야 하기 때문에 가져온 이미지를 지정된 크기로 설정
        Image img = ((ImageIcon) startButton.getIcon()).getImage();
        Image scaledImg = img.getScaledInstance(startButtonWidth, startButtonHeight, Image.SCALE_SMOOTH);
        startButton.setIcon(new ImageIcon(scaledImg));
        add(startButton);

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBounds(startButtonX - 10, startButtonY - 5, startButtonWidth + 20, startButtonHeight + 10);
                Image img = ((ImageIcon) startButton.getIcon()).getImage();
                Image scaledImg = img.getScaledInstance(startButtonWidth + 20, startButtonHeight + 10, Image.SCALE_SMOOTH);
                startButton.setIcon(new ImageIcon(scaledImg));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBounds(startButtonX, startButtonY, startButtonWidth, startButtonHeight);
                Image img = ((ImageIcon) startButton.getIcon()).getImage();
                Image scaledImg = img.getScaledInstance(startButtonWidth, startButtonHeight, Image.SCALE_SMOOTH);
                startButton.setIcon(new ImageIcon(scaledImg));
            }


            @Override
            public void mousePressed(MouseEvent e) {
                openIdDialog(); // 버튼을 누르면 캐릭터와 아이디 입력창을 생성한다.
            }
        });


        //단어 편집 버튼
        editWordButton = new JButton("단어 편집");
        int editWordButtonWidth = 200;  // 버튼의 가로 크기
        int editWordButtonHeight = 50;  // 버튼의 세로 크기
        int editWordButtonX = (1200 - editWordButtonWidth) / 2;
        int editWordButtonY = (800 - editWordButtonHeight) / 2 + 150;
        editWordButton.setFont(new Font("", Font.BOLD, 25));
        editWordButton.setBackground(new Color(135, 206, 235));
        editWordButton.setForeground(new Color(255, 255, 170));
        editWordButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true)); //테두리 설정

        editWordButton.setBounds(editWordButtonX, editWordButtonY, editWordButtonWidth, editWordButtonHeight);
        add(editWordButton);
        // 버튼의 마우스 이벤트 설정
        editWordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                editWordButton.setBounds(editWordButtonX - 6, editWordButtonY - 3, editWordButtonWidth + 12, editWordButtonHeight + 6);
                editWordButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                editWordButton.setBounds(editWordButtonX, editWordButtonY, editWordButtonWidth, editWordButtonHeight);
                editWordButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                openWordDialog(); // 버튼을 클릭하면 단어편집 다이얼로그를 생성한다.

            }
        });

        //랭킹 버튼
        JButton LankButton = new JButton("랭킹 보기");
        int LankButtonWidth = 200;  // 버튼의 가로 크기
        int LankButtonHeight = 50;  // 버튼의 세로 크기
        int LankButtonX = (1200 - editWordButtonWidth) / 2;
        int LankButtonY = (800 - editWordButtonHeight) / 2 + 210;
        LankButton.setFont(new Font("고딕", Font.BOLD, 25));
        LankButton.setBackground(new Color(135, 206, 235));
        LankButton.setForeground(new Color(255, 255, 170));
        LankButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true)); // 테두리 설정

        LankButton.setBounds(LankButtonX, LankButtonY, LankButtonWidth, LankButtonHeight);
        add(LankButton);
        // 버튼의 마우스 이벤트 설정
        LankButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LankButton.setBounds(LankButtonX - 6, LankButtonY - 3, LankButtonWidth + 12, LankButtonHeight + 6);
                LankButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LankButton.setBounds(LankButtonX, LankButtonY, LankButtonWidth, LankButtonHeight);
                LankButton.setBorder(new LineBorder(new Color(0, 191, 255), 10, true));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                soundPlayer.stopSound();
                LankPanel lankPanel = new LankPanel(frame,StartPanel.this,soundPlayer);
                LodingPanel lp = new LodingPanel(frame, lankPanel,soundPlayer,sound); //다음으로 전환할 Round1Panel을 전달한다.
                frame.switchToPanel(lp); // 프레임의 컨텐트팬을 LodingPanel로 전환
            }
        });
    }
    private void openWordDialog() {
        WordsDialog wordDialog = new WordsDialog("words.txt"); // 단어편집창 다이얼로그를 생성한다.
        wordDialog.setVisible(true);  // 다이얼로그를 화면에 표시
    }
    private void openIdDialog() {
        InputIdDialog idDialog = new InputIdDialog(frame,round1Panel,soundPlayer); // 아이디입력 다이얼로그를 생성한다.
        idDialog.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

    }
}