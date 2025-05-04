import AdvertisementForm from "../components/AdvertisementForm";
import { AdvertisementProvider } from "../context/advertisement/AdvertisementProvider";
import { useParams } from "react-router";
import { testAdvertisements } from "../testAdvertisements";

const PostAdvertisement = () => {
  const { id } = useParams();
  let initialData = undefined;
  if (id) {
    initialData = testAdvertisements.find(ad => String(ad.id) === String(id));
  }
  return (
    <AdvertisementProvider initialData={initialData}>
      <AdvertisementForm />
    </AdvertisementProvider>
  );
};

export default PostAdvertisement;
