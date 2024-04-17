package mars.mips.SO.ProcessManager;

import java.util.List;
import java.util.Queue;

/*
 * Crie uma classe Escalonador onde poderão ser implementados diferentes algoritmos para escolha
 * dos processos que estão no estado de “pronto” na Tabela de Processos.
 * A priori o algoritmo de escalonamento será uma fila, ou seja, sempre que a função “escalonar”
 * for invocada o processo do início dessa fila é retirado. Novos processos “prontos” são incluídos
 * sempre no fim dessa fila.
 */
public class Scheduler {
    public static void executeNextProcess() {
        // TODO: implementar algoritmo de escalonamento
        
        List<ProcessControlBlock> processQueue = ProcessTable.getReadyProcesses();
        ProcessControlBlock readyProcess = processQueue.remove(0);
        ProcessControlBlock runningProcess = ProcessTable.getRunningProcess();
        
        if (runningProcess != null) {
            processQueue.add(runningProcess);
        }

        ProcessTable.setRunningProcess(readyProcess);
        readyProcess.loadContext();
    }
}
