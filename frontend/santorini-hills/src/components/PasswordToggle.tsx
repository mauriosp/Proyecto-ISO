import { TbEye as Eye, TbEyeClosed as EyeClosed} from "react-icons/tb";
import { useState } from "react";

interface Props {
  onToggle: (isVisible: boolean) => void;
}

const PasswordToggle: React.FC<Props> = ({ onToggle }) => {
  const [isVisible, setIsVisible] = useState(false);
  const toggleVisibility = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    const newVisibility = !isVisible;
    setIsVisible(newVisibility);
    onToggle(newVisibility);
  };
  const hidePasswordButtonStyle =
    "hover:text-neutral-700 transition-all h-6 w-6";

  return (
    <button
      className="hover:cursor-pointer absolute right-3 translate-y-1/2 top-0"
      onClick={toggleVisibility}
    >
      {isVisible ? (
        <Eye className={`${hidePasswordButtonStyle} text-accent`} />
      ) : (
        <EyeClosed className={`${hidePasswordButtonStyle} text-neutral-500`} />
      )}
    </button>
  );
};

export default PasswordToggle;
