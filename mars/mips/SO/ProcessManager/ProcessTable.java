package mars.mips.SO.ProcessManager;

import java.util.ArrayList;
import java.util.List;

/*
 * Crie uma classe Tabela de Processos que instancia objetos da classe PCB para cada novo processo
 * Essa Tabela de Processo deve manter uma lista de processo “Prontos”
 * E um processo em “Execução”
 */
public class ProcessTable {
    static private List<ProcessControlBlock> ready = new ArrayList<ProcessControlBlock>();
    static private ProcessControlBlock running;
    
    /*
     * Adiciona processo na ProcessTable e retorna o PID do processo
     */
    public static int addProcess(ProcessControlBlock process) {
        ready.add(process);
        return -1;
    }

    /*
     * Recebe o PID do processo a ser removido da ProcessTable
     */
    public static void removeProcess(int PID) {
    }
}
