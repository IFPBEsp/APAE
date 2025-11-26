import { Patient, Professional, UUID } from "@/app/services/appointmentService";

export interface TodayAppointment {
    id: UUID;
    patient: Patient;
    professional: Professional;
    scheduledDateTime: Date;
	overriddenDateTime: Date,
	performed: boolean;
	cancelled: boolean;
	cancellationReason: string;
	effectiveDateTime: Date;
	ruleId: UUID,
}