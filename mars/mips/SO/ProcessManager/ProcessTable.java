package mars.mips.SO.ProcessManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Crie uma classe Tabela de Processos que instancia objetos da classe PCB para cada novo processo
 * Essa Tabela de Processo deve manter uma lista de processo “Prontos”
 * E um processo em “Execução”
 */

public class ProcessTable {
    static private List<ProcessControlBlock> processes = new ArrayList<ProcessControlBlock>();
    static private List<Integer> readyProcesses = new ArrayList<Integer>();
    static private ProcessControlBlock runningProcess;

    // Usado somente se o algoritmo de prioridade fixa for selecionado
    static private Map<Integer, List<Integer>> priorityQueues = new HashMap<Integer, List<Integer>>();
    
    /*
     * Adiciona processo na ProcessTable
     */
    public static ProcessControlBlock createProcess(int programCounter, int priority) {
        ProcessControlBlock process = new ProcessControlBlock(programCounter, priority);
        
        // Verificar se há algum processo na ProcessTable
        // if (processes.size() > 0) {
        //     // processes.get(processes.size() - 1).setLowerBound(process.getUpperBound());
        // }

        addProcessToQueue(process);
        return process;
    }

    /*
     * Recebe o PID do processo a ser removido da ProcessTable
     */
    public static void removeProcess(int PID) {
        ProcessControlBlock process = getProcess(PID);
        processes.remove(process);
    }

    // TODO: Mover parte dessa lógica para o scheduler
    /*
     * Remove o processo escolhido da lista de espera (ou da lista de bloquados);
     * Muda o estado processo em execução para "ready";
     * Move o processo em execução para o final da fila de espera;
     * Muda o estado do processo escolhido para "running";
     * Remove o processo escolhido da lista de espera
     * 
     * Return: void
     */
    public static void executeProcess(int PID) {
        if (readyProcesses.isEmpty()) {
            System.out.println("Não é possível executar programa com PID inexistente.");
            return;
        }

        stopProcess();
        ProcessControlBlock process = removeProcessFromQueue(PID);
        process.setProgramState("running");
        runningProcess = process;
    }
    
    public static void stopProcess() {
        ProcessControlBlock process = getRunningProcess();

        if (process == null) {
            System.out.println("Não é possível parar processo pois não há processo em execução.");
            return;
        }
        
        addProcessToQueue(process);
        runningProcess = null;
    }

    public static ProcessControlBlock getRunningProcess() {
        return runningProcess;
    }

    public static List<ProcessControlBlock> getProcessesQueue() {
        List<ProcessControlBlock> processList = new ArrayList<ProcessControlBlock>();
        for (Integer pid: readyProcesses) {
            processList.add(getProcess(pid));
        }
        return processList;
    }
   
    public static ProcessControlBlock getProcess(int PID) {
        for (ProcessControlBlock process: processes) {
            if (process.getPID() == PID) {
                return process;
            }
        }
        return null;
    }

    public static ProcessControlBlock getHigherPriorityProcess() {
        // Usado somente caso o algoritmo de escalonamento use as filas com prioridade
        // Retorna o processo com prioridade mais próximo a zero

        for (int priority = 0; priority < 10; priority++) {
            List<Integer> queue = priorityQueues.get(priority);
            if (queue != null && queue.size() > 0) {
                int pid = queue.get(0);
                return getProcess(pid);
            }
        }

        return null;
    }

    public static ProcessControlBlock removeProcessFromQueue(int PID) {
        for (int i = 0; i < readyProcesses.size(); i++) {
            if (readyProcesses.get(i) == PID) {
                ProcessControlBlock process = getProcess(PID);
                int priority = process.getPriority();
                
                // Remover da fila de processos prontos
                readyProcesses.remove(i);

                // Remover da fila de processos com prioridade
                List<Integer> queue = priorityQueues.get(priority);
                queue.remove((Integer) PID);

                return process;
            }
        }
        return null;
    }

    public static void addProcessToQueue(ProcessControlBlock process) {
        process.setProgramState("ready");
        
        if (!processes.contains(process)) {
            processes.add(process);
        }
        
        // Adicionar a fila de prontos
        readyProcesses.add(process.getPID());

        // Adicionar tambem a fila com prioridade
        int priority = process.getPriority();
        List<Integer> queue = priorityQueues.get(priority);

        if (queue == null) {
            queue = new ArrayList<Integer>();
            priorityQueues.put(priority, queue);
        }

        queue.add(process.getPID());
    }
}
