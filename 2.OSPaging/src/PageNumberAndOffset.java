import java.util.Scanner;

public class PageNumberAndOffset {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int pageNumber = a / 256;
        int offset = a % 256;
        System.out.println("pageNumber : " + pageNumber + " offset : " + offset);
    }
}
