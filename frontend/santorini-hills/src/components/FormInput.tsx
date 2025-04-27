import { useState } from "react";
import PasswordToggle from "./PasswordToggle";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  ref?: React.Ref<HTMLInputElement>;
}

export const FormInput: React.FC<FormInputProps> = ({
  id,
  placeholder,
  ref,
  type: initialType,
  ...props
}) => {
  const [isPasswordVisible, setPasswordVisibility] = useState(
    initialType === "password" ? false : true
  );
  const handleToggle = (isVisible: boolean) => {
    setPasswordVisibility(isVisible);
  };
  return (
    <div className="relative w-full">
      <input
        {...props}
        id={id}
        ref={ref}
        type={isPasswordVisible ? "text" : initialType}
        placeholder={placeholder}
        className="font-medium text-sm py-3 px-3 w-full shadow-md rounded-xl border-accent border outline-none transition-all"
      />
      {initialType === "password" && <PasswordToggle onToggle={handleToggle} />}
    </div>
  );
};
