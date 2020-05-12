import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class BirdArray implements Serializable {
    private static volatile BirdArray birdArray;
    private static volatile LinkedList<Bird> list = new LinkedList<Bird>();
    private static TreeSet<Integer> set = new TreeSet<Integer>();
    private static HashMap<Integer, String> map = new HashMap<Integer, String>();
    private BirdArray() {}

    public static synchronized BirdArray getBirdArray() {
        if (birdArray == null) {
            birdArray = new BirdArray();
        }
        return birdArray;
    }

    public synchronized void addBird(Bird bird, String time) {
        int id = bird.hashCode();
        list.addLast(bird);
        set.add(id);
        map.put(id, time);
    }

    public synchronized void checkBirds(double timeLife1, double timeLife2, double time) {
        Vector<Bird> birds = new Vector<Bird>();
        for (Bird bird : list) {
            double period = bird instanceof BigBird ? timeLife1 : timeLife2;
            double bornTime = -1;
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                if (entry.getKey() == bird.hashCode()) {
                    bornTime = Double.parseDouble(entry.getValue());
                    break;
                }
            }
            if (bornTime + period <= time) {
                birds.addElement(bird);
                System.out.println(bird);
            }
        }
        for (Bird bird : birds) {
            String bornTime = "";
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                if (entry.getKey() == bird.hashCode()){
                    bornTime = entry.getValue();
                }
            }
            set.remove(bird.hashCode());
            map.remove(bird.hashCode());
            list.remove(bird);
        }
    }

    public synchronized void paintBirds(Graphics g) {
        for (Bird bird : list) {
            bird.drawBird(g);
        }
    }

    public synchronized void moveBigBirds(int dX, int dY) {
        for (Bird bird : list) {
            if (bird instanceof BigBird) {
                bird.move(dX, dY);
            }
        }
    }

    public synchronized void moveSmallBirds(int dX, int dY) {
        for (Bird bird : list) {
            if (bird instanceof SmallBird) {
                bird.move(dX, dY);
            }
        }
    }

    public void removeAllBirds() {
        list.clear();
        map.clear();
        set.clear();
    }

    public synchronized HashMap<Integer, String> getMap() {
        return map;
    }

    public synchronized LinkedList<Bird> getList() {
        return list;
    }

    public synchronized TreeSet<Integer> getSet() {
        return set;
    }


    public synchronized void setBirdArray(LinkedList<Bird> list, String time) {
        for (Bird bird : list) {
            this.addBird(bird, time);
        }
    }
    public synchronized void printInfo() {
        System.out.println("-- LIST --");
        for (Bird bird : list) {
            System.out.println(bird);
        }
        System.out.println("-- SET --");
        for (int n : set) {
            System.out.println(n);
        }
        System.out.println("-- MAP --");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
