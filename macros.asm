.macro SyscallFork (%process)
	la $a0, %process
	li $v0, 60
	syscall
.end_macro 

.macro SyscallProcessChange
	li $v0, 61
	syscall
.end_macro 

.macro SyscallProcessTerminate
	li $v0, 62
	syscall
.end_macro 