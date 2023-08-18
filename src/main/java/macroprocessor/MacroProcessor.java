package macroprocessor;

import assembler.Log;
import assembler.LogType;
import assembler.Logger;
import macroprocessor.macrotable.Macro;
import assembler.utils.AssemblerUtils;

import java.io.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class MacroProcessor {
    // Macro processor
    private final HashMap<String, Macro> macroTable;
    private boolean isInsideMacroDefinition = false;
    private int currentMacroAlignmentLevel = 0;
    private boolean hasNestedMacros = false;

    // New file output lines for assembler
    private final LinkedList<String> step1Lines;
    private final LinkedList<String> step2Lines;

    // Handling files utils
    private String currentLine;
    private int lineCounter;

    // Macros defines
    private static final String MACRODEF = "MACRODEF";
    private static final String MACROEND = "ENDM";

    public MacroProcessor() {
        macroTable = new HashMap<>();
        step1Lines = new LinkedList<>();
        step2Lines = new LinkedList<>();
    }

    public String parseMacros(String pathToProgram) throws Exception {
        lineCounter = 0;
        Logger.getInstance().reset();

        // Read entire file to linked list ignoring comments and blank lines
        try {
            FileReader fileReader = new FileReader(pathToProgram);
            BufferedReader fileIO = new BufferedReader(fileReader);

            for (String s : fileIO.lines().toList()) {
                s = s.trim();
                if (!s.isEmpty()) {
                    if (s.trim().charAt(0) != ';') {
                        this.step1Lines.add(s);
                    }
                }
            }

            fileReader.close();
            fileIO.close();

            // Executes passes
            do {
                // Step 1: macro declaration processing
                while (lineCounter < step1Lines.size()) {
                    // Reads first line from list
                    currentLine = step1Lines.pop();

                    // Check for macros
                    parseMacroDeclaration();

                    // Next Line
                    lineCounter += 1;
                }

                lineCounter = 0;
                step1Lines.clear();

                // Step 2: macro expansion
                while (lineCounter < step2Lines.size()) {
                    // Reads first line from list
                    currentLine = step2Lines.pop();

                    // Check for macros
                    parseMacroExpansion();

                    // Next Line
                    lineCounter += 1;
                }

            } while (hasNestedMacros);

            // If no nested macros were found, then the macro expansion is done and the
            // final extended code is in step1Lines

        } catch (IOException e) {
            Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "Error while reading " + pathToProgram + "file: " + e.getMessage()));
            Logger.getInstance().printLogs();
            resetMacroProcessor();
        }

        // Removes ".asm" extension and append ".pre"
        pathToProgram = pathToProgram.replace(".asm", ".pre");

        // Writing intermediate file (this will be sent to Assembler)
        OutputStream outputStream = new FileOutputStream(pathToProgram);
        try (DataOutputStream dataOutStream = new DataOutputStream(outputStream)) {
            for (String line : step1Lines)
                dataOutStream.writeBytes(line + "\n");
        } catch (IOException e) {
            Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "Error while writing " + pathToProgram + "file: " + e.getMessage()));
            Logger.getInstance().printLogs();
            resetMacroProcessor();
        }

        // Log success message
        Logger.getInstance().addLog(new Log(LogType.INFO, 0, "Macros expanded!"));
        Logger.getInstance().printLogs();

        return pathToProgram;
    }

    public void parseMacroDeclaration() throws Exception {
        // All tokens in line
        String[] tokens = AssemblerUtils.decomposeInTokens(this.currentLine);

        // is external macro declaration?
        if (tokens[1].equalsIgnoreCase(MACRODEF) && !isInsideMacroDefinition) {


        }

        // Macro name is valid?
        if (!AssemblerUtils.isValidMacro(this.currentLine)) {
            throw new Exception("Invalid macro name");
        }

        // Macro name
        String macroName = tokens[0];

        if (isInsideMacroDefinition) {

        }

        // If macro does not exist
        if (!macroTable.macroExists(macroName)) {
            // Macro parameters
            String[] macroParams;
            if (tokens.length > 2) {
                macroParams = Arrays.copyOfRange(tokens, 2, tokens.length);
            } else {
                macroParams = new String[0];
            }

            // Ai gustavo no cu nao - Ferrao
            // no cu sim! - Gustavo
            // Cala a boca e senta - Bessa
            // Semata - Nicolas
            // Get macro code

            int startMacroDef = lineCounter;
            StringBuilder macroCode = new StringBuilder();
            while (!step2Lines[lineCounter].equals(MACROEND)) {
                this.currentLine = step2Lines[lineCounter];

                // Check if it's a nested macro definition
                if (this.currentLine.equalsIgnoreCase(MACRODEF)) {
                    this.macroAlignmentLevel += 1;
                    try {
                        parseMacroDeclaration();
                    } catch (StackOverflowError e) {
                        Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "Maximum nested macro level reached!"));
                    }
                }
                macroCode.append(step2Lines[lineCounter]).append("\n");
                lineCounter += 1;
            }
            int endMacroDef = lineCounter;
            

            // Macro being added to the macro table
            Macro macro = new Macro(macroName, macroCode.toString(), macroParams, );

        } else {
            throw new Exception("Macro already exists");
        }

        return true;
    }

    private void resetMacroProcessor() {
        // Macro processor
        this.macroTable.reset();
        this.isInsideMacroDefinition = false;
        // New file output lines for assembler
        this.step1Lines.clear();
        // Handling files utils
        this.currentLine = "";
        this.step2Lines.clear();
        // Gay é o henrique
        this.lineCounter = 0;
    }
}