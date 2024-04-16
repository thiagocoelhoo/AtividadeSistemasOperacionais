package mars.mips.SO.ProcessManager;

import mars.mips.hardware.Memory;
import mars.mips.hardware.Register;

public class ProcessControlBlock {

    public static final int GLOBAL_POINTER_REGISTER = 28;
    public static final int STACK_POINTER_REGISTER = 29;
    
    private Register [] regFile = {
    	new Register("$zero", 0, 0),
    	new Register("$at", 1, 0),
       	new Register("$v0", 2, 0),
       	new Register("$v1", 3, 0),
       	new Register("$a0", 4, 0),
       	new Register("$a1", 5, 0),
       	new Register("$a2", 6, 0),
       	new Register("$a3", 7, 0),
       	new Register("$t0", 8, 0),
       	new Register("$t1", 9, 0),
       	new Register("$t2", 10, 0),
		new Register("$t3", 11, 0), 
       	new Register("$t4", 12, 0),
		new Register("$t5", 13, 0),
       	new Register("$t6", 14, 0),
		new Register("$t7", 15, 0),
       	new Register("$s0", 16, 0),
		new Register("$s1", 17, 0),
       	new Register("$s2", 18, 0),
		new Register("$s3", 19, 0),
       	new Register("$s4", 20, 0),
		new Register("$s5", 21, 0),
       	new Register("$s6", 22, 0),
		new Register("$s7", 23, 0),
       	new Register("$t8", 24, 0),
		new Register("$t9", 25, 0),
       	new Register("$k0", 26, 0),
		new Register("$k1", 27, 0),
       	new Register("$gp", GLOBAL_POINTER_REGISTER, Memory.globalPointer),
       	new Register("$sp", STACK_POINTER_REGISTER, Memory.stackPointer),
       	new Register("$fp", 30, 0),
       	new Register("$ra", 31, 0)
    };
       												  
    private Register programCounter= new Register("pc", 32, Memory.textBaseAddress); 
    private Register hi= new Register("hi", 33, 0);//this is an internal register with arbitrary number
    private Register lo= new Register("lo", 34, 0);// this is an internal register with arbitrary number
    
    private int PID;
    private int programStartAddress;
    private String programState;
    
    // Getters
    
    public int getRegister(int index) {
    	return regFile[index].getValue();
    }
    
    public int getRegisterPC() {
    	return programCounter.getValue();
    }
    
    public int getRegisterHi() {
    	return hi.getValue();
    }	
    
    public int getRegisterLo() {
    	return lo.getValue();
    }
    
    public int getPID() {
    	return PID;
    }
    
    public int getStartAddress() {
    	return programStartAddress;
    }
    
    public String getProgramState() {
    	return programState;
    }
    
    // Setters
    
    public void setRegister(int index, int value) {
    	if (index == 0) return;
    	
    	regFile[index].setValue(value);
    }
    
    public void setRegisterPC(int value) {
    	programCounter.setValue(value);
    }
    
    public void setRegisterHi(int value) {
    	hi.setValue(value);
    }
    
    public void setRegisterLo(int value) {
    	lo.setValue(value);
    }
    
    public void setPID(int value) {
    }
    
    public void setStartAddress(int value) {
    }
    
    public void setProgramState(String value) {
    }
}

