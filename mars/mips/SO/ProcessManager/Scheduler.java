package mars.mips.SO.ProcessManager;

import java.awt.event.ActionEvent;
import java.util.List;

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
    public static void executeNextProcess() {
        List<ProcessControlBlock> processQueue = ProcessTable.getProcessesQueue();
        ProcessControlBlock readyProcess = processQueue.remove(0);
        
        if (readyProcess == null) {
            System.out.println("Não há processo na fila para ser executado.");
            return;
        }

        ProcessTable.executeProcess(readyProcess.getPID());
        

        // TODO: Corrigir isso (Outro bug)
        try {
            Simulator.getInstance().simulate(Globals.program, readyProcess.getRegisterPC(), 1, null, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Log processes status
        RegisterFile.setProgramCounter(readyProcess.getRegisterPC());
        // System.out.printf("Set process %d as running at address %d\n", readyProcess.getPID(), readyProcess.getRegisterPC());
        System.out.printf("Set process %d as running at address %d\n", readyProcess.getPID(), RegisterFile.getProgramCounter());
        readyProcess.loadContext();
        System.out.println("Process context loaded!");
        System.out.print("Process queue:");
        System.out.printf("[Running %d] ", ProcessTable.getRunningProcess().getPID());
        for (ProcessControlBlock process : ProcessTable.getProcessesQueue()) {
            System.out.printf("[Waiting %d] ", process.getPID());
        }
        System.out.println("\n");
    }
}


// public class Scheduler {
//     public static void executeNextProcess() {
        
//         List<ProcessControlBlock> processQueue = ProcessTable.getProcessesQueue();

//         ProcessControlBlock readyProcess = processQueue.remove(0);
//         ProcessControlBlock runningProcess = ProcessTable.getRunningProcess();
        
//         if (runningProcess != null) {
//             processQueue.add(runningProcess);
//         }

//         ProcessTable.executeProcess(readyProcess.getPID());

//         System.out.printf("Set process %d as running\n", readyProcess.getPID());
//         readyProcess.loadContext();
//         System.out.println("Process context loaded!");
        
//         System.out.print("Process queue:");
//         System.out.printf("[Running %d] ", ProcessTable.getRunningProcess().getPID());
//         for (ProcessControlBlock process : ProcessTable.getProcessesQueue()) {
//             System.out.printf("[Waiting %d] ", process.getPID());
//         }

//         System.out.println("\n");

//     }
// }