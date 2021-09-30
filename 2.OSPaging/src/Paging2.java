import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Paging2 {
    public static int leastRecentlyUsed(int[] lru) {
        int min = 0;
        int temp = 0;
        for (int i = 0; i < lru.length; i++) {
            if (lru[i] < temp) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("addresses.txt"));
        RandomAccessFile r = new RandomAccessFile("BACKINGSTORE.bin", "r");
        int addressLength = 1000;
        int freeMemoryFrameNum = 0;
        boolean memoryFullFlag = false;
        int pageFaultCount = 0;
        int TLBHitCount = 0;
        int turn = 0;

        Integer[] pageTable = new Integer[256];
        byte[][] memory = new byte[128][256];
        int[] LRU = new int[128];
        LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> TLB = new LinkedHashMap<>(16);


        for (int i = 0; i < addressLength; i++) {
            if (freeMemoryFrameNum == 128) {
                memoryFullFlag = true;
            }
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
                    LRU[pageFrameNum] /= 2;
                    LRU[pageFrameNum] += 128;
                    LRU[pageFrameNum] %= 256;
                    flagTLB = true;
                    TLBHitCount++;
                    break;
                }
            }
            if (!flagTLB && pageTable[VirtualPageNumber] == null) {

                if (memoryFullFlag) {
                    pageFaultCount++;
                    freeMemoryFrameNum = leastRecentlyUsed(LRU);
                    LRU[freeMemoryFrameNum] = 128;
                    byte[] read = new byte[256];
                    r.seek(VirtualPageNumber * 256);
                    r.read(read);
                    memory[freeMemoryFrameNum] = read;
                    pageFrameNum = freeMemoryFrameNum;
                    for (int j = 0; j < pageTable.length; j++) {
                        if (pageTable[j] == null) {
                            continue;
                        }
                        if (pageTable[j] == freeMemoryFrameNum) {
                            pageTable[j] = null;
                            pageTable[VirtualPageNumber] = freeMemoryFrameNum;
                            boolean existInTLB = false;
                            for (int k = 0; k < TLB.size(); k++) {
                                if (TLB.get(k).containsKey(j)) {
                                    LinkedHashMap<Integer, Integer> TLBEntry = new LinkedHashMap<>();
                                    TLBEntry.put(j, freeMemoryFrameNum);
                                    TLB.put(k, TLBEntry);
                                    existInTLB = true;
                                    break;
                                }
                            }
                            if (!existInTLB) {
                                LinkedHashMap<Integer, Integer> TLBEntry = new LinkedHashMap<>();
                                TLBEntry.put(j, freeMemoryFrameNum);
                                TLB.put(turn++, TLBEntry);
                                if (turn == 16) {
                                    turn = 0;
                                }
                            }
                            break;
                        }
                    }
                } else {
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

                }

            } else if (!flagTLB) {
                LinkedHashMap<Integer, Integer> TLBEntry = new LinkedHashMap<>();
                TLBEntry.put(VirtualPageNumber, pageTable[VirtualPageNumber]);
                TLB.put(turn++, TLBEntry);
                if (turn == 16) {
                    turn = 0;
                }
                int tmp = pageTable[VirtualPageNumber];
                LRU[tmp] /= 2;
                LRU[tmp] += 128;
                LRU[tmp] %= 256;
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
