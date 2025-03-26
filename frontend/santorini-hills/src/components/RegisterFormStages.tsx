import { FcGoogle as GoogleIcon } from "react-icons/fc";
import { countriesList } from "../assets/countries";
import { useModalContext } from "../context/modal/ModalContext";
import { FormInput } from "./FormInput";
import ProfileSelection from "./ProfileSelection";
import Separator from "./Separator";
import SocialLoginButton from "./SocialLoginButton";
import { useRegisterContext } from "../context/registerForm/RegisterContext";
import { useEffect, useRef } from "react";
import { useForm } from "react-hook-form";

export const EmailStage = () => {
  const { setMessage } = useModalContext();
  const { setNextStage } = useRegisterContext();

  useEffect(() => {
    setMessage("¡Crea una cuenta!");
  }, [setMessage]);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<{ email: string }>();

  const onSubmit = (data: { email: string }) => {
    setNextStage({ email: data.email });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleSubmit(onSubmit)}>
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
        {errors.email && <p className="text-red-500 text-sm">{errors.email.message}</p>}
      </div>
      <button
        type="submit"
        className="form-button hover:bg-slate-800 bg-accent text-white font-semibold"
        disabled={isSubmitting}
      >
        {isSubmitting ? "Verificando..." : "Continuar"}
      </button>
      <Separator label="o" />
      <SocialLoginButton Icon={GoogleIcon} text="Continuar con Google" buttonStyles="form-button" />
      <div className="mt-2">
        <Separator />
      </div>
      <div className="text-center text-neutral-700">
        ¿Ya tienes una cuenta?{" "}
        <a className="font-medium hover:font-semibold text-accent transition-all" href="#">
          Inicia sesión
        </a>
      </div>
    </form>
  );
};

export const PasswordStage = () => {
  const { setMessage } = useModalContext();
  const { setNextStage } = useRegisterContext();

  useEffect(() => {
    setMessage("¡Crea tu contraseña!");
  }, [setMessage]);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<{ password: string; confirmPassword: string }>();

  const onSubmit = (data: { password: string }) => {
    setNextStage({ password: data.password });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label className="form-label" htmlFor="password-input">
          Contraseña
        </label>
        <FormInput
          id="password-input"
          placeholder="Escribe tu contraseña"
          type="password"
          {...register("password", {
            required: "La contraseña es obligatoria",
            minLength: {
              value: 8,
              message: "Debe tener al menos 8 caracteres",
            },
            pattern: {
              value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
              message:
                "Debe incluir mayúscula, minúscula, número y carácter especial",
            },
          })}
        />
        {errors.password && <p className="text-red-500 text-sm">{errors.password.message}</p>}
      </div>

      <div>
        <label className="form-label" htmlFor="confirm-password-input">
          Confirmar contraseña
        </label>
        <FormInput
          id="confirm-password-input"
          placeholder="Escribe tu contraseña"
          type="password"
          {...register("confirmPassword", {
            required: "Confirma tu contraseña",
            validate: (value) => value === watch("password") || "Las contraseñas no coinciden",
          })}
        />
        {errors.confirmPassword && <p className="text-red-500 text-sm">{errors.confirmPassword.message}</p>}
      </div>

      <button
        className="form-button mt-4 hover:bg-slate-800 bg-accent text-white font-semibold"
        type="submit"
      >
        Continuar
      </button>
    </form>
  );
};

export const PersonalInfoStage = () => {
  const { setMessage } = useModalContext();
  const { setNextStage } = useRegisterContext();

  useEffect(() => {
    setMessage("¡Te queremos conocer!");
  }, [setMessage]);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<{ name: string; surname: string; phone: string; dialCode: string }>();

  const onSubmit = (data: { name: string; surname: string; phone: string; dialCode: string }) => {
    setNextStage({
      name: `${data.name} ${data.surname}`,
      phone: `${data.dialCode}${data.phone}`,
    });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label className="form-label" htmlFor="name-input">
          Nombre
        </label>
        <FormInput
          id="name-input"
          placeholder="Escribe tu nombre"
          {...register("name", {
            required: "El nombre es obligatorio",
            pattern: {
              value: /^[a-zA-ZÀ-ÿ\s]+$/,
              message: "El nombre solo debe contener letras",
            },
          })}
        />
        {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}
      </div>

      <div>
        <label className="form-label" htmlFor="surname-input">
          Apellido
        </label>
        <FormInput
          id="surname-input"
          placeholder="Escribe tu apellido"
          {...register("surname", {
            required: "El apellido es obligatorio",
            pattern: {
              value: /^[a-zA-ZÀ-ÿ\s]+$/,
              message: "El apellido solo debe contener letras",
            },
          })}
        />
        {errors.surname && <p className="text-red-500 text-sm">{errors.surname.message}</p>}
      </div>

      <div>
        <label className="form-label" htmlFor="phone-input">
          Celular
        </label>
        <div className="flex gap-2">
          <select
            className="font-medium text-sm py-3 px-3 w-min shadow-md rounded-xl border-accent border outline-none transition-all"
            defaultValue={"+57"}
            {...register("dialCode")}
          >
            {countriesList.map((country) => (
              <option key={country.code} value={country.dial_code}>
                {country.flag}
              </option>
            ))}
          </select>
          <FormInput
            id="phone-input"
            placeholder="Ej. (123) 456 - 7890"
            type="tel"
            {...register("phone", {
              required: "El número de teléfono es obligatorio",
              pattern: {
                value: /^[0-9]{7,15}$/,
                message: "Debe contener entre 7 y 15 dígitos numéricos",
              },
            })}
          />
        </div>
        {errors.phone && <p className="text-red-500 text-sm">{errors.phone.message}</p>}
      </div>

      <button className="form-button mt-4 hover:bg-slate-800 bg-accent text-white font-semibold" type="submit">
        Continuar
      </button>
    </form>
  );
};

export const ProfileStage = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("¿Cuál es tu perfil?");
  }, [setMessage]);

  return <ProfileSelection onSubmit={() => {}} />;
};
