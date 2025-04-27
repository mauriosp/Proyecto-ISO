import { FaMinus, FaPlus } from "react-icons/fa6";

interface NumberInputWithButtonsProps {
    value: number;
    onChange: (event: { target: { value: string } }) => void;
    min?: number;
    id?: string;
}

const NumberInputWithButtons = ({ 
    value, 
    onChange, 
    min = 0, 
    id = "" 
}: NumberInputWithButtonsProps) => {
        const handleIncrement = () => {
                onChange({ target: { value: (value + 1).toString() } });
        };

        const handleDecrement = () => {
                if (value > min) {
                        onChange({ target: { value: (value - 1).toString() } });
                }
        };

        return (
                <div className="flex items-center gap-3">
                        <button
                                type="button"
                                onClick={handleDecrement}
                                disabled={value <= min}
                                className={`hover:cursor-pointer hover:bg-accent hover:text-white aspect-square border-2 border-accent rounded-full p-2 transition-all ${value <= min ? 'opacity-50 cursor-not-allowed' : ''}`}
                        >
                                <FaMinus />
                        </button>
                        <input
                                id={id}
                                type="number"
                                min={min}
                                value={value}
                                onChange={onChange}
                                className="outline-none font-medium border-accent border-2 rounded-md p-2 w-12 text-center"
                        />
                        <button
                                type="button"
                                onClick={handleIncrement}
                                className="hover:cursor-pointer hover:bg-accent hover:text-white aspect-square border-2 border-accent rounded-full p-2 transition-all"
                        >
                                <FaPlus />
                        </button>
                </div>
        );
};

export default NumberInputWithButtons;