package mars.mips.SO.ProcessManager;

import java.util.List;
import java.util.ArrayList;

public class Semaphore {
    private static List<Semaphore> instances = new ArrayList<Semaphore>();

    private int value;
    private int varAddress;

    public Semaphore(int value, int address) {
        this.value = value;
        this.varAddress = address;
        instances.add(this);
    }

    public static List<Semaphore> getInstances(int varAddress) {
        return instances;
    }

    public static void removeInstances(int varAddress) {
        Semaphore s = getSemaphore(varAddress);
        if (s != null) {
            instances.remove(s);
        }
    }

    public static Semaphore getSemaphore(int varAddress) {
        for (Semaphore instance: instances) {
            if (instance.getAddress() == varAddress) {
                return instance;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public int getAddress() {
        return varAddress;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
