import { createContext, useContext } from "react";
import { User } from "../../models/user";

type registerFormContextType = {
  stage: number;
  user: User;
  setUser: (user: User) => void;
  setNextStage: (user: Partial<User>) => void;
  setPrevStage: (user: Partial<User>) => void;
};

export const RegisterContext = createContext<registerFormContextType>({
  stage: 0,
  user: {
    name: "",
    email: "",
    password: "",
    profile: "owner",
    phone: "",
  },
  setUser: () => {},
  setNextStage: () => {},
  setPrevStage: () => {},
});

export const useRegisterContext = () => {
  const context = useContext(RegisterContext);
  if (!context) {
    throw new Error("useRegisterContext must be used within a RegisterProvider");
  }
  return context;
};
