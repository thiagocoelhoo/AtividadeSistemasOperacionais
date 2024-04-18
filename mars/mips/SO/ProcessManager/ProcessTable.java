package mars.mips.SO.ProcessManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * Crie uma classe Tabela de Processos que instancia objetos da classe PCB para cada novo processo
 * Essa Tabela de Processo deve manter uma lista de processo “Prontos”
 * E um processo em “Execução”
 */

public class ProcessTable {
    static private List<ProcessControlBlock> processes = new ArrayList<ProcessControlBlock>();
    static private List<Integer> readyProcesses = new ArrayList<Integer>();
    static private List<Integer> waitingProcesses = new ArrayList<Integer>();
    static private ProcessControlBlock runningProcess;

    /*
     * Adiciona processo na ProcessTable
     */
    public static ProcessControlBlock createProcess(int programCounter) {
        ProcessControlBlock process = new ProcessControlBlock(programCounter);
        
        // Verificar se há algum processo na ProcessTable
        if (processes.size() > 0) {
            processes.get(processes.size() - 1).setLowerBound(process.getUpperBound());
        }

        processes.add(process);
        readyProcesses.add(process.getPID());
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
        ProcessControlBlock process = getProcess(PID);

        // TODO: Criar lógica para definir estado do processo durante criação
        removeProcessFromQueue(PID);
        removeProcessFromBlockedQueue(PID);
        
        stopProcess();
        process.setProgramState("running");
        runningProcess = process;
    }
    
    public static void stopProcess() {
        ProcessControlBlock process = getRunningProcess();

        if (process == null) {
            System.out.println("Não é possível parar processo pois não há processo em execução.");
            return;
        }        
        
        process.setProgramState("ready");
        readyProcesses.add(process.getPID());
        runningProcess = null;
    }

    public static void blockProcess() {
        if (runningProcess == null) {
            System.out.println("Não é possível bloquear processo pois não há processo em execução");
           return;
        }
        runningProcess.setProgramState("waiting");
        waitingProcesses.add(runningProcess.getPID());

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

    public static List<ProcessControlBlock> getBlockedProcessesQueue() {
        List<ProcessControlBlock> processList = new ArrayList<ProcessControlBlock>();
        for (Integer pid: waitingProcesses) {
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

    public static ProcessControlBlock removeProcessFromQueue(int PID) {
        for (int i = 0; i < readyProcesses.size(); i++) {
            if (readyProcesses.get(i) == PID) {
                readyProcesses.remove(i);
                return getProcess(PID);
            }
        }
        return null;
    }

    public static ProcessControlBlock removeProcessFromBlockedQueue(int PID) {
        // else if (process.getProgramState() != null && process.getProgramState().equals("waiting")) {
        for (int i = 0; i < waitingProcesses.size(); i++) {
            if (waitingProcesses.get(i) == PID) {
                waitingProcesses.remove(i);
                return getProcess(PID);
            }
        }
        return null;
    }

    public static void runBlockedProcess() {
        int PID = waitingProcesses.get(waitingProcesses.size() - 1);
        ProcessControlBlock process = getProcess(PID);
        if (process != null) {
            runningProcess.setProgramState("ready");
            
            runningProcess = process;
            runningProcess.setProgramState("running");
        }
    }
}
