import * as z from "zod";
import {
  Personal,
  Address,
  Additionals,
  Guardian,
  Profile,
} from "../domains/patients/schemas/member-schemas";

export const EditAdditionals = Additionals.extend({
  disability: z.object({
    types: z.array(z.string().min(1)).min(1, "O tipo de atendimento é obrigatório."),
    report: z.union([z.instanceof(File), z.string()]),
  }),
  care: z.object({
    types: z.array(z.string().min(1)).min(1, "O tipo de atendimento é obrigatório."),
    referral: z.union([z.instanceof(File), z.string()]),
  }),
});

export const EditProfile = Profile.extend({
  photo: z.union([z.instanceof(File), z.string()]).optional(),
});

export const EditPersonal = Personal;
export const EditAddress = Address;
export const EditGuardian = Guardian;
