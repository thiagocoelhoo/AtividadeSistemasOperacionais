package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.hardware.RegisterFile;

public class SyscallFork extends AbstractSyscall {
    public SyscallFork() {
        super(60, "Fork");
    }

    /*
     * Cria um novo processo na ProcessTable
     */
    public void simulate(ProgramStatement statement) {
        // int pc = statement.getAddress();
        int pc = RegisterFile.getValue(4);
        int priority = RegisterFile.getValue(5);

        ProcessControlBlock process = ProcessTable.createProcess(pc, priority);

        System.out.printf("Syscall fork: $a0 = %d; PID = %d\n", pc, process.getPID());
    }
}
