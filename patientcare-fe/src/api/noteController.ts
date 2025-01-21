import axios from "./axiosConfig";
import type {AppointmentNote, NoteFile} from "@/types/types";

export const createNote = async (note: AppointmentNote) => {
    console.log("payload", note);
    const response = await axios.post(`/notes`, note);
    return response.data as AppointmentNote;
};

export const uploadFile = async (file: NoteFile) => {
    const formData = new FormData();
    formData.append("file", file.file);
    formData.append("description", file.description);
    formData.append("appointmentId", file.appointmentId.toString());

    console.log("formdata", formData);
    try {
        const response = await axios.post("/notes/upload", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
        return response.data;
    } catch (error) {
        console.error("Error uploading file:", error);
        throw error;
    }
};

export const constructNoteFileUrl = (note: AppointmentNote) => {
    return `${axios.defaults.baseURL}/notes/download/${note.id}`;
};

export class requestNoteFileDownload {
}