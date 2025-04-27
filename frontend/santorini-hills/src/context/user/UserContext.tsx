import { createContext, useContext } from "react";
import { User } from "../../models/user";

// filepath: c:\Users\juanj\OneDrive\Escritorio\Proyecto-ISO\frontend\santorini-hills\src\context\user\UserContext.tsx

type UserContextType = {
    user: User | null;
    setUser: (user: User | null) => void;
    logout: () => void;
};

export const UserContext = createContext<UserContextType>({
    user: null,
    setUser: () => {},
    logout: () => {},
});

export const useUserContext = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUserContext must be used within a UserProvider");
    }
    return context;
};