import { LoadScript, StandaloneSearchBox } from "@react-google-maps/api";
import { useRef, useState } from "react";
import { IconType } from "react-icons";

interface PlaceInputProps {
    Icon: IconType;
    onPlaceSelected: (locationData: {latitude: number; longitude: number; address: string}) => void;
}

const PlaceInput: React.FC<PlaceInputProps> = ({ Icon, onPlaceSelected }) => {
    const [inputValue, setInputValue] = useState("");
    const searchBoxRef = useRef<google.maps.places.SearchBox | null>(null);

    const onLoad = (searchBox: google.maps.places.SearchBox) => {
        searchBoxRef.current = searchBox;
    };

    const onPlacesChanged = () => {
        if (searchBoxRef.current) {
            const places = searchBoxRef.current.getPlaces();
            if (places && places.length > 0 && places[0].geometry && places[0].geometry.location && places[0].formatted_address) {
                const location = places[0].geometry.location;
                onPlaceSelected({
                    latitude: location.lat(),
                    longitude: location.lng(),
                    address: places[0].formatted_address
                });
                setInputValue(places[0].formatted_address);
            } else {
                console.error("No se encontró una dirección válida con coordenadas.");
            }
        } else {
            console.error("El searchBoxRef no está inicializado.");
        }
    };

    return (
        <LoadScript
            googleMapsApiKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY || ""}
            libraries={["places"]}
        >
            <StandaloneSearchBox
                onLoad={onLoad}
                onPlacesChanged={onPlacesChanged}
            >
                <label
                    htmlFor="placeInput"
                    className="flex items-center gap-2 border-neutral-500 border-2 rounded-md p-2 hover:cursor-text"
                >
                    {Icon && <Icon size={20} className="text-accent w-min" />}
                    <input
                        type="text"
                        id="placeInput"
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                        placeholder="Busca una ubicación"
                        className="rounded p-2 w-full outline-none font-medium flex-1"
                    />
                </label>
            </StandaloneSearchBox>
        </LoadScript>
    );
};

export default PlaceInput;