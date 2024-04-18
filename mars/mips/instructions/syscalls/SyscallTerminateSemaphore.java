package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
// import mars.mips.SO.ProcessManager.Semaphore;
// import mars.mips.hardware.RegisterFile;

/*
Recebe como parâmetro o endereço da variável associada a um semáforo, de modo a
eliminar a lista associada ao semáforo; [Que lista?]

Se o endereço da variável não for de um semáforo previamente criado retorna
um código de erro.
*/

public class SyscallTerminateSemaphore extends AbstractSyscall {
    public SyscallTerminateSemaphore() {
        super(64, "TerminateSemaphore");
    }

    public void simulate(ProgramStatement statement) {
        // int varAddress = RegisterFile.getValue(4);
        // int value;

        // TODO: Terminar implementação de SyscallTerminateSemaphore
    }
}
