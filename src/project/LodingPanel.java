package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import project.LodingPanel.LodingThread;

//로딩 후에 프레임의 패널을 바꿔준다.
class LodingPanel extends JPanel implements Runnable {
    private ImageIcon icon = new ImageIcon("images/loding.JPG");
    private Image img = icon.getImage();
    private TypingGameFrame frame;
    private int dotCount = 0; // 현재 점의 개수
    private boolean lodingFlag = true; // 스레드 실행 상태
    private JPanel nextPanel; // 다음에 표시될 패널
    private ScorePanel scorePanel = null;
    private GamePanel gamePanel = null;
    private String id;
    private SoundPlayer soundPlayer = null;
    private boolean sound = true;
    // 생성자에서 스레드를
    public LodingPanel(TypingGameFrame frame, JPanel nextPanel,SoundPlayer soundPlayer,boolean sound) {
        this.gamePanel = gamePanel;
        this.frame = frame;
        this.nextPanel = nextPanel;
        this.soundPlayer = soundPlayer;
        this.sound = sound;

        setLayout(null);
        Thread lodingAnimationThread = new Thread(this);
        lodingAnimationThread.start(); // 자기자신의 스레드를 시작한다.

        // 바로 로딩스레드 시작. 여기서 애니메이션 스레드를 멈춘다.
        LodingThread lodingThread = new LodingThread(this);
        lodingThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 설정
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

        // 글씨 스타일 설정
        g.setColor(new Color(25, 25, 112));
        g.setFont(new Font("SansSerif", Font.BOLD, 48)); // 폰트 설정

        String lodingMessage = "Loading";
        String dots = "";
        for (int i = 0; i < dotCount; i++) {
            dots += "."; // dotCount 만큼 점을 추가
        }
        String message = lodingMessage + dots;

        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(message)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.drawString(message, x, y); // 중앙에 텍스트 그리기
    }

    @Override
    public void run() {
        int dotPosition = 0; // 점의 현재 위치
        while (lodingFlag) {
            try {
                Thread.sleep(300); // 0.3초 대기
                dotPosition = (dotPosition + 1) % 4; // 점 위치 계산 (0, 1, 2, 3 반복)
                dotCount = dotPosition; // 점 개수를 현재 상태에 맞게 설정한다.
                repaint(); // 화면 다시 그리기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAnimation() {
        lodingFlag = false; // 애니메이션 중단
    }

    public void sountStop() {
        soundPlayer.stopSound();
    }

    class LodingThread extends Thread{ // 로딩스레드 클래스
        private LodingPanel panel = null;
        public LodingThread(LodingPanel panel) {
            this.panel = panel;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(1500); // 2초 대기
                panel.stopAnimation(); // 애니메이션 스레드 중단

                frame.switchToPanel(nextPanel); // 넘겨받은 패널을 프레임의 패널로 설정한다.

                //각종 조건에 맞는 사운드 설정
                if(nextPanel instanceof Round1Panel) {
                    ((Round1Panel) nextPanel).timerStart();
                    soundPlayer.loadSound("audio/monsterBackSound.wav");
                    soundPlayer.loopSound();
                    soundPlayer.playSound();
                }
                else if(nextPanel instanceof StartPanel) {
                    if(sound==true) {
                        soundPlayer.loadSound("audio/backSound.wav");
                        soundPlayer.loopSound();
                        soundPlayer.playSound();
                    }
                }
                else if(nextPanel instanceof LankPanel) {
                    soundPlayer.loadSound("audio/sideBackSound.wav");
                    soundPlayer.loopSound();
                    soundPlayer.playSound();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
