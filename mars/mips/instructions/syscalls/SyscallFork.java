package mars.mips.instructions.syscalls;

import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;

public class SyscallFork extends AbstractSyscall {
    private static ProcessTable processTable = new ProcessTable();
    
    public SyscallFork() {
        super(20, "Fork");
    }

    public void simulate(ProgramStatement statement) {
        ProcessControlBlock process = new ProcessControlBlock();
        int pc = mars.mips.hardware.RegisterFile.getNumber("a0");
        
        process.setRegisterPC(pc);
        processTable.addProcess(process);
    }
}
