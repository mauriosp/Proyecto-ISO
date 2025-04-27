import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import {
  AdvertisementTypeStage,
  AdvertisementPicturesStage,
  PropertyLocationStage,
  AdvertismentInformationStage,
  AdvertisementTitleStage,
  AdvertisementDescriptionStage,
  AdvertisementPriceStage,
  AdvertisementPreviewStage,
} from "./AdvertisementFormStages";
import name from "../assets/logo/Name.png";
import { Link } from "react-router";

const AdvertisementForm = () => {
  const { stage } = useAdvertisementContext();
  // Ajusta las etapas a tu estructura real
  const formStages = [
    AdvertisementTypeStage,
    AdvertismentInformationStage,
    PropertyLocationStage,
    AdvertisementPicturesStage,
    AdvertisementTitleStage,
    AdvertisementDescriptionStage,
    AdvertisementPriceStage,
    AdvertisementPreviewStage,
  ];

  const CurrentStageComponent =
    formStages[stage] || (() => <div>No hay etapa definida</div>);

  const percentage = ((stage + 1) / formStages.length) * 100;

  return (
    <>
      <nav className="p-4 max-w-7xl text-accent">
        <div className="w-1/5">
          <Link className="w-min" to={"/"}>
            <img src={name} alt="Santorini Hills" className="h-8 object-contain" />
          </Link>
        </div>
      </nav>
      <main className="flex-1 flex items-center justify-center p-4">
        <CurrentStageComponent />
      </main>
      <footer
        style={{ width: `${percentage}%` }}
        className="bg-accent h-5 transition-all"
      ></footer>
    </>
  );
};

export default AdvertisementForm;
