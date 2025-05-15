package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class InputIdDialog extends JDialog{
    private ImageIcon icon = new ImageIcon("images/idBackground.JPG");
    private Image img = icon.getImage();
    private JTextField textId = null;
    private ImageIcon bazziIcon = new ImageIcon("images/bazziId.JPG");
    private ImageIcon uniIcon = new ImageIcon("images/uniId.JPG");
    private TypingGameFrame frame = null;
    private Round1Panel round1Panel = null; // 다음에 표시될 패널
    private boolean bazzi = false;
    private boolean uni = false;
    private boolean inputId = false;
    SoundPlayer soundPlayer = null;
    private boolean sound = true;

    public InputIdDialog(TypingGameFrame frame, Round1Panel round1Panel,SoundPlayer soundPlayer) {
        this.frame = frame;
        this.round1Panel = round1Panel;
        this.soundPlayer = soundPlayer;
        setTitle("캐릭터 선택");
        setSize(700,500);
        setLayout(new BorderLayout());
        CenterPanel cp = new CenterPanel();
        SouthPanel sp = new SouthPanel();
        sp.setPreferredSize(new Dimension(0,80));
        add(cp,BorderLayout.CENTER);
        add(sp,BorderLayout.SOUTH);


    }

    class CenterPanel extends JPanel {
        public CenterPanel() {
            setLayout(null);

            int buttonWidth = 400;
            int buttonHeight = 120;

            int bazziButtonX = 40;
            int bazziButtonY = 40;

            int uniButtonX = 40;
            int uniButtonY = 200;
            Image scaledBazziImage = bazziIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            Image scaledUniImage = uniIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

            JButton bazziButton = new JButton(new ImageIcon(scaledBazziImage));
            JButton uniButton = new JButton(new ImageIcon(scaledUniImage));

            // 버튼의 크기를 지정된 크기로 설정
            bazziButton.setBounds(bazziButtonX, bazziButtonY, buttonWidth, buttonHeight);
            uniButton.setBounds(uniButtonX, uniButtonY, buttonWidth, buttonHeight);

            bazziButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    bazziButton.setBounds(bazziButtonX - 10, bazziButtonY - 5, buttonWidth + 20, buttonHeight + 10);
                    Image img = ((ImageIcon) bazziButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth + 20, buttonHeight + 10, Image.SCALE_SMOOTH);
                    bazziButton.setIcon(new ImageIcon(scaledImg));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    bazziButton.setBounds(bazziButtonX, bazziButtonY, buttonWidth, buttonHeight);
                    Image img = ((ImageIcon) bazziButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                    bazziButton.setIcon(new ImageIcon(scaledImg));

                }
                @Override
                public void mousePressed(MouseEvent e) {
                    bazziButton.setBounds(bazziButtonX - 10, bazziButtonY - 5, buttonWidth + 20, buttonHeight + 10);
                    uniButton.setBounds(uniButtonX, uniButtonY, buttonWidth, buttonHeight);
                    bazziButton.setBorder(new LineBorder(Color.BLUE, 7, true));
                    uniButton.setBorder(null); // 우니 버튼의 테두리 제거
                    bazzi = true;
                    uni = false;
                }

            });

            uniButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    uniButton.setBounds(uniButtonX - 10, uniButtonY - 5, buttonWidth + 20, buttonHeight + 10);
                    Image img = ((ImageIcon) uniButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth + 20, buttonHeight + 10, Image.SCALE_SMOOTH);
                    uniButton.setIcon(new ImageIcon(scaledImg));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    uniButton.setBounds(uniButtonX, uniButtonY, buttonWidth, buttonHeight);
                    Image img = ((ImageIcon) uniButton.getIcon()).getImage();
                    Image scaledImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                    uniButton.setIcon(new ImageIcon(scaledImg));

                }
                @Override
                public void mousePressed(MouseEvent e) {
                    uniButton.setBounds(uniButtonX - 10, uniButtonY - 5, buttonWidth + 20, buttonHeight + 10);
                    bazziButton.setBounds(bazziButtonX, bazziButtonY, buttonWidth, buttonHeight);
                    uniButton.setBorder(new LineBorder(Color.BLUE, 7, true));
                    bazziButton.setBorder(null); // 우니 버튼의 테두리 제거
                    bazzi = false;
                    uni = true;
                }

            });

            add(bazziButton);
            add(uniButton);

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }
    class SouthPanel extends JPanel {
        public SouthPanel() {
            setLayout(new FlowLayout());
            setBackground(Color.blue);

            JLabel textLabel = new JLabel("아이디 입력");
            textLabel.setForeground(new Color(255, 255, 20));
            textLabel.setFont(new Font("", Font.BOLD, 18));

            textId = new JTextField(10);
            textId.setFont(new Font("", Font.PLAIN, 18));  // 텍스트 필드 글꼴 설정
            textId.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 235), 5));

            ImageIcon startIcon = new ImageIcon("images/startButton2.JPG");
            JButton startButton = new JButton(startIcon);
            startButton.setBounds(0, 0, 100, 100);
            Image img = ((ImageIcon) startButton.getIcon()).getImage();
            Image scaledImg = img.getScaledInstance(170, 60, Image.SCALE_SMOOTH);
            startButton.setIcon(new ImageIcon(scaledImg));
            startButton.setPreferredSize(new Dimension(165, 55));

            startButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    String id = textId.getText();
                    if(bazzi) {
                        round1Panel.setCharacter("bazzi");
                        round1Panel.setId(id);
                    }
                    else if(uni) {
                        round1Panel.setCharacter("uni");
                        round1Panel.setId(id);
                    }

                    if(id.isEmpty() || (!bazzi && !uni)) {
                        JOptionPane.showMessageDialog(frame, "아이디와 캐릭터를 선택해 주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        soundPlayer.stopSound();
                        LodingPanel lp = new LodingPanel(frame, round1Panel,soundPlayer,sound); //다음으로 전환할 Round1Panel을 전달한다.
                        frame.switchToPanel(lp); // 프레임의 컨텐트팬을 LodingPanel로 전환한다.
                        dispose(); // 다이얼로그를 닫는다.
                    }


                }
            });

            add(textLabel);
            add(textId);
            add(startButton);
        }
    }


}
