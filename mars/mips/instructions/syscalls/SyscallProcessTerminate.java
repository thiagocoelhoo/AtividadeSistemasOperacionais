package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;

public class SyscallProcessTerminate extends AbstractSyscall {
    public SyscallProcessTerminate() {
        super(62, "ProcessTerminate");
    }
    
    public void simulate(ProgramStatement statement) {
        /*
         * O único parâmetro passado para essa syscall é seu código no registrador $v0
         *
         * O processo que a invoca é finalizado, ou seja, tudo que estava alocado para ele
         * é desalocado e sua PCB é removida da tabela de processos
         *
         * Em seguida, o algoritmo de escalonamento deve ser invocado e um processo no estado
         * de pronto deve ser colocado para executar
         *
         * O algoritmo de escalonamento será uma simples fila, ou seja, cada novo processo criado
         * é colocado no final da fila. Todos os processos que estão nessa fila devem estar no
         * estado de “Pronto”. Quando o escalonador é chamado o algoritmo de escalonamento é executado, 
         * e neste caso, o primeiro processo da fila é removido para ser então ser colocado no estado
         * de “Executando”
         *
         * A classe que implementa o algoritmo de escalonamento deve estar preparada para implementar
         * outros algoritmos, ou seja, quando houver outros algoritmos uma simples mudança de parâmetro
         * deve invocar outro algoritmo sem a necessidade de mudar a implementação das outras classes.
        */

        // ProcessTable.setRunningProcess(null);
        ProcessTable.stopProcess();
        Scheduler.executeNextProcess();
    }
}
