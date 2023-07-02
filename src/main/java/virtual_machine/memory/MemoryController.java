package virtual_machine.memory;
import virtual_machine.utils.UnsignedShort;

public class MemoryController {
    private Memory mainMemory;
    private int codeSegment;
    private int dataSegment;
    private int stackSegment;

    private static final int standardCodeSegment = 0;
    private static final int standardDataSegment = 1001;
    private static final int standardStackSegment = 65_000;

    public short getInstruction(int addAddress) {
        return mainMemory.read((short) (addAddress + codeSegment));
    }

    public void writeInstruction(short value, int addAddress) {
        mainMemory.write(value, (short) (addAddress + codeSegment));
    }

    public short getData(int addAddress) {
        return mainMemory.read((short) (addAddress + dataSegment));
    }

    public void writeData(short value, int addAddress) {
        mainMemory.write(value, (short) (addAddress + dataSegment));
    }

    public void resetSegments() {
        codeSegment = standardCodeSegment;
        dataSegment = standardDataSegment;
        stackSegment = standardStackSegment;
    }
}
