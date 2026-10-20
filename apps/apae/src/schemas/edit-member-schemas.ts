import * as z from "zod";
import { Personal, Address, Additionals, Guardian, Profile } from "../domains/patients/schemas/member-schemas";

export const EditAdditionals = Additionals.pick({
  vaccines: true,
  allergies: true,
});

export const EditProfile = Profile.extend({
  photo: z.union([z.instanceof(File), z.string()]).optional(),
});

export const EditPersonal = Personal;
export const EditAddress = Address;
export const EditGuardian = Guardian;