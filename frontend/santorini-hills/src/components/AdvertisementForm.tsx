import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import {
  AdvertisementTypeStage,
  AdvertisementPicturesStage,
  PropertyLocationStage,
  AdvertisementTitleStage,
  AdvertisementDescriptionStage,
  AdvertisementPriceStage
} from "./AdvertisementFormStages";

const AdvertisementForm = () => {
  const { stage } = useAdvertisementContext();
  // Ajusta las etapas a tu estructura real
  const formStages = [
    AdvertisementTypeStage,
    PropertyLocationStage,
    AdvertisementPicturesStage,
    AdvertisementTitleStage,
    AdvertisementDescriptionStage,
    AdvertisementPriceStage,
  ];

  const CurrentStageComponent =
    formStages[stage] || (() => <div>No hay etapa definida</div>);

  const percentage = ((stage + 1) / formStages.length) * 100;

  return (
    <>
      <nav className="p-4 max-w-7xl text-accent">
        <h1>Santorini Hills</h1>
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
