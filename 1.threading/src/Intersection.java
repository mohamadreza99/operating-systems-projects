import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mohamadreza fereydooni
 * @version 1.0
 * this is a program to simulate an intersection by multithread programming
 */
public class Intersection {
    /**
     *
     * @param args <p>because I generate a .jar file for this program, if we want to give the inputs from the command line to the
     *             executable jar file, we should first give the number of cars we want and then give the source and destination
     *             of each car separated by a delimiter like a space.
     *             if we don't use the handy input in command line, the program executes the example in the project text.</p>
     * @throws InterruptedException <p>because we await on a countDownLatch to all the threads finish their works and then we print
     * the string "All works Completed"</p>
     *
     * we use four locks as binary semaphores or mutexes for each region in the intersection as the problem declared them
     */
    public static void main(String[] args) throws InterruptedException {
        Lock ne = new ReentrantLock();
        Lock nw = new ReentrantLock();
        Lock se = new ReentrantLock();
        Lock sw = new ReentrantLock();

        CountDownLatch latch = null;
        if (args.length == 0) {
            latch = new CountDownLatch(5);

            new Car(1, 3, ne, nw, se, sw, latch).start();
            new Car(3, 1, ne, nw, se, sw, latch).start();
            new Car(1, 4, ne, nw, se, sw, latch).start();
            new Car(2, 4, ne, nw, se, sw, latch).start();
            new Car(3, 1, ne, nw, se, sw, latch).start();
        } else {
            int count = Integer.valueOf(args[0]);
            latch = new CountDownLatch(count);
            for (int i = 1; i < args.length; i += 2) {
                new Car(Integer.valueOf(args[i]), Integer.valueOf(args[i + 1]), ne, nw, se, sw, latch).start();
            }
        }
        latch.await();
        System.out.println("All works Completed");
    }
}

/**
 * this class simulate each car wanna to pass the intersection by creating a thread do that car's job
 */
class Car extends Thread {
    int src, dst;
    Lock ne, nw, se, sw;
    CountDownLatch latch;

    /**
     *
     * @param src
     * @param dst
     * @param ne
     * @param nw
     * @param se
     * @param sw
     * @param latch
     * we set the locks and the latch to give the access to each thread to work with them
     */
    public Car(int src, int dst, Lock ne, Lock nw, Lock se, Lock sw, CountDownLatch latch) {
        this.src = src;
        this.dst = dst;
        this.ne = ne;
        this.nw = nw;
        this.se = se;
        this.sw = sw;
        this.latch = latch;
    }

    /**
     * in this method I define the job of each thread
     * each thread depends on it's src and dst, must give the lock of some regions in the intersection.
     * I do this according to assumptions of the problem like all cars should wait for 1 second when reaching the intersection and
     * if they can give the locks the car need to enter the intersection, it must wait for 2 seconds and then check the locks again.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Lock> locks = new ArrayList<>();

        switch (src) {
            case 1: {
                if (dst == 2) {
                    boolean tmp = false;
                    while (!tmp) {
                        tmp = sw.tryLock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.add(sw);
                            passTheWayAndUnlock(locks);
                        }
                    }
                }

                if (dst == 3) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (sw.tryLock())
                            if (se.tryLock())
                                tmp = true;
                            else
                                sw.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(sw, se));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                if (dst == 4) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (sw.tryLock())
                            if (se.tryLock()) {
                                if (ne.tryLock()) {
                                    tmp = true;
                                } else {
                                    sw.unlock();
                                    se.unlock();
                                }
                            } else
                                sw.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(sw, se, ne));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                break;
            }
            case 2: {
                if (dst == 3) {
                    boolean tmp = false;
                    while (!tmp) {
                        tmp = se.tryLock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.add(se);
                            passTheWayAndUnlock(locks);
                        }
                    }
                }

                if (dst == 4) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (se.tryLock())
                            if (ne.tryLock())
                                tmp = true;
                            else
                                se.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(se, ne));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                if (dst == 1) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (se.tryLock())
                            if (ne.tryLock()) {
                                if (nw.tryLock()) {
                                    tmp = true;
                                } else {
                                    ne.unlock();
                                    se.unlock();
                                }
                            } else
                                se.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(se, ne, nw));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                break;
            }
            case 3: {
                if (dst == 4) {
                    boolean tmp = false;
                    while (!tmp) {
                        tmp = ne.tryLock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.add(ne);
                            passTheWayAndUnlock(locks);
                        }
                    }
                }

                if (dst == 1) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (ne.tryLock())
                            if (nw.tryLock())
                                tmp = true;
                            else
                                ne.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(ne, nw));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                if (dst == 2) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (ne.tryLock())
                            if (nw.tryLock()) {
                                if (sw.tryLock()) {
                                    tmp = true;
                                } else {
                                    ne.unlock();
                                    nw.unlock();
                                }
                            } else
                                ne.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(ne, nw, sw));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                break;
            }
            case 4: {
                if (dst == 1) {
                    boolean tmp = false;
                    while (!tmp) {
                        tmp = nw.tryLock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.add(nw);
                            passTheWayAndUnlock(locks);
                        }
                    }
                }

                if (dst == 2) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (nw.tryLock())
                            if (sw.tryLock())
                                tmp = true;
                            else
                                nw.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(nw, sw));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                if (dst == 3) {
                    boolean tmp = false;
                    while (!tmp) {
                        if (nw.tryLock())
                            if (sw.tryLock()) {
                                if (se.tryLock()) {
                                    tmp = true;
                                } else {
                                    nw.unlock();
                                    sw.unlock();
                                }
                            } else
                                nw.unlock();
                        if (tmp == false) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            locks.addAll(Arrays.asList(nw, sw, se));
                            passTheWayAndUnlock(locks);
                        }
                    }
                }
                break;

            }

        }
        latch.countDown();

    }

    /**
     * if a car gives all the locks of the regions it needed to pass the intersection, it will invoke this method to pass the area.
     * according to the assumptions of the problem declaration,it will take 1 second for each region the car passes, so we sleep
     * 1000 milli seconds and then after passing that region, we will release the lock of it.
     * after passing the intersection, we print an string to show that this car passes.
     * @param locks the list of region locks that this car acquire to pass the intersection.
     */
    private void passTheWayAndUnlock(ArrayList<Lock> locks) {
        try {
            System.out.println("car with [" + src + " to " + dst + "] wanna pass the intersection.");
            while (!locks.isEmpty()) {
                Thread.sleep(1000);
                locks.remove(0).unlock();
            }
            System.out.println("car with [" + src + " to " + dst + "] passes safely");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//        private void passTheWayAndUnlock(ArrayList<Lock> locks) {
//        try {
//            Thread.sleep(locks.size() * 1000);
//            System.out.println("car with [" + src + " to " + dst + "] passes safely");
//            for (Lock lock : locks) {
//                lock.unlock();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
