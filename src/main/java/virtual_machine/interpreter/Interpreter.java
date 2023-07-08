package virtual_machine.interpreter;

import virtual_machine.commands.CommandExecutor;
import virtual_machine.memory.Memory;
import virtual_machine.os_interruptions.OSInterruptionHandler;
import virtual_machine.memory.MemoryController;
import virtual_machine.registers.BankOfRegisters;
import virtual_machine.registers.RegFlags;
import virtual_machine.registers.RegWork;
import virtual_machine.utils.BinaryUtils;

import java.util.HashMap;

public class Interpreter {
    private final BankOfRegisters registers = new BankOfRegisters();

    private final MemoryController memoryController = new MemoryController();
    private final CommandExecutor commandExecutor = new CommandExecutor();
    private final HashMap<Short, Object> interruptionVector = new HashMap<>();
    private final HashMap<OpParameters, Object> operationParameters = new HashMap<>();

    public Interpreter() {
        registers.getCs().setValue((short) 0);
        registers.getDs().setValue((short) (30000 + 1));
        registers.getSs().setValue((short) (Memory.MEM_SIZE - 1));

        initializeInterruptionVector();
        initializeOperationParameters();
    }

    public Interpreter(int codeSegLength) {
        registers.getCs().setValue((short) 0);
        registers.getDs().setValue((short) (codeSegLength + 1));
        registers.getSs().setValue((short) (Memory.MEM_SIZE - 1));

        initializeInterruptionVector();
        initializeOperationParameters();
    }

    private void initializeInterruptionVector() {
        interruptionVector.put((short) 0x0021, new OSInterruptionHandler()); // OS Interruption (duh)
    }

    private void initializeOperationParameters() {
        operationParameters.put(OpParameters.REGISTERS, registers);
        operationParameters.put(OpParameters.MEM_CONTROLLER, memoryController); // Memory (duh)
        operationParameters.put(OpParameters.INT_VECTOR, interruptionVector); // Interruption vector (duh)
    }

    public void executeProgram() {
        while (registers.getIp().getValue() < registers.getSs().getValue()) {
            executeNextInstruction();
        }
    }

    public void executeNextInstruction() {
        // Get the instruction that is ponteinted by IP
        short instruction = memoryController.getWordBE(registers.getIp().getValue());
        // Execute the instruction
        commandExecutor.doOperation(instruction, operationParameters);
        // Update the register IP
        registers.incrementIp();
    }

    public BankOfRegisters getRegisters() {
        return registers;
    }

    public MemoryController getMemoryController() {
        return memoryController;
    }
}
