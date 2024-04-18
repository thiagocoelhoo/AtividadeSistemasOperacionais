package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Semaphore;
import mars.mips.hardware.RegisterFile;

/*
Recebe como parâmetro o endereço da variável associada a um semáforo de modo a
acessar as estruturas associadas ao mesmo;

Se o valor atual do semáforo for maior que zero então decrementa este valor;

Caso o valor do semáforo for igual a zero o processo que chamou é colocado na
fila de bloqueado. [Que fila é essa? Fila dos ready?]
*/

public class SyscallDownSemaphore extends AbstractSyscall {
    public SyscallDownSemaphore() {
        super(65, "DownSemaphore");
    }

    public void simulate(ProgramStatement statement) {
        int varAddress = RegisterFile.getValue(4);
        int value;
        Semaphore s = Semaphore.getSemaphore(varAddress);
        
        if (s == null) {
            System.out.println("Error: Invalid semaphore.");
            return;
        }
        
        value = s.getValue();
        
        if (value > 0) {
            value -= 1;
            s.setValue(value);
        } else {
            ProcessTable.blockProcess();
        }
    }
}
