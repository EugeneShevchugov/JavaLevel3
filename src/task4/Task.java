package task4;

public class Task {

    private volatile char currentLetter = 'A';
    private final int COUNT = 5;

    public static void main(String[] args) {
        Task t = new Task();
        Thread t1 = new Thread(t::printA);
        Thread t2 = new Thread(t::printB);
        Thread t3 = new Thread(t::printC);
        t1.start();
        t2.start();
        t3.start();
    }

    public void printA() {
        synchronized (this) {
            try {
                for (int i = 0; i < COUNT; i++) {
                    while (currentLetter != 'A') {
                        this.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (this) {
            try {
                for (int i = 0; i < COUNT; i++) {
                    while (currentLetter != 'B') {
                        this.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (this) {
            try {
                for (int i = 0; i < COUNT; i++) {
                    while (currentLetter != 'C') {
                        this.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
