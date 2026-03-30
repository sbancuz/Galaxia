package com.gtnewhorizons.galaxia.utility.hazards;

public enum HazardWarnings {

    FINE("No warnings"),
    FREEZING("Freezing"),
    BURNING("Burning"),
    LOW_PRESSURE("Low pressure"),
    HIGH_RADIATION("High radiation"),
    SPORES("Spores detected"),
    WITHER("Withering"),
    LOW_OXYGEN("Low oxygen"),
    NO_OXYGEN("Oxygen depleted");

    public final String message;

    HazardWarnings(String message) {
        this.message = message;
    }
}
