package macroprocessor.macrotable;

import java.util.ArrayList;
import java.util.LinkedList;

public class Macro {
    private final String macroName;
    private final LinkedList<String> macroCode;
    private final ArrayList<String> formalParameters;
    private String[] realParameters;
    private final boolean isNamedByUser;

    public Macro(String macroName, LinkedList<String> macroCode, ArrayList<String> formalParameters, boolean isNamedByUser) {
        this.macroName = macroName;

        if (formalParameters != null)
            this.formalParameters = (ArrayList<String>) formalParameters.clone();
        else
            this.formalParameters = null;

        this.macroCode = (LinkedList<String>) macroCode.clone();
        this.isNamedByUser = isNamedByUser;
    }

    public Macro(String macroName, boolean isNamedByUser) {
        this.macroName = macroName;
        this.isNamedByUser = isNamedByUser;
        this.formalParameters = null;
        this.macroCode = null;
    }

    public String getIdentification() {
        return macroName;
    }

    public LinkedList<String> getMacroCode() {
        return macroCode;
    }

    public ArrayList<String> getFormalParameters() {
        return formalParameters;
    }

    public boolean isNamedByUser() {
        return isNamedByUser;
    }

    public LinkedList<String> processRealParameters(ArrayList<String> realParameters) {
        return null;
    }


}
