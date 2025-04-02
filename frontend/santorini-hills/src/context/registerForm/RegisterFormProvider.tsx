import { PropsWithChildren, useState } from "react";
import { RegisterContext } from "./RegisterContext";
import { User } from "../../models/user";

const RegisterFormProvider: React.FC<PropsWithChildren<object>> = ({
  children,
}) => {
  const [stage, setStage] = useState(0);
  const [user, setUser] = useState<User>({
    name: "",
    email: "",
    password: "",
    phone: "",
  });

  const setNextStage = (user: Partial<User>) => {
    setUser((prev) => {
      return {
        ...prev,
        ...user,
      };
    });
    setStage((prev) => prev + 1);
  };

  const setPrevStage = () => {
    setStage((prev) => prev - 1);
  };
  return (
    <RegisterContext.Provider
      value={{ stage, user, setUser, setNextStage, setPrevStage }}
    >
      {children}
    </RegisterContext.Provider>
  );
};

export default RegisterFormProvider;
