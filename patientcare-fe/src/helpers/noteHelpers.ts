import type {
  Appointment,
  BackendAppointment,
  Doctor,
  Patient,
  User,
} from "@/types/types";

export function lookupNoteType(noteType: string | undefined): string {
  const MAPPING = {
    MEASUREMENT: "Messung",
    TREATMENT: "Behandlung",
    DIAGNOSIS: "Diagnose",
    FILE: "Datei",
  } as const;

  return MAPPING[noteType as keyof typeof MAPPING] ?? "unbekannt";
}
