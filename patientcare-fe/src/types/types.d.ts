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
  type?: "OFFLINE" | "ONLINE";
}
