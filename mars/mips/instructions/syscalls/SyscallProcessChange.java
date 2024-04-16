package mars.mips.instructions.syscalls;

import mars.ProgramStatement;

public class SyscallProcessChange extends AbstractSyscall{
    public SyscallProcessChange() {
        super(21, "ProcessChange");
    }

    public void simulate(ProgramStatement ps) {
    }
}
