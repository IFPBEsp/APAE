import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

function MembersRegisterForm({
  title,
  buttons,
  children,
  className,
  onSubmit,
}: {
  title: string;
  buttons: React.ReactNode;
} & React.ComponentProps<"form">) {
  return (
    <>
      <h2 className="text-xl font-bold text-blue-900/50 mb-4">{title}</h2>
      <form
        onSubmit={onSubmit}
        className={cn(
          "flex flex-col grow justify-between space-y-7",
          className,
        )}
      >
        {children}
        <div className="flex justify-end gap-4">{buttons}</div>
      </form>
    </>
  );
}

function DoubleColumn({ className, children }: React.ComponentProps<"div">) {
  return (
    <div className={cn("grid grid-cols-1 md:grid-cols-2 gap-6", className)}>
      {children}
    </div>
  );
}

function FileInputButton({
  id,
  onChange,
  children,
  ...props
}: Readonly<
  Required<Pick<React.ComponentProps<"input">, "onChange" | "id">> &
    Omit<React.ComponentProps<"button">, "onChange" | "onClick">
>) {
  return (
    <div className="w-full overflow-hidden">
      <input
        type="file"
        id={`${id}-upload`}
        className="hidden"
        onChange={onChange}
      />

      <Button
        type="button"
        variant="outline"
        onClick={() => document.getElementById(`${id}-upload`)?.click()}
        {...props}
      >
        {children}
      </Button>
    </div>
  );
}

function FormButton({
  className,
  children,
  ...props
}: React.ComponentProps<"button">) {
  return (
    <Button
      className={cn(
        "bg-yellow-300 text-blue-900 px-8 py-6 text-lg cursor-pointer hover:bg-blue-900 hover:text-white",
        className,
      )}
      size="lg"
      {...props}
    >
      {children}
    </Button>
  );
}

export { MembersRegisterForm, DoubleColumn, FileInputButton, FormButton };
