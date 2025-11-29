package test.gemini;

import entity.IDVerfication;

public class GeminiClientTest {
    public static void main(String[] args) {
        try {
            IDVerfication idVerfication = new IDVerfication(); 
            String response = idVerfication.getResponse("src/main/java/test/gemini/resources/id_test.jpeg");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
