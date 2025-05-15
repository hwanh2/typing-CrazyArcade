package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class WordsDialog extends JDialog {
    private JTextArea textArea;
    private String fileName;

    public WordsDialog(String fileName) {
        this.fileName = fileName;
        setTitle("단어 입력 창");
        setSize(350, 350);
        setLayout(new BorderLayout());

        // 텍스트 영역 설정
        textArea = new JTextArea();
        textArea.setEditable(false);  // 텍스트 수정 불가
        textArea.setFocusable(false); // 텍스트에 포커스 못 받게 함
        textArea.setFont(new Font("", Font.BOLD, 14));
        setLocationRelativeTo(null);

        // 스크롤 영역 추가
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        readFile();

        // NORTH영역에 단어 입력, 저장 버튼 추가
        NorthPanel northPanel = new NorthPanel();
        add(northPanel, BorderLayout.NORTH);
        northPanel.setPreferredSize(new Dimension(50, 40));

        // 왼쪽, 오른쪽에 패널 추가 (디자인용)
        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(50, 100));
        westPanel.setBackground(new Color(0, 191, 255)); // 밝은 색
        add(westPanel, BorderLayout.WEST);

        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(50, 100));
        eastPanel.setBackground(new Color(0, 191, 255)); // 밝은 색
        add(eastPanel, BorderLayout.EAST);

        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(50, 40));
        southPanel.setBackground(new Color(0, 191, 255)); // 밝은 색
        add(southPanel, BorderLayout.SOUTH);
    }

    // 파일에서 단어를 읽어 텍스트 영역에 추가
    public void readFile() {
        try {
            Scanner scanner = new Scanner(new FileReader(fileName));
            while (scanner.hasNext()) {
                String word = scanner.nextLine();
                textArea.append(word + "\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            System.exit(0);
        }
    }

    // 북쪽 패널 (단어 입력 및 저장 버튼)
    class NorthPanel extends JPanel {
        public NorthPanel() {
            setLayout(new FlowLayout());
            setBackground(new Color(0, 191, 255));  // 배경을 밝은 노란색으로 설정

            // 단어 입력을 위한 텍스트 필드
            JLabel textLabel = new JLabel("단어 입력");
            textLabel.setFont(new Font("", Font.BOLD, 18));
            textLabel.setForeground(new Color(25, 25, 120));  // 텍스트 색상 빨간색
            add(textLabel);

            JTextField textField = new JTextField(10);
            textField.setFont(new Font("", Font.PLAIN, 14));  // 텍스트 필드 글꼴 설정
            add(textField);

            // 저장 버튼
            JButton saveButton = new JButton("저장");
            saveButton.setFont(new Font("", Font.BOLD, 14));
            saveButton.setBackground(Color.GREEN); // 버튼 배경을 초록색으로 설정
            saveButton.setForeground(Color.WHITE); // 버튼 텍스트 색상 하얀색
            saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 버튼 경계 설정
            saveButton.setFocusPainted(false); // 버튼 클릭 시 테두리 없애기
            saveButton.setPreferredSize(new Dimension(50, 30));
            add(saveButton);

            // 저장 버튼 클릭 이벤트
            saveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    String text = textField.getText(); // 텍스트 필드에서 입력받은 단어

                    if (text != null && !text.trim().isEmpty()) { // 입력된 텍스트가 비어있지 않으면
                        // 텍스트 영역에 추가
                        textArea.append(text + "\n");

                        // 파일에 추가
                        saveToFile(text);
                    }

                    // 텍스트 필드 초기화
                    textField.setText("");
                }
            });
        }
    }

    // 파일에 단어 추가하는 메서드
    private void saveToFile(String word) {
        try {
            // 파일에 단어 추가 (append 모드로 파일 열기)
            FileWriter fw = new FileWriter(fileName, true); // true: 기존 내용에 추가
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(word);  // 단어 쓰기
            bw.newLine();    // 새로운 줄 추가
            bw.close();      // 파일 닫기
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생");
        }
    }
}
