import { LoadScript, Autocomplete } from "@react-google-maps/api";
import { useRef, useState } from "react";
import { IconType } from "react-icons";

interface PlaceInputProps {
    Icon: IconType;
    onPlaceSelected: (address: string) => void;
}

const PlaceInput: React.FC<PlaceInputProps> = ({ Icon, onPlaceSelected }) => {
    const [inputValue, setInputValue] = useState("");
    const autocompleteRef = useRef<google.maps.places.Autocomplete | null>(null);

    const onLoad = (autocomplete: google.maps.places.Autocomplete) => {
        autocompleteRef.current = autocomplete;
    };

    const onPlaceChange = () => {
        if (autocompleteRef.current) {
            const place = autocompleteRef.current.getPlace();
            if (place && place.formatted_address) {
                onPlaceSelected(place.formatted_address);
            } else {
                console.error("No se encontró una dirección válida.");
            }
        } else {
            console.error("El autocompleteRef no está inicializado.");
        }
    };

    return (
        <LoadScript
            googleMapsApiKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY || ""}
            libraries={["places"]}
        >
            <Autocomplete
                onLoad={onLoad}
                onPlaceChanged={onPlaceChange}
                options={{ types: ['address'] }}
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
            </Autocomplete>
        </LoadScript>
    );
};

export default PlaceInput;