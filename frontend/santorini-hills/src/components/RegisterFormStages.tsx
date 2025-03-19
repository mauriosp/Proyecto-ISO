import { FcGoogle as GoogleIcon } from "react-icons/fc";
import { countriesList } from "../assets/countries";
import { useModalContext } from "../context/modal/ModalContext";
import { FormInput } from "./FormInput";
import ProfileSelection from "./ProfileSelection";
import Separator from "./Separator";
import SocialLoginButton from "./SocialLoginButton";
import { useRegisterContext } from "../context/registerForm/RegisterContext";
import { useEffect, useRef } from "react";

export const EmailStage = () => {
  const { setMessage } = useModalContext();

  useEffect(() => {
    setMessage("¡Crea una cuenta!");
  }, [setMessage]);

  const { setNextStage } = useRegisterContext();

  const emailInputRef = useRef<HTMLInputElement>(null);
  const handleContinue = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setNextStage({ email: emailInputRef.current?.value });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleContinue}>
      <div>
        <label className="form-label" htmlFor="email-input">
          Correo electrónico
        </label>
        <FormInput
          id="email-input"
          ref={emailInputRef}
          placeholder="Escribe tu correo"
          type="text"
        />
      </div>
      <button className="form-button hover:bg-slate-800 bg-accent text-white font-semibold">
        Continuar
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
        ¿Ya tienes una cuenta?{" "}
        <span>
          <a
            className="font-medium hover:font-semibold text-accent transition-all"
            href="#"
          >
            Inicia sesión
          </a>
        </span>
      </div>
    </form>
  );
};

export const PasswordStage = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("¡Crea tu contraseña!");
  }, [setMessage]);

  const { setNextStage } = useRegisterContext();

  const passwordInputRef = useRef<HTMLInputElement>(null);
  const handleContinue = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setNextStage({ password: passwordInputRef.current?.value });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleContinue}>
      <div>
        <label className="form-label" htmlFor="password-input">
          Contraseña
        </label>
        <FormInput
          id="password-input"
          placeholder="Escribe tu contraseña"
          type="password"
          ref={passwordInputRef}
        />
      </div>
      <div>
        <label className="form-label" htmlFor="confirm-password-input">
          Confirmar contraseña
        </label>
        <FormInput
          id="confirm-password-input"
          placeholder="Escribe tu contraseña"
          type="password"
        />
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
  useEffect(() => {
    setMessage("¡Te queremos conocer!");
  }, [setMessage]);

  const { setNextStage } = useRegisterContext();

  const nameInputRef = useRef<HTMLInputElement>(null);
  const surnameInputRef = useRef<HTMLInputElement>(null);
  const dialCodeInputRef = useRef<HTMLSelectElement>(null);
  const phoneInputRef = useRef<HTMLInputElement>(null);

  const handleContinue = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setNextStage({
      name: `${nameInputRef.current?.value} ${surnameInputRef.current?.value}`,
      phone: `${dialCodeInputRef.current?.value}${phoneInputRef.current?.value}`,
    });
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleContinue}>
      <div>
        <label className="form-label" htmlFor="name-input">
          Nombre
        </label>
        <FormInput
          id="name-input"
          ref={nameInputRef}
          placeholder="Escribe tu nombre"
        />
      </div>
      <div>
        <label className="form-label" htmlFor="surname-input">
          Apellido
        </label>
        <FormInput
          id="surname-input"
          ref={surnameInputRef}
          placeholder="Escribe tu apellido"
        />
      </div>
      <div>
        <label className="form-label" htmlFor="phone-input">
          Celular
        </label>
        <div className="flex gap-2">
          <select
            className="font-medium text-sm py-3 px-3 w-min shadow-md rounded-xl border-accent border outline-none transition-all"
            defaultValue={"+57"}
            ref={dialCodeInputRef}
            id="country-code"
          >
            {countriesList.map((country) => {
              return (
                <option key={country.code} value={country.dial_code}>
                  {country.flag}
                </option>
              );
            })}
          </select>
          <FormInput
            id="phone-input"
            ref={phoneInputRef}
            placeholder="Ej. (123) 456 - 7890"
            type="tel"
          />
        </div>
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

export const ProfileStage = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("¿Cuál es tu perfil?");
  }, [setMessage]);

  return <ProfileSelection onSubmit={() => {}} />;
};
