import axios from "axios";

export const apiAuth = axios.create({
  baseURL:
    process.env.NEXT_PUBLIC_API_URL_AUTH ?? "http://localhost:9000/api/auth/",
});
