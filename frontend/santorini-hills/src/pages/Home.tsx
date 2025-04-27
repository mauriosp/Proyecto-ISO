import AdvertisementCard from "../components/AdvertisementCard";
import {testAdvertisements} from "../testAdvertisements";

const Home = () => {
  return (
    <div className="flex flex-col items-center justify-center w-10/12 mx-auto mt-10">
      <section className="w-full">
        <h2 className="font-semibold text-2xl">Destacados</h2>
        <div className="flex flex-wrap gap-10 mt-10 overflow-x-auto w-">
          {testAdvertisements.map((advertisement) => (<AdvertisementCard key={advertisement.id} advertisement={advertisement} />))}
        </div>
      </section>
    </div>
  );
};



export default Home;
