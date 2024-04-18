package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.Semaphore;
import mars.mips.hardware.Memory;
import mars.mips.hardware.RegisterFile;

/*
Recebe como parâmetro o endereço de uma variável inteira, 

Assumirá o valor atual dessa variável como o valor inicial do semáforo. 

A chamada de sistema cria, internamente, uma lista na qual os processos
que acessarem esse semáforo poderão ficar bloqueados.
*/

public class SyscallCreateSemaphore extends AbstractSyscall {
    public SyscallCreateSemaphore() {
        super(63, "CreateSemaphore");
    }

    public void simulate(ProgramStatement statement) {
        int varAddress = RegisterFile.getValue(4);
        int value;
        
        try {
            value = Memory.getInstance().get(varAddress, 4);
            new Semaphore(value, varAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
