package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;

public class SyscallFork extends AbstractSyscall {
    public SyscallFork() {
        super(60, "Fork");
    }

    /*
     * Cria um novo processo na ProcessTable
     */
    public void simulate(ProgramStatement statement) {
        ProcessControlBlock process = new ProcessControlBlock();
        int pc = statement.getAddress();
        process.setRegisterPC(pc);
        ProcessTable.addProcess(process);
    }
}
