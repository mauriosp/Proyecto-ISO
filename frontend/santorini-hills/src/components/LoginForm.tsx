import { FormInput } from "./FormInput";
import { FcGoogle as GoogleIcon } from "react-icons/fc";
import Separator from "./Separator";
import SocialLoginButton from "./SocialLoginButton";
import { useForm } from "react-hook-form";
import { login } from "../utils/APICalls";

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<{ email: string; password: string }>();

  const onSubmit = (data: { email: string; password: string }) => {
    login(data.email, data.password)
      .then((response) => {
        console.log("Login successful", response);
      })
      .catch((error) => {
        console.error("Login failed", error);
      });
    
    console.log({ email: data.email, password: data.password });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col space-y-2">
      <div>
        <label className="form-label" htmlFor="email-input">
          Correo electrónico
        </label>
        <FormInput
          id="email-input"
          placeholder="Escribe tu correo"
          type="email"
          {...register("email", {
            required: "El correo es obligatorio",
            pattern: {
              value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
              message: "Formato de correo inválido",
            },
          })}
        />
        {errors.email && (
          <p className="text-red-500 text-sm">{errors.email.message}</p>
        )}
      </div>
      <div>
        <label className="form-label" htmlFor="password-input">
          Contraseña
        </label>
        <FormInput
          id="password-input"
          placeholder="Escribe tu contraseña"
          type="password"
        />
        <div className="flex h-6 justify-end items-center">
          <a
            className="text-xs text-accent font-medium hover:saturate-200 transition-all"
            href="#"
          >
            ¿Olvidaste tu contraseña?
          </a>
        </div>
      </div>
      <div className="flex items-center gap-2 mb-4">
        <input
          className="w-5 h-5 accent-accent border-gray-300 rounded-md focus:ring-0"
          type="checkbox"
          name="remember-me"
          id="remember-me-checkbox"
        />
        <label className="form-label" htmlFor="remember-me-checkbox">
          Recordarme
        </label>
      </div>
      <button
        className="form-button hover:bg-slate-800 bg-accent text-white font-semibold"
        type="submit"
      >
        Iniciar sesión
      </button>
      <Separator label="o" />
      <SocialLoginButton
        Icon={GoogleIcon}
        text="Continuar con Google"
        buttonStyles="form-button"
      />
      <div className="mt-2">
        <Separator />
      </div>
      <div className="text-center text-neutral-700">
        ¿Nuevo usuario?{" "}
        <span>
          <a
            className="font-medium hover:font-semibold text-accent transition-all"
            href="#"
          >
            Regístrate
          </a>
        </span>
      </div>
    </form>
  );
};

export default LoginForm;
