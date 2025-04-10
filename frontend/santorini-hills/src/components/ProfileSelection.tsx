import React, { useState } from "react";
import { FaHouseChimney, FaHouseChimneyUser } from "react-icons/fa6";
import { useRegisterContext } from "../context/registerForm/RegisterContext";
import { register } from "../utils/APICalls";
import { User } from "../models/user";
import FormRadioOption from "./FormRadioOption";
import { useModalContext } from "../context/modal/ModalContext";

interface ProfileOptionData {
  value: "owner" | "renter";
  id: string;
  icon: React.JSX.Element;
  title: string;
  description: string;
}

const profileOptions: ProfileOptionData[] = [
  {
    value: "owner",
    id: "owner-input",
    icon: <FaHouseChimney size={80} />,
    title: "Propietario",
    description:
      "¡Publica anuncios con tus propiedades raíces, y encuentra un arrendador en tiempo récord!",
  },
  {
    value: "renter",
    id: "renter-input",
    icon: <FaHouseChimneyUser size={80} />,
    title: "Inquilino",
    description:
      "Conecta con propietarios de propiedades adaptadas a tus necesidades, y encuentra el lugar de tus sueños!",
  },
];

interface ProfileSelectionProps {
  onSubmit: (profile: "owner" | "renter") => void;
}

const ProfileSelection: React.FC<ProfileSelectionProps> = () => {
  const {openModal} = useModalContext();

  const [profile, setProfile] = useState<"owner" | "renter">();

  const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setProfile(e.target.value as "owner" | "renter");

  const { user, setUser } = useRegisterContext();

  const handleContinue = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const updatedUser : User = { ...user, profile, isVerified: false };
    setUser(updatedUser);
    register(updatedUser);
    openModal("login");
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleContinue}>
      {profileOptions.map((option) => (
        <FormRadioOption
          key={option.id}
          name="profile"
          value={option.value}
          id={option.id}
          onChange={handleProfileChange}
        >
          <div className="flex gap-8">
            {option.icon}
            <div className="flex flex-col justify-center flex-1 max-w-[400px]">
              <h4 className="text-xl font-semibold">{option.title}</h4>
              <p className="text-sm">{option.description}</p>
            </div>
          </div>
        </FormRadioOption>
      ))}
      {profile && (
        <button className="form-button mt-4 hover:bg-slate-800 bg-accent text-white font-semibold">
          Continuar
        </button>
      )}
    </form>
  );
};

export default ProfileSelection;
