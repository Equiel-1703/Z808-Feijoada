package virtual_machine.commands.operations.flow;

import virtual_machine.commands.operations.Command;
import virtual_machine.interpreter.OpParameters;
import virtual_machine.memory.MemoryController;
import virtual_machine.registers.BankOfRegisters;
import virtual_machine.registers.RegWork;

import java.util.HashMap;

// nome de escolha do miguel
public class Jmp implements Command {
    @Override
    public void doOperation(HashMap<OpParameters, Object> args) {
        RegWork ip = (RegWork) ((BankOfRegisters) args.get(OpParameters.REGISTERS)).getIp();
        MemoryController mc = (MemoryController) args.get(OpParameters.MEM_CONTROLLER);

        // We must get from the memory the 16 bit constant
        ip.setValue((short) (ip.getValue() + 1)); // Increment IP

        short jmpAddr = mc.getWordBE(ip.getValue()); // Get operand addr in dataMem

        ip.setValue((short) (ip.getValue() + jmpAddr));
    }
}
