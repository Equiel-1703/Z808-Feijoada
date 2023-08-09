package preprocessor;

public class Preprocessor {
    private static Preprocessor instance;

    private Preprocessor() {

    }

    public Preprocessor getInstance() {
        if (instance == null)
            instance = new Preprocessor();

        return instance;
    }

    public String expandMacros(/*table, tokens*/) {
/*
        StringBuilder output = new StringBuilder();

        for (String token : tokens) {
            if (table.containsKey(token)) {
                output.append(macros.get(token)\n);
            } else {
                output.append(token);
            }
            output.append(" ");
        }

        return output.toString();
    }
*/
    }
}
