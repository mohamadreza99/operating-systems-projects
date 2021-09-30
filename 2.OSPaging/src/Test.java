import java.io.IOException;
import java.io.RandomAccessFile;

public class Test {
    public static void main(String[] args) throws IOException {
        RandomAccessFile r = new RandomAccessFile("BACKINGSTORE.bin", "r");
        r.seek(515);
        System.out.println(r.readByte());

    }
}
