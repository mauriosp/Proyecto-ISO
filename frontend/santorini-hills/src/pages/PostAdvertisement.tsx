import AdvertisementForm from "../components/AdvertisementForm";
import { AdvertisementProvider } from "../context/advertisement/AdvertisementProvider";

const PostAdvertisement = () => {
  return (
    <AdvertisementProvider>
      <AdvertisementForm />
    </AdvertisementProvider>
  );
};

export default PostAdvertisement;
