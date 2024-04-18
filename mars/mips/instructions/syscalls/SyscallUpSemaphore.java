package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
// import mars.mips.SO.ProcessManager.Scheduler;
import mars.mips.SO.ProcessManager.Semaphore;
import mars.mips.hardware.RegisterFile;

/*
Recebe como parâmetro o endereço da variável associada a um semáforo; 

Se existir pelo menos um processo bloqueado então retira um dos processos da
fila de bloqueados do semáforo e o transfere para fila de prontos senão incrementa
o valor do semáforo.
*/

public class SyscallUpSemaphore extends AbstractSyscall {
    public SyscallUpSemaphore() {
        super(66, "UpSemaphore");
    }

    public void simulate(ProgramStatement statement) {
        int varAddress = RegisterFile.getValue(4);
        int value;

        Semaphore s = Semaphore.getSemaphore(varAddress);
        
        if (s == null) {
            System.out.println("Error: Invalid semaphore.");
            return;
        }

        if (ProcessTable.getBlockedProcessesQueue().size() > 0) {
            // Scheduler.executeNextProcess();
        }
        
        value = s.getValue() + 1;
        s.setValue(value);

    }
}
