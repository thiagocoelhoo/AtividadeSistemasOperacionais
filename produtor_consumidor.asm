.data
N:      .word 100
count:  .word 0
item:   .word 0

.text
.globl producer
producer:
    # Prologue
    addi $sp, $sp, -8    # Adjust stack pointer
    sw $ra, 4($sp)       # Save return address
    sw $s0, 0($sp)       # Save $s0

    li $t0, 1            # Set t0 to 1 for the while loop

producer_loop:
    lw $s0, count        # Load count into $s0
    lw $t1, N            # Load N into $t1
    beq $s0, $t1, producer_sleep # if count == N, sleep
    # Placeholder for produce_item function
    # jal produce_item     # Call produce_item function
    li $v0, 42           # Placeholder value for item
    sw $v0, item         # Store item into item variable
    # Placeholder for insert_item function
    # jal insert_item      # Call insert_item function
    lw $s0, count        # Reload count into $s0
    addi $s0, $s0, 1     # Increment count
    sw $s0, count        # Store updated count
    bne $s0, 1, producer_continue # if count != 1, continue
    # Placeholder for wakeup_consumer function
    # jal wakeup_consumer  # Call wakeup function for consumer

producer_continue:
    j producer_loop      # Jump back to the beginning of the loop

producer_sleep:
    # Placeholder for sleep function
    j producer_loop      # Jump back to the beginning of the loop

.globl consumer
consumer:
    # Prologue
    addi $sp, $sp, -8    # Adjust stack pointer
    sw $ra, 4($sp)       # Save return address
    sw $s0, 0($sp)       # Save $s0

    li $t0, 1            # Set t0 to 1 for the while loop

consumer_loop:
    lw $s0, count        # Load count into $s0
    lw $t1, N            # Load N into $t1
    beq $s0, 0, consumer_sleep # if count == 0, sleep
    # Placeholder for remove_item function
    # jal remove_item      # Call remove_item function
    lw $v0, item         # Load item from item variable
    # Placeholder for consume_item function
    # jal consume_item     # Call consume_item function
    lw $s0, count        # Reload count into $s0
    addi $s0, $s0, -1    # Decrement count
    sw $s0, count        # Store updated count
    li $t2, 99           # Load N - 1 into $t2
    bne $s0, $t2, consumer_continue # if count != N - 1, continue
    # Placeholder for wakeup_producer function
    # jal wakeup_producer # Call wakeup function for producer

consumer_continue:
    j consumer_loop      # Jump back to the beginning of the loop

consumer_sleep:
    # Pl
