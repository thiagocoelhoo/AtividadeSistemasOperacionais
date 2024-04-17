package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;

public class SyscallProcessChange extends AbstractSyscall{
    public SyscallProcessChange() {
        super(61, "ProcessChange");
    }

    public void simulate(ProgramStatement statement) {
        // TODO: Implementar syscall ProcessChange
        /* 
         * A segunda syscall deve ser chamada de SycallProcessChange com o objetivo de trocar
         * o processo que está executando na CPU 
         * 
         * Só possui um parâmetro: o número da sycall passado no registrador $v0
         * 
         * Um processo que invoque sua chamada de sistema abre mão da CPU voluntariamente
         * e um novo processo é colocado para executar
         * no seu lugar.
         * 
         * A chamada de sistema implementada deve salvar o contexto do processo paralisado
         * em seu respectivo PCB na tabela de processos (mudando seu estado de “Executando” 
         * para “Pronto”),
         * 
         * chamar o algoritmo de roteamento do Escalonador, que por sua vez deve escolher 
         * um outro processo que esteja no estado de “Pronto”, carregar o contexto do processo 
         * escolhido a partir do seu PCB (mudando seu estado para “Executando”) e retornar à execução
         * (agora do outro processo escolhido).
         */
        
        ProcessControlBlock process = ProcessTable.getRunningProcess();
        if (process != null) {
            process.saveContext();
            System.out.println("Saving process context");
        }
        Scheduler.executeNextProcess();
    }
}
