import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import {
  AdvertisementDescriptionStage,
  AdvertisementExtraInfoStage,
  AdvertisementPicturesStage,
  AdvertisementPreviewStage,
  AdvertisementPriceStage,
  AdvertisementTitleStage,
  AdvertisementTypeStage,
  AdvertismentInformationStage,
  PropertyLocationStage,
} from "./AdvertisementFormStages";

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
    AdvertisementExtraInfoStage,
    AdvertisementPriceStage,
    AdvertisementPreviewStage,
  ];

  const CurrentStageComponent =
    formStages[stage] || (() => <div>No hay etapa definida</div>);

  const percentage = ((stage + 1) / formStages.length) * 100;

  return (
    <>
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
