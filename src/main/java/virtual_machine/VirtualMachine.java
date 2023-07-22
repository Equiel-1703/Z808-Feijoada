package virtual_machine;

import virtual_machine.interpreter.Interpreter;
import virtual_machine.loader.Loader;
import virtual_machine.observerpattern.RegObsListener;
import virtual_machine.registers.Registers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class VirtualMachine {
    private final Loader vmLoader = new Loader();
    private final Interpreter vmInterpreter = new Interpreter();
    private static LinkedList<RegObsListener> subscribers = new LinkedList<>();

    public void loadProgram(String path) throws IOException {
        vmLoader.setProgramToLoad(path);
        // Loads program to main memory
        vmLoader.loadToMemory(vmInterpreter.getMemoryController(), vmInterpreter.getRegisters().getCs().getValue(), vmInterpreter.getRegisters().getDs().getValue());
    }

    public void executeProgram() {
        vmInterpreter.executeProgram();
        notifySubscribers();
    }

    public void executeNextInstruction() {
        vmInterpreter.executeNextInstruction();
        notifySubscribers();
    }

    public static void subscribe(RegObsListener rl) {
        subscribers.add(rl);
    }

    private void notifySubscribers() {
        HashMap<Registers, Short> workRegValues = vmInterpreter.getRegisters().getWorkRegValues();
        String regFlagValue = vmInterpreter.getRegisters().getRegFlags();

        for(RegObsListener rl : subscribers) {
            rl.updatedRegs(workRegValues, regFlagValue);
        }
    }
}