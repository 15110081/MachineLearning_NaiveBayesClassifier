import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        HashSet<String> posWord = new HashSet<String>();
        HashSet<String> negWord = new HashSet<String>();

        List<String> dtWord = new ArrayList<String>();

        HashSet<String> dictionary = new HashSet<String>();

        //POSITIVE WORDS 
        try (BufferedReader br = new BufferedReader(new FileReader("pos.txt"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                posWord.add(line.trim());
                dictionary.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //NEGATIVE WORDS 
        try (BufferedReader br = new BufferedReader(new FileReader("neg.txt"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                negWord.add(line.trim());
                dictionary.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //DATA
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                dtWord.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int totWordCount = posWord.size() + negWord.size();
        int negWordCount = negWord.size();
        int posWordCount = posWord.size();

        double negProb = 1.0 * negWordCount / totWordCount;
        double posProb = 1.0 * posWordCount / totWordCount;

        for (String content : dtWord) {
            double predict_neg_prob = negProb;
            double predict_pos_prob = posProb;

            int found_neg = 0;
            int found_pos = 0;

            String saveCont = content;

            for (String keyword : dictionary) {
                if (content.contains(keyword)) {
                    content = content.replace(keyword, "");

                    if (negWord.contains(keyword)) {
                        predict_neg_prob = predict_neg_prob * 1.0 / negWordCount;
                        found_neg++;
                    }

                    if (posWord.contains(keyword)) {
                        predict_pos_prob = predict_pos_prob * 1.0 / posWordCount;
                        found_pos++;
                    }
                }
            }

            //smooth
            if (found_neg == 0) predict_neg_prob = 0;
            if (found_pos == 0) predict_pos_prob = 0;

            System.out.println("Bình luận:" + saveCont);

            System.out.println("Xác suất: Chê " + predict_neg_prob + " | Khen: " + predict_pos_prob);

            if (predict_neg_prob > predict_pos_prob) {
                System.out.println("Dự đoán: Bình luận chê");
            } else if (predict_neg_prob < predict_pos_prob) {
                System.out.println("Dự đoán: Bình luận khen");
            } else {
                System.out.println("Dự đoán: không xác định");
            }
        }
    }
}