import AdvertisementForm from "../components/AdvertisementForm";
import { AdvertisementProvider } from "../context/advertisement/AdvertisementProvider";

const PostAdvertisement = () => {
  return (
    <AdvertisementProvider>
      <div className="flex flex-col h-screen">
        <AdvertisementForm />
      </div>
    </AdvertisementProvider>
  );
};

export default PostAdvertisement;
