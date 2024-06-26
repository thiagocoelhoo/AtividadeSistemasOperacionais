.include "macros.asm"

.data
.text
	SyscallFork(Programa1, 2)
	SyscallFork(Programa2, 1)
	SyscallFork(Idle, 3)
	# SyscallProcessChange
Idle:
	loop:	
		# SyscallProcessChange
		NOP
		j loop

Programa1:
	addi $s1, $zero, 1 # valor inicial do contador
	addi $s2, $zero, 10 # valor limite do contador
	loop1:	
		addi $s1, $s1, 1
		beq $s1, $s2, fim1
		# SyscallProcessChange
		j loop1
	fim1:	SyscallProcessTerminate

Programa2:
	addi $s1, $zero, -1 # valor inicial do contador
	addi $s2, $zero, -10 # valor limite do contador
	loop2:	
		addi $s1, $s1, -1
		beq $s1, $s2, fim2
		# SyscallProcessChange
		j loop2
	fim2: SyscallProcessTerminate

