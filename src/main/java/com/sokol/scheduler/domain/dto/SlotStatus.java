package com.sokol.scheduler.domain.dto;

public enum SlotStatus {
    FREE,       // Dostępny (Zielony)
    MY_BOOKING, // Mój termin (Niebieski)
    TAKEN,      // Zajęty przez kogoś innego (Szary)
    BLOCKED,    // Zablokowany przez admina (Szary)
    PAST        // Miniony termin (Szary)
}
