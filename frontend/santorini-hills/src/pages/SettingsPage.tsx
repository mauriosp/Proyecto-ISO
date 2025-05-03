import { useParams } from "react-router";
import EditForm from "../components/EditForm";

const SettingsPage = () => {
  type Settings = "profile" | "properties" | "notifications" | "security" | "payment" | "privacy";
  const { setting } = useParams<{ setting : string }>();

  return (
    <div className="flex max-w-7xl mx-auto min-h-[calc(100vh-10rem)] mt-5 gap-3">
      <aside className="w-1/4 rounded-xl bg-white shadow-lg p-4 font-medium text-neutral-800">
        <ul>
          <li>
            <button className="w-full text-right p-3 hover:bg-accent hover:text-white transition-all rounded-md hover:cursor-pointer">Perfil</button>
          </li>
          <li>
            <button className="w-full text-right p-3 hover:bg-accent hover:text-white transition-all rounded-md hover:cursor-pointer">Propiedades</button>
          </li>
        </ul>
      </aside>
      <main className="flex-1 p-10 rounded-xl bg-white shadow-lg">
        <EditForm />
      </main>
    </div>
  );
};

export default SettingsPage;
