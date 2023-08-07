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

    public String expandMacros() {

    }
}
