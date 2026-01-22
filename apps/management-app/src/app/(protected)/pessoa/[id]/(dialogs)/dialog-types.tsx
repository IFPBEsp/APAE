export type DialogProps<T> = {
  open: boolean;
  member: any;
  onOpenChange: (open: boolean) => void;
};

export enum DialogType {
  ADDRESS,
  DOCUMENTATION,
  PERSONAL,
  GUARDIANS,
}
