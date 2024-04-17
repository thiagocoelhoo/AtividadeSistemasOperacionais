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
        int regNumber = RegisterFile.getNumber("$a0");
        int pc = RegisterFile.getValue(regNumber);
        ProcessControlBlock process = ProcessTable.createProcess(pc);
        System.out.printf("Syscall fork: $a0 = %d; PID = %d\n", pc, process.getPID());
    }
}
