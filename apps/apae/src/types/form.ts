import { z } from "zod";
import { EditAddress, EditPersonal, EditAdditionals, EditGuardian, EditProfile } from "@/schemas/edit-member-schemas";
import { Address, Personal, Additionals, Guardian, Profile, Kinships } from "@/domains/patients/schemas/member-schemas";

export type EditAddressFormValues = z.infer<typeof EditAddress>;
export type EditPersonalFormValues = z.infer<typeof EditPersonal>;
export type EditAdditionalsFormValues = z.infer<typeof EditAdditionals>;
export type EditGuardianFormValues = z.infer<typeof EditGuardian>;
export type EditProfileFormValues = z.infer<typeof EditProfile>;

export type AddressFormValues = z.infer<typeof Address>;
export type PersonalFormValues = z.infer<typeof Personal>;
export type AdditionalsFormValues = z.infer<typeof Additionals>;
export type GuardianFormValues = z.infer<typeof Guardian>;
export type ProfileFormValues = z.infer<typeof Profile>;
export type KinshipsFormValues = z.infer<typeof Kinships>;
