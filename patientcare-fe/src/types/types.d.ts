export interface Patient extends User {}

export interface Doctor extends User {
  title: string;
  speciality: string;
  medicalId: string;
}
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  dateOfBirth: Date;
  profileImg: string;
  userType: "DOCTOR" | "PATIENT";
}

export interface Appointment {
  start: datetime;
  end: datetime;
  title: string;
  content: string;
  class: string;
}
