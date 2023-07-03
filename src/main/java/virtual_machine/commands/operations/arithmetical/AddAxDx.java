package virtual_machine.commands.operations.arithmetical;

import virtual_machine.commands.operations.Command;
import virtual_machine.commands.operations.OperationsUtils;
import virtual_machine.interpreter.OpParameters;
import virtual_machine.registers.RegFlags;
import virtual_machine.registers.RegWork;

import java.util.HashMap;

public class AddAxDx implements Command {
    private void add( RegWork ax, RegWork dx ) {
        ax.setReg( (short)(ax.getReg() + dx.getReg()) );
    }

    // Campos do registrador SR afetados pela soma: CF, PF, ZF, SF, OF
    // CF: carry ou borrow
    // PF: flag de paridade
    // ZF: flag de zero
    // SF: flag de sinal
    // OF: flag de overflow
    //  0001
    // +0001
    // =0010

    @Override
    public void doOperation(HashMap<OpParameters, Object> args ) {
        RegWork ax = (RegWork) args.get(OpParameters.AX);
        RegWork dx = (RegWork) args.get(OpParameters.DX);
        RegFlags sr = (RegFlags) args.get(OpParameters.SR_FLAGS);

        int result = ax.getReg() + dx.getReg();
        sr.setOf(OperationsUtils.hasOverflow16(result));
        sr.setCf(OperationsUtils.hasCarry(ax.getReg(), dx.getReg()));
        sr.setPf(OperationsUtils.parityBit(result));
        sr.setZf(OperationsUtils.isZero(result));
        sr.setSf(OperationsUtils.hasSignal(result));

        ax.setReg((short) result);
    }
}