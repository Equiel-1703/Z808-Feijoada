package preprocessor;

import java.util.LinkedList;

public class Definition {
    private String code;

    private LinkedList<Integer> usedAt;


    public Definition(String code) {
        this.code = code;
    }

    public boolean isDeclared() {
        return code != null;
    }

    public LinkedList<Integer> getUsedAt() {
        return usedAt;
    }

    public String getCode() {
        return code;
    }
}
