import React, { useState } from "react";
import ProfileOption from "./ProfileOption";
import { FaHouseChimney, FaHouseChimneyUser } from "react-icons/fa6";
import { useRegisterContext } from "../context/registerForm/RegisterContext";
import { register } from "../utils/APICalls";

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
  const [profile, setProfile] = useState<"owner" | "renter">();

  const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setProfile(e.target.value as "owner" | "renter");

  const { user, setUser } = useRegisterContext();

  const handleContinue = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setUser({ ...user, profile, isVerified: false });
    register(user);
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleContinue}>
      {profileOptions.map((option) => (
        <ProfileOption
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
        </ProfileOption>
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
