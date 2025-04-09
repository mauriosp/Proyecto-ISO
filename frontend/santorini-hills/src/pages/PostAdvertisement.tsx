import AdvertisementForm from "../components/AdvertisementForm";
import { AdvertisementProvider } from "../context/advertisement/AdvertisementProvider";

const PostAdvertisement = () => {
  return (
    <AdvertisementProvider>
      <div className="flex flex-col">
        <AdvertisementForm />
      </div>
    </AdvertisementProvider>
  );
};

export default PostAdvertisement;
