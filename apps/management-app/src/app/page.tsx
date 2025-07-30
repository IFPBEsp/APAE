import { IS_LOGGED_IN } from "@/constants/is-logged-in";
import { redirect } from "next/navigation";

export default function Home() {
  if (!IS_LOGGED_IN) {
    redirect("/login");
  }

  return <h1>LOGGED PAGE - HELLO, WORLD!</h1>;
}
