import axios from "axios";
import { cookies } from "next/headers";

const LOCAL_API_BASE_URL = "http://localhost:8090/apae-geral/api";

function trimTrailingSlash(baseURL?: string) {
  return baseURL?.replace(/\/+$/, "");
}

function getBaseApiURL() {
  return (
    trimTrailingSlash(process.env.API_URL) ||
    trimTrailingSlash(process.env.NEXT_PUBLIC_API_URL) ||
    LOCAL_API_BASE_URL
  );
}

export async function createBaseApi() {
  const session = (await cookies()).get("session")?.value;

  return axios.create({
    baseURL: getBaseApiURL(),
    headers: {
      ...(session ? { Authorization: `Bearer ${session}` } : {}),
    },
  });
}
