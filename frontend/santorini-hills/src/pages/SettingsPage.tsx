import { Link, useParams } from "react-router";
import ProfileSettings from "../components/ProfileSettings";
import { JSX } from "react";
import PropertiesSettings from "../components/PropertiesSettings";

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

  return (
    <div className="flex max-w-7xl mx-auto min-h-[calc(100vh-10rem)] mt-5 gap-3">
      <aside className="w-1/4 rounded-xl bg-white shadow-lg p-4 font-medium text-neutral-800">
        <ul>
          {settingOptions.map((option) => (
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
        {selectedSetting}
      </main>
    </div>
  );
};

export default SettingsPage;