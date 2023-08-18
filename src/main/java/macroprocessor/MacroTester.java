package macroprocessor;

import assembler.Logger;

public class MacroTester {
    public static void main(String[] args) {
        MacroProcessor macroProcessor = new MacroProcessor();
        try {
            macroProcessor.parseMacros("src\\programs\\macrotest.asm");
        } catch (Exception e) {
            System.out.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
        }
    }
}
