import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    private int counter;

    public static void main(String[] args) throws InterruptedException {
        new Worker().main();  // объект This

    }

}

class Worker {
    Random random = new Random();
    Object lock1 = new Object();   //создаем два объекта. Они нужны для случаев когда один поток берет монитор объекта,
    Object lock2 = new Object();   //второй поток не ждал пока освободиться, а работал с другим объектом

    private List<Integer> list = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    public void addToList1() throws InterruptedException {
        synchronized (lock1) {
            for (int i = 0; i < 1000; i++) {
                Thread.sleep(1);
                list.add(random.nextInt(100));
            }
        }
    }


    public void addToList2() throws InterruptedException {
        synchronized (lock2) {
            for (int i = 0; i < 1000; i++) {
                Thread.sleep(1);
                list2.add(random.nextInt(100));
            }
        }
    }

    public void work() throws InterruptedException {
        addToList1();
        addToList2();
    }

    public void main() throws InterruptedException {
        long before = System.currentTimeMillis();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    work();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    work();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long after = System.currentTimeMillis();
        System.out.println("Program took: " + " " + (after - before));
        System.out.println("List1 " + list.size());
        System.out.println("List2 " + list2.size());
    }
}
    /*
    synchronized применяется только на методах, потому создаем метод с ключевым словом и в нем инкрементируем переменную
    Работаем таким образом, что только один поток может работать с телом метода
     */

