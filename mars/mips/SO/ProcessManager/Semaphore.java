package mars.mips.SO.ProcessManager;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class Semaphore {
    private static List<Semaphore> instances = new ArrayList<Semaphore>();

    public static List<Semaphore> getInstances(int varAddress) {
        return instances;
    }

    public static void removeInstance(int varAddress) {
        Semaphore s = getSemaphore(varAddress);
        
        if (s == null) {
            System.out.println("Erro ao terminar semáforo: código inválido.");
            return;
        }

        instances.remove(s);
    }

    public static Semaphore getSemaphore(int varAddress) {
        for (Semaphore instance: instances) {
            if (instance.getAddress() == varAddress) {
                return instance;
            }
        }
        return null;
    }

    private int value;
    private int varAddress;
    private Queue<ProcessControlBlock> blockedProcesses = new LinkedList<ProcessControlBlock>(); 
    
    public Semaphore(int value, int address) {
        this.value = value;
        this.varAddress = address;
        instances.add(this);
    }

    /*
     * Adiciona novo processo a fila de processos bloqueados
     */
    public void add(ProcessControlBlock process) {
        process.setProgramState("blocked");
        this.blockedProcesses.add(process);
    }

    /*
     * Remove processo da fila de processos bloquados
     * @return: número de PID do processo removido
     */
    public ProcessControlBlock pop() {
        return this.blockedProcesses.poll();
    }

    public Queue<ProcessControlBlock> getBlockedProcesses() {
        // TODO: retornar uma cópia da fila
        return blockedProcesses;
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
