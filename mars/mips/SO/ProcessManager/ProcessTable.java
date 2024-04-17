package mars.mips.SO.ProcessManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/*
 * Crie uma classe Tabela de Processos que instancia objetos da classe PCB para cada novo processo
 * Essa Tabela de Processo deve manter uma lista de processo “Prontos”
 * E um processo em “Execução”
 */
public class ProcessTable {
    static private List<ProcessControlBlock> readyProcesses = new LinkedList<ProcessControlBlock>();
    static private ProcessControlBlock runningProcess;
    
    public static ProcessControlBlock getRunningProcess() {
        return runningProcess;
    }

    public static void setRunningProcess(ProcessControlBlock process) {
        runningProcess = process;
    }

    public static List<ProcessControlBlock> getReadyProcesses() {
        return readyProcesses;
    }

    /*
     * Adiciona processo na ProcessTable
     */
    public static ProcessControlBlock createProcess(int programCounter) {
        ProcessControlBlock process = new ProcessControlBlock(programCounter);
        readyProcesses.add(process);
        return process;
    }

    /*
     * Recebe o PID do processo a ser removido da ProcessTable
     */
    public static void removeProcess(int PID) {
    }

    public static void executeProcess(int PID) {
    }
}
