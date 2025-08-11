"use client";

import type React from "react";
import { useState } from "react";
import { login } from "../service/loginService";
import type { LoginPayload, LoginResponse } from "../service/interfaces";

export default function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      if (!email || !password) {
        setError("Por favor, preencha todos os campos.");
        setIsLoading(false);
        return;
      }
      
      const payload: LoginPayload = {
        username: email,
        password: password,
      };

      const response: LoginResponse = await login(payload); 
      console.log("Login bem-sucedido:", response);

      alert("Login bem-sucedido! O token foi retornado.");
    } catch (err) {
      console.error("Erro no login:", err);
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Falha no login. Ocorreu um erro inesperado.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative z-10 w-full max-w-md mx-auto">
      <div className="bg-white rounded-3xl shadow-2xl p-8 mx-4">
        <div className="flex justify-center mb-8">
          <div className="w-16 h-16 bg-white rounded-full shadow-lg flex items-center justify-center border-4 border-gray-100">
            <div className="text-2xl"><img src="Group 3.svg" alt="" /></div>
          </div>
        </div>

        <h1 className="text-2xl font-semibold text-center text-gray-800 mb-8">Entrar</h1>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
              Email ou CPF
            </label>
            <input
              type="text"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all duration-200 placeholder-gray-400 text-gray-600"
              placeholder="Digite seu email ou CPF"
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
              Senha
            </label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 pr-12 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all duration-200 placeholder-gray-400 text-gray-600"
                placeholder="Digite sua senha"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-black-500 hover:text-gray-700 transition-colors duration-200"
              >
                {showPassword ? (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21"
                    />
                  </svg>
                ) : (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                    />
                  </svg>
                )}
              </button>
            </div>
          </div>

          <div className="text-right">
            <a href="#" className="text-sm text-gray-600 hover:text-blue-600 transition-colors duration-200">
              Esqueceu a senha?
            </a>
          </div>

          {error && (
            <div className="text-red-500 text-sm text-center">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={isLoading}
            className={`w-full font-semibold py-3 px-4 rounded-lg transition-colors duration-200 shadow-lg transform transition-all duration-200
              ${isLoading 
                ? "bg-blue-400 cursor-not-allowed" 
                : "bg-blue-600 hover:bg-blue-700 text-white hover:shadow-xl hover:-translate-y-0.5"}`}
          >
            {isLoading ? "Entrando..." : "ENTRAR"}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-sm text-gray-600">
            Não possui uma conta ainda?{" "}
            <a href="#" className="text-orange-500 hover:text-orange-600 font-medium transition-colors duration-200">
              Criar uma conta
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
