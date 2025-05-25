import { Link, useParams } from "react-router";
import ProfileSettings from "../components/ProfileSettings";
import { JSX } from "react";
import PropertiesSettings from "../components/PropertiesSettings";
import { useUserContext } from "../context/user/UserContext";

const SettingsPage = () => {
  type Settings = "profile" | "properties" | "notifications" | "security";
  const { setting } = useParams<{ setting: string }>();

  // Mapeo de nombres técnicos a nombres amigables para el usuario
  const settingLabels: Record<Settings, string> = {
    profile: "Perfil",
    properties: "Propiedades",
    notifications: "Notificaciones",
    security: "Seguridad"
  };

  // cada uno de los settings tiene su propio componente
  const settingsComponents: Record<Settings, JSX.Element> = {
    profile: <ProfileSettings />,
    properties: <PropertiesSettings />,
    notifications: <ProfileSettings />,
    security: <ProfileSettings />,
  };
  const selectedSetting = settingsComponents[setting as Settings] || <ProfileSettings />;

  // Obtener todas las opciones disponibles
  const settingOptions = Object.keys(settingsComponents) as Settings[];

  // Filtrar opciones según el perfil del usuario
  const { user } = useUserContext();
  const filteredSettingOptions = settingOptions.filter(option => {
    if (option === "properties" && user?.profile === "renter") return false;
    if (option === "security" && (user?.profile === "owner" || user?.profile === "renter")) {
      return false; // Oculta la opción "security" para "owner" y "renter"
    }
    return true;
  });

  return (
    <div className="flex w-7xl mx-auto min-h-[calc(100vh-10rem)] mt-5 gap-3">
      <aside className="w-1/4 rounded-xl bg-white shadow-lg p-4 font-medium text-neutral-800">
        <ul>
          {filteredSettingOptions.map((option) => (
            <li key={option}>
              <Link
                className={`block w-full my-2 text-right p-3 transition-all rounded-md ${setting === option
                  ? "bg-accent text-white"
                  : "hover:bg-accent hover:text-white"
                  }`}
                to={`/settings/${option}`}
              >
                {settingLabels[option]}
              </Link>
            </li>
          ))}
        </ul>
      </aside>
      <main className="flex-1 p-10 rounded-xl bg-white shadow-lg">
        <div className="w-full h-full flex flex-col justify-start items-start">
          {selectedSetting}
        </div>
      </main>
    </div>
  );
};

export default SettingsPage;