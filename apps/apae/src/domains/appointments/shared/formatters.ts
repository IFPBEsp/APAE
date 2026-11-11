import type { Professional } from '../types/appointments.types';

export function getProfessionalAreaName(
  professional?: Pick<Professional, 'serviceArea'> | null,
): string {
  return professional?.serviceArea?.area ?? '';
}

export const formatTimeForBackend = (timeString: string): string => {
  if (timeString.length === 5) {
    return `${timeString}:00`;
  }
  return timeString;
};

export const parseTimeFromBackend = (timeString: string): string => {
  return timeString.substring(0, 5);
};