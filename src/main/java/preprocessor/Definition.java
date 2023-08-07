package preprocessor;

import java.util.LinkedList;

public class Definition {
    private String code;
    private LinkedList<Integer> usedAt;


    public Definition() {

    }

    public boolean isDeclared() {
        return code != null;
    }
}
