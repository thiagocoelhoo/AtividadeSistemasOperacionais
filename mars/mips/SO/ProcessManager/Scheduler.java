package mars.mips.SO.ProcessManager;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;

import mars.Globals;
import mars.mips.hardware.RegisterFile;
import mars.simulator.Simulator;

/*
 * Crie uma classe Escalonador onde poderão ser implementados diferentes algoritmos para escolha
 * dos processos que estão no estado de “pronto” na Tabela de Processos.
 * A priori o algoritmo de escalonamento será uma fila, ou seja, sempre que a função “escalonar”
 * for invocada o processo do início dessa fila é retirado. Novos processos “prontos” são incluídos
 * sempre no fim dessa fila.
 */


// BUG: 
//      Situação: Há um bug quando o PC é mudado enquanto o simulator está em uma instrução do tipo branch ou jump.
// 
//      Descrição: O PC é mudado pelo programa, mas depois é sobrescrito pela instrução jump/branch
//
//      Comportamento desejado: O PC deve ser alterado e então a execução do processo deve ser redirecionado diretamente
//                              para o endereço definido no programCounter (PC)

public class Scheduler {
    public enum ALGORITHM {
        FIFO,
        LOTTERY,
        FIXED_PRIORITY
    };
    
    private static Random rand = new Random();
    private static ALGORITHM algorithm = ALGORITHM.FIFO;
    
    public static void schedule() {
        if (algorithm == ALGORITHM.FIFO) {
            fifoSchedule();
            return;
        } else if (algorithm == ALGORITHM.LOTTERY) {
            lotterySchedule();
            return;
        } else if (algorithm == ALGORITHM.FIXED_PRIORITY) {
            fixedPrioritySchedule();
            return;
        }
    }

    public static void setAlgorithm(ALGORITHM algorithm) {
        Scheduler.algorithm = algorithm;
    }

    private static void fifoSchedule() {
        List<ProcessControlBlock> processQueue = ProcessTable.getProcessesQueue();
        ProcessControlBlock selectedProcess = processQueue.get(0);
        
        if (selectedProcess == null) {
            System.out.println("Não há processo na fila para ser executado.");
            return;
        }

        executeProcess(selectedProcess);
    }

    private static void lotterySchedule() {
        List<ProcessControlBlock> processQueue = ProcessTable.getProcessesQueue();
        ProcessControlBlock selectedProcess = processQueue.get(rand.nextInt(processQueue.size()));
        
        if (selectedProcess == null) {
            System.out.println("Não há processo na fila para ser executado.");
            return;
        }
        
        executeProcess(selectedProcess);
    }

    private static void fixedPrioritySchedule() {
        ProcessControlBlock selectedProcess = ProcessTable.getHigherPriorityProcess();
        
        if (selectedProcess == null) {
            System.out.println("Não há processo na fila para ser executado.");
            return;
        }
        
        executeProcess(selectedProcess);
    }

    private static void executeProcess(ProcessControlBlock process) {
        ProcessTable.executeProcess(process.getPID());
        
        // TODO: Corrigir isso (Outro bug)
        try {
            Simulator.getInstance().simulate(Globals.program, process.getRegisterPC(), 1, null, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        RegisterFile.setProgramCounter(process.getRegisterPC());
        process.loadContext();

        // Log processes status
        System.out.printf("Running process %d at address %d\n", process.getPID(), RegisterFile.getProgramCounter());
        System.out.println("Process context loaded!");
        System.out.print("Process queue:");
        
        // Imprimir lista de processos prontos e em execução
        System.out.printf("[Running %d] ", ProcessTable.getRunningProcess().getPID());
        for (ProcessControlBlock p : ProcessTable.getProcessesQueue()) {
            System.out.printf("[Waiting %d] ", p.getPID());
        }
        System.out.println("\n");
    }
}