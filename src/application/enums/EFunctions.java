package application.enums;

public enum EFunctions {
    Quadratic("Kwadratowa"),
    Rastring ("Rastringa"),
    ContinuousTaskWithConstraints("Zadanie ciągle z ogarniczeniami");

    private final String name;

    /**
     * @param name
     */
    EFunctions(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    }
