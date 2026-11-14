package com.blackmoss.lazycat.api.recipe.thaumaturge;

public enum InstabilityType {
    NEGLIGIBLE(0),
    MINOR(1),
    MODERATE(2),
    HIGH(3),
    VERY_HIGH(4),
    DANGEROUS(5);

    private final int instability;

    InstabilityType(int instability) {
        this.instability = instability;
    }

    public int get() {
        return instability;
    }
}
