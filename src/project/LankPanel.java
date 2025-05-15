package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class LankPanel extends JPanel {
    private StartPanel startPanel;
    private HashMap<String, Integer> scoreMap = new HashMap<>();
    private TypingGameFrame frame = null;
    private SoundPlayer soundPlayer = null;
    private boolean sound = true;

    public LankPanel(TypingGameFrame frame,StartPanel startPanel,SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
        this.frame = frame;
        this.startPanel = startPanel;

        setLayout(new BorderLayout());

        Top10Panel top10Panel = new Top10Panel();
        add(top10Panel, BorderLayout.CENTER); // Top10Panel을 추가
    }

    class Top10Panel extends JPanel {
        private ImageIcon icon = new ImageIcon("images/startBackground.JPG");
        private Image img = icon.getImage();
        private JLabel[] lankLabels = new JLabel[10];

        public Top10Panel() {
            setLayout(null);

            JLabel top10Label = new JLabel("TOP 10");
            top10Label.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
            top10Label.setForeground(new Color(255, 255, 200));  // 글자 색상 설정
            top10Label.setSize(300, 100);  // 크기 설정
            top10Label.setLocation(470,70);
            add(top10Label);


            int labelX = 250;
            int labelY = 220;
            int space = 0;

            // lankLabels 초기화 및 위치 설정
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    labelX += 400;
                    space = 0;
                }
                lankLabels[i] = new JLabel(i + 1 + ".");
                lankLabels[i].setLocation(labelX, labelY + space);
                lankLabels[i].setSize(250, 70);
                lankLabels[i].setFont(new Font("", Font.BOLD, 24));  // 두껍고 큰 폰트 설정
                lankLabels[i].setOpaque(true);
                lankLabels[i].setBackground(new Color(135, 206, 250,220));
                lankLabels[i].setForeground(new Color(255, 255, 200));
                lankLabels[i].setBorder(new LineBorder(new Color(0, 170, 255), 7));
                lankLabels[i].setHorizontalAlignment(JLabel.CENTER);  // 수평 중앙 정렬
                lankLabels[i].setVerticalAlignment(JLabel.CENTER);  // 수직 중앙 정렬
                space += 100;
                add(lankLabels[i]);
            }

            ImageIcon outIcon = new ImageIcon("images/outButton.JPG");

            int buttonWidth = 170;
            int buttonHeight = 50;

            int outButtonX = 950;
            int outButtonY = 690;
            Image scaledoutImage = outIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            JButton outButton = new JButton(new ImageIcon(scaledoutImage));

            outButton.setBounds(outButtonX, outButtonY, buttonWidth, buttonHeight);

            outButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    outButton.setBounds(outButtonX - 10, outButtonY - 5, buttonWidth + 20, buttonHeight + 10);
                    Image img = ((ImageIcon) outButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth + 20, buttonHeight + 10, Image.SCALE_SMOOTH);
                    outButton.setIcon(new ImageIcon(scaledImg));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    outButton.setBounds(outButtonX, outButtonY, buttonWidth, buttonHeight);
                    Image img = ((ImageIcon) outButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                    outButton.setIcon(new ImageIcon(scaledImg));

                }
                @Override
                public void mousePressed(MouseEvent e) {
                    soundPlayer.stopSound();

                    LodingPanel lp = new LodingPanel(frame, startPanel,soundPlayer,sound); //다음으로 전환할 Round1Panel을 전달한다.
                    frame.switchToPanel(lp); // 프레임의 컨텐트팬을 LodingPanel로 전환
                }

            });

            add(outButton);
            // 점수 로드 및 정렬
            try (Scanner scanner = new Scanner(new FileReader("lank.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    if (tokenizer.countTokens() == 2) {
                        String name = tokenizer.nextToken();
                        int score = Integer.parseInt(tokenizer.nextToken());
                        scoreMap.put(name, score);  // 이름과 점수를 해시맵에 추가
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 점수를 기준으로 내림차순으로 정렬할 이름 목록 생성
            List<String> sortedNames = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                sortedNames.add(entry.getKey());
            }

            // 이름을 기준으로 점수를 내림차순으로 정렬
            Collections.sort(sortedNames, (name1, name2) -> {
                int score1 = scoreMap.get(name1);
                int score2 = scoreMap.get(name2);
                return Integer.compare(score2, score1);  // 내림차순 정렬
            });

            // 상위 10명 이름과 점수를 lankLabels에 삽입
            int count = 0;
            for (String name : sortedNames) {
                if (count++ >= 10) break;  // 상위 10명만 표시
                int score = scoreMap.get(name);
                lankLabels[count - 1].setText((count) + ". " + name + " : " + score);  // 이름과 점수를 lankLabels에 설정
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

}
