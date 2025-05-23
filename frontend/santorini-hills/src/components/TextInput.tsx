type TextInputProps = {
    id: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    icon?: React.ElementType;
    type?: string;
    className?: string;
    onKeyDown?: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  };

const TextInput = ({ 
    id, 
    value, 
    onChange, 
    placeholder, 
    icon: Icon 
}: TextInputProps) => (
    <label
        htmlFor={id}
        className="flex min-w-xl items-center gap-2 border-neutral-500 border-2 rounded-md p-2 hover:cursor-text"
    >
        {Icon && <Icon size={20} className="text-accent w-min" />}
        <input
            id={id}
            type="text"
            onChange={onChange}
            className="outline-none font-medium flex-1"
            value={value}
            placeholder={placeholder}
            autoComplete="off"
        />
    </label>
);

export default TextInput;