import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class addressGen {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("addresses.txt");
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            fileWriter.write(String.valueOf(random.nextInt(65536))+"\n");
        }
        fileWriter.close();
    }
}
