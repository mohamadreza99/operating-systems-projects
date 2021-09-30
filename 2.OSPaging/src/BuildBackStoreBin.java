import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class BuildBackStoreBin {
    public static void main(String[] args) throws IOException {
        RandomAccessFile r = new RandomAccessFile("BACKINGSTORE.bin", "rw");
        Random random = new Random();
        byte[] b = new byte[256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                b[j] = ((byte) random.nextInt());
            }
//            if (i == 0) {
//                System.out.println(b[3]);
//            }
            r.write(b);
        }

//        r.seek(1);//prints the byte number 1; byte numbers beginning at 0
//        byte[] read = new byte[256];
//        r.read(read);
//        System.out.println((byte)r.readByte());//prints the byte where the file pointer is determined by seek method
    }
}
