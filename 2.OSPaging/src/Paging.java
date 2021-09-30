import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Paging {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("addresses.txt"));
        RandomAccessFile r = new RandomAccessFile("BACKINGSTORE.bin", "r");
        int addressLength = 1000;
        int freeMemoryFrameNum = 0;
        int pageFaultCount = 0;
        int TLBHitCount = 0;
        int turn = 0;

        Integer[] pageTable = new Integer[256];
        byte[][] memory = new byte[256][256];
        LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> TLB = new LinkedHashMap<>(16);


        for (int i = 0; i < addressLength; i++) {
            int logicalAdrs = scanner.nextInt();
            System.out.println("logical address is : " + logicalAdrs);
            int VirtualPageNumber = logicalAdrs / 256;
            int offset = logicalAdrs % 256;
            int pageFrameNum = -1;
            boolean flagTLB = false;
            for (int j = 0; j < TLB.size(); j++) {
                if (TLB.get(j).containsKey(VirtualPageNumber)) {
                    System.err.println("TLB hit");
                    pageFrameNum = TLB.get(j).get(VirtualPageNumber);
                    flagTLB = true;
                    TLBHitCount++;
                    break;
                }
            }
            if (!flagTLB && pageTable[VirtualPageNumber] == null) {
                LinkedHashMap<Integer, Integer> TLBEntry = new LinkedHashMap<>();
                TLBEntry.put(VirtualPageNumber, freeMemoryFrameNum);
                TLB.put(turn++, TLBEntry);
                if (turn == 16) {
                    turn = 0;
                }
                pageFaultCount++;
                byte[] read = new byte[256];
                r.seek(VirtualPageNumber * 256);
                r.read(read);
                memory[freeMemoryFrameNum] = read;
                pageFrameNum = freeMemoryFrameNum;
                pageTable[VirtualPageNumber] = freeMemoryFrameNum++;
            } else if (!flagTLB) {
                LinkedHashMap<Integer, Integer> TLBEntry = new LinkedHashMap<>();
                TLBEntry.put(VirtualPageNumber, pageTable[VirtualPageNumber]);
                TLB.put(turn++, TLBEntry);
                if (turn == 16) {
                    turn = 0;
                }
                pageFrameNum = pageTable[VirtualPageNumber];
            }

            System.out.println("physical address is : " + (pageFrameNum * 256 + offset));
            System.out.println("the value in the specified address is : " + (byte) memory[pageFrameNum][offset]);
            System.out.flush();
            System.out.println("---------------------");

        }
        System.err.println("page Fault rate is : " + (pageFaultCount * 1.0) / addressLength);
        System.err.println("TLB hit rate is : " + (TLBHitCount * 1.0) / addressLength);
    }
}
