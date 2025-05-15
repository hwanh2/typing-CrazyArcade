package project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

//words.txt 파일을 읽고 벡터에 저장하고 벡터로부터 랜덤하게 단어를 추출하는 클래스
class Words {
    Vector<String> wordVector = new Vector<String>();

    public Words(String fileName) {
        try {
            Scanner scanner = new Scanner(new FileReader(fileName));
            while(scanner.hasNext()) { // 파일 끝까지 읽음
                String word = scanner.nextLine(); // 한 라인을 읽고 '\n'을 버린 나머지 문자열만 리턴
                wordVector.add(word); // 문자열을 벡터에 저장
            }
            scanner.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("file not found error");
            System.exit(0);
        }
    }

    public String getRandomWord() {
        final int WORDMAX = wordVector.size(); // 총 단어의 개수
        int index = (int)(Math.random()*WORDMAX);
        return wordVector.get(index); // 랜덤한 단어 리턴
    }
}
