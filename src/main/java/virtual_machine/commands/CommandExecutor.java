package virtual_machine.commands;


import virtual_machine.commands.operations.Command;
import virtual_machine.commands.operations.arithmetical.AddAxDx;

import java.util.HashMap;

public class CommandExecutor {

    private Command command;

    private final HashMap<Byte, Command> opCodeMap;

    public CommandExecutor() {
        this.opCodeMap = new HashMap<>();
        this.opCodeMap.put( (byte) 0x03, new AddAxDx() );
    }

    private void setOperation( byte opCode ) {
        this.command = this.opCodeMap.get( opCode );
    }

    public void doOperation( byte opCode, HashMap<String, Object> args ) {
        this.setOperation( opCode );
        this.command.doOperation( args );
    }

}
