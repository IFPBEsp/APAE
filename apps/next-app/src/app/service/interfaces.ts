export interface LoginPayload {
  username: string;
  password: string;
}

export interface LoginResponse {
  access_token: string;
  expires_in: number;
  refresh_token: string;
  token_type: string;
  "not-before-policy"?: number;
  session_state?: string;
  scope?: string;
}
