import { Patient, Professional, UUID } from "@/app/services/appointmentService";

export interface TodayAppointment {
	id: UUID;
	annualRegistration: {
		bpc: string;
		diseases: string;
		disorders: Array<string>;
		familyIncome: number;
		id: UUID;
		patient: Patient;
		year: string;
	},
	professional: Professional;
	scheduledDateTime: Date;
	overriddenDateTime: Date,
	performed: boolean;
	cancelled: boolean;
	cancellationReason: string;
	effectiveDateTime: Date;
	ruleId: UUID,
}