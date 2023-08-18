package macroprocessor;

import assembler.Log;
import assembler.LogType;
import assembler.Logger;
import macroprocessor.macrotable.Macro;
import assembler.utils.AssemblerUtils;
import macroprocessor.utils.MacroUtils;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class MacroProcessor {
    // Macro processor
    private final HashMap<String, Macro> macroTable;
    private int currentMacroNestLevel = 0;

    // Lists of lines
    private final LinkedList<String> inputLines;
    private final LinkedList<String> outputLines;

    // Handling files utils
    private String currentLine;
    private int lineCounter;

    // Macros defines
    private static final String MACRODEF = "MACRODEF";
    private static final String MACROEND = "ENDM";
    private static final String MACROCALL = "CALLM";

    public MacroProcessor() {
        macroTable = new HashMap<>();
        inputLines = new LinkedList<>();
        outputLines = new LinkedList<>();
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
                        this.inputLines.add(s);
                    }
                }
            }

            // Close file
            fileReader.close();
            fileIO.close();

            // Read first line
            currentLine = inputLines.isEmpty() ? "" : inputLines.pop();

            // Start parsing in MODE 1 -> Copy mode
            while (!currentLine.isEmpty()) {
                // Decompose in tokens
                String[] tokens = AssemblerUtils.decomposeInTokens(currentLine);

                // Check if it's a macro declaration or macro call
                if (tokens.length > 1) {
                    if (tokens[1].equalsIgnoreCase(MACRODEF)) {
                        // Macro declaration
                        parseMacroDeclaration();
                    } else if (tokens[0].equalsIgnoreCase(MACROCALL)) {
                        // Macro call (expand)
                        parseMacroCall();
                    }
                } else {
                    // Copy line to output (no macro declaration or call)
                    outputLines.add(currentLine);
                }

                // Get next line
                currentLine = inputLines.isEmpty() ? "" : inputLines.pop();
            }
        } catch (IOException e) {
            Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "Error while reading " + pathToProgram + "file: " + e.getMessage()));
            resetMacroProcessor();
            throw e;
        } catch (Exception e) {
            resetMacroProcessor();
            throw e;
        }

        // Removes ".asm" extension and append ".pre"
        pathToProgram = pathToProgram.replace(".asm", ".pre");

        // Writing intermediate file (this will be sent to Assembler)
        OutputStream outputStream = new FileOutputStream(pathToProgram);
        try (DataOutputStream dataOutStream = new DataOutputStream(outputStream)) {
            for (String line : outputLines)
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

    // Macro declaration (MODE 2)
    private void parseMacroDeclaration() throws Exception {
        // Get all tokens in line
        String[] tokens = AssemblerUtils.decomposeInTokens(this.currentLine);

        // Increment nest level
        currentMacroNestLevel += 1;

        // Ai gustavo no cu nao - Ferrao
        // no cu sim! - Gustavo
        // Cala a boca e senta - Bessa
        // Semata - Nicolas

        // Get macro name
        String macroName = tokens[0];

        // Validate name if this macro was defined by user
        if (macroTable.containsKey(macroName)) {
            Macro m = macroTable.get(macroName);

            if (m.isNamedByUser() && !MacroUtils.isValidMacroName(macroName)) {
                Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "INVALID MACRO NAME: " + macroName));
                throw new IllegalArgumentException();
            }
        }

        // Get formal parameters
        ArrayList<String> formalParameters;
        if (tokens.length > 2) {
            formalParameters = new ArrayList<>(Arrays.stream(tokens, 2, tokens.length).toList());
        } else {
            formalParameters = null;
        }

        // Create linked list to store macro code
        LinkedList<String> macroCode = new LinkedList<>();
        HashMap<String, String> nestedMacroNames = new HashMap<>();

        // Process macro code
        while (true) {
            // Get next line and its tokens
            currentLine = inputLines.pop();
            tokens = AssemblerUtils.decomposeInTokens(currentLine);

            // Macro declaration (nested macro)
            if (tokens.length > 1 && tokens[1].equalsIgnoreCase(MACRODEF)) {
                // Will only add to macro table 2 levels of nesting
                if (currentMacroNestLevel == 1) {
                    // Rename nested macro
                    String newNestedMacroName = MacroUtils.renameNestedMacro(macroName, tokens[0]);

                    // Add to nested macro names, where OldName ->map-> NewName
                    nestedMacroNames.put(tokens[0], newNestedMacroName);
                    // Add to macro table with new name
                    macroTable.put(newNestedMacroName, new Macro(newNestedMacroName, false));

                    // Add nested macro declaration to macro code
                    currentLine = currentLine.replaceFirst(tokens[0], newNestedMacroName);
                }

                // Add macro declaration to outer macro
                macroCode.add(currentLine);
                // Increase nest level
                currentMacroNestLevel += 1;
            }
            // Check if it is a macro call (validate only to outer macro)
            else if (tokens.length > 1 && currentMacroNestLevel < 2 && tokens[0].equalsIgnoreCase(MACROCALL)) {
                // Get name of called macro
                String macroCalled = tokens[1];

                // Verify if it's a call for a nested macro
                if (nestedMacroNames.containsKey(macroCalled)) {
                    // Rename macro call
                    currentLine = currentLine.replaceFirst(macroCalled, nestedMacroNames.get(macroCalled));
                    macroCalled = nestedMacroNames.get(macroCalled);
                }

                // Check if called macro exists in macro table
                if (macroTable.containsKey(macroCalled)) {
                    macroCode.add(currentLine);
                }
                // Calling undeclared macro
                else {
                    Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, "MACRO NOT FOUND: " + macroCalled));
                    resetMacroProcessor();
                    throw new UndeclaredMacro(macroCalled);
                }
            }
            // No macro declaration or call
            else {
                macroCode.add(currentLine);
            }

            // Check if it is end of macro declaration
            if (tokens[0].equalsIgnoreCase(MACROEND)) {
                currentMacroNestLevel -= 1;

                if (currentMacroNestLevel == 0) {
                    break;
                }
            }
        }

        // Create macro
        Macro newMacro = new Macro(macroName, macroCode, formalParameters, true);
        macroTable.put(macroName, newMacro);
    }

    private void parseMacroCall() throws UndeclaredMacro {
        // Get all tokens in line
        String[] tokens = AssemblerUtils.decomposeInTokens(this.currentLine);

        // Get macro name
        String macroName = tokens[1];

        // Get real parameters
        ArrayList<String> realParameters;
        if (tokens.length > 2) {
            realParameters = new ArrayList<>(Arrays.stream(tokens, 2, tokens.length).toList());
        } else {
            realParameters = null;
        }

        // Process nested macros calls
        boolean isNestedMacro = false;
        if (macroName.contains(".")) {
            macroName = macroName.replace(".", "#");
            isNestedMacro = true;
        }

        // Check if macro exists
        if (macroTable.containsKey(macroName)) {
            var macroToExpand = macroTable.get(macroName);

            // Expand macro with real parameters
            var expandedMacro = macroToExpand.processRealParameters(realParameters);

            // Add expanded code in input lines to be processed when back to MODE 1
            while (!expandedMacro.isEmpty()) {
                inputLines.addFirst(expandedMacro.removeLast());
            }
        }
        // Macro does not exist
        else {
            String message;
            if (isNestedMacro)
                message = "NESTED MACRO WAS NOT CALLED: ";
            else
                message = "MACRO NOT FOUND: ";

            Logger.getInstance().addLog(new Log(LogType.ERROR, lineCounter, message + macroName));
            resetMacroProcessor();
            throw new UndeclaredMacro(macroName);
        }
    }

    private void resetMacroProcessor() {
        this.macroTable.clear();
        this.inputLines.clear();
        this.outputLines.clear();
        this.currentLine = "";
        this.lineCounter = 0;
        this.currentMacroNestLevel = 0;
    }
}