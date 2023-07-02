package virtual_machine.commands.operations.arithmetical;

import virtual_machine.commands.operations.Command;
import virtual_machine.registers.RegWork;
import virtual_machine.types.short;

import java.util.List;

public class AddAxDx implements Command {
    private void add( RegWork ax, RegWork dx ) {
        ax.setReg( new short( ax.getReg() + dx.getReg() ) );
    }

    @Override
    public void doOperation( List<Object> args ) {
        RegWork ax = (RegWork)args.get( 0 );
        RegWork dx = (RegWork)args.get( 1 );
        this.add( ax, dx );
    }
}
