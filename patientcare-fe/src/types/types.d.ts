export interface Patient extends User {}

export interface Doctor extends User {
  title: string;
  speciality: string;
  licenseId?: string;
}
export interface User {
  id: number | undefined;
  firstName: string;
  lastName: string;
  street?: string;
  houseNumber?: string;
  zipCode?: string;
  city?: string;
  dateOfBirth?: Date;
  profileImg?: string;
  phoneNumber: string;
  email: string;
  userType: "DOCTOR" | "PATIENT";
}

export interface Appointment {
  id: number;
  date?: date;
  start: datetime;
  end: datetime;
  title: string;
  content?: string;
  class?: string;
  patient?: Patient | null;
  doctor?: Doctor | null;
  notes?: AppointmentNote[];
  type?: "OFFLINE" | "ONLINE";
}

export interface BackendAppointment {
  id: number;
  date?: string;
  startDateTime: string; // Corresponds to `start` in the frontend
  endDateTime: string; // Corresponds to `end` in the frontend
  title: string;
  content?: string;
  class?: string;
  patient?: Patient | null;
  doctor?: Doctor | null;
  notes?: any;
  type?: "OFFLINE" | "ONLINE";
}
export interface AppointmentNote {
  id: number | undefined;
  timestamp?: string;
  appointmentId?: number | undefined;
  doctorId: number | undefined;
  patientId: number | undefined;
  creator: "Patient" | "Doctor";
  file: NoteFile;
  body?: string;
  type: string | undefined;
  payload?: Measurement | Treatment | Diagnosis | FILE[];
  noteType: "MEASUREMENT" | "DIAGNOSIS" | "TREATMENT" | "FILE" | undefined;
  value?: number;
  icdCode?: string;
  recommendation?: string;
  action?: string;
  description?: string;
}

export interface Diagnosis {
  icdCode: string;
  recommendation: string;
}

export interface Measurement {
  type:
    | "BLOOD_SUGAR"
    | "BLOOD_PRESSURE"
    | "HEART_RATE"
    | "BODY_FAT"
    | "OXYGEN_BLOOD_SATURATION"
    | "BODY_TEMPERATURE"
    | "BODY_WEIGHT"
    | "BODY_HEIGHT"
    | undefined;
  value: number | null;
}
export interface Treatment {
  action: string;
  diagnosis: Diagnosis | null;
}
export interface NoteFile {
  file: File;
  description: string;
  base64Data: string;
}
