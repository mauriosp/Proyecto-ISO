import { FaRegStar } from "react-icons/fa6";
import { MdOutlineBed } from "react-icons/md";
import { TbBath, TbMeterSquare } from "react-icons/tb";
import { AdvertisementPost } from "../models/advertisement";
import { formatNumber } from "../utils/parseNumbers";
import { Link } from "react-router";

const AdvertisementCard: React.FC<{ advertisement: AdvertisementPost }> = ({ advertisement }) => {
  const { title, price, property, images } = advertisement;

  return (
    <Link to={`/advertisements/${advertisement.id}`} className="w-64 flex flex-col items-center justify-center rounded-3xl">
      <img
        className="w-full aspect-square object-cover rounded-3xl"
        src={images[0] || "https://thumb.ac-illust.com/b1/b170870007dfa419295d949814474ab2_t.jpeg"}
        alt={title}
      />
      <div className="w-full mt-3">
        <div className="flex items-center justify-between">
          <h4 className="font-semibold text-lg">${formatNumber(price)}</h4>
          <div className="flex items-center gap-1">
            <FaRegStar size={20} />
            <p className="font-medium text-sm">4.5</p>
          </div>
        </div>
        <div className="flex gap-4 items-center">
          <div className="flex items-center gap-1 text-accent">
            <MdOutlineBed size={20} />
            <p className="font-medium text-sm">{property?.bedrooms || 0}</p>
          </div>
          <div className="flex items-center gap-1 text-accent">
            <TbBath size={20} />
            <p className="font-medium text-sm">{property?.bathrooms || 0}</p>
          </div>
          <div className="flex items-center gap-1 text-accent">
            <TbMeterSquare size={20} />
            <p className="font-medium text-sm">{property?.area || 0}</p>
          </div>
        </div>
        <h3 className="font-semibold text-neutral-600 mt-2">{title}</h3>
      </div>
    </Link>
  );
};

export default AdvertisementCard;