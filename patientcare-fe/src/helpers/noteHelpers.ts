export function lookupNoteType(noteType: string | undefined): string {
    const MAPPING = {
        MEASUREMENT: "Messung", TREATMENT: "Behandlung", DIAGNOSIS: "Diagnose", NOTEFILE: "Datei",
    } as const;

    return MAPPING[noteType as keyof typeof MAPPING] ?? "unbekannt";
}
