import { useState } from "react";
import { UserContext } from "./UserContext";
import { User } from "../../models/user";

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const defaultUser : User = {
        name: "John Doe",
        email: "johndoe@email.com",
        password: "password",
        phone: "+571234567890",
        profile: "owner",
        isVerified: true,
    }
    const [user, setUser] = useState<User | null>(defaultUser);
    const logout = () => {
        setUser(null);
    };

    return (
        <UserContext.Provider value={{ user, setUser, logout }}>
            {children}
        </UserContext.Provider>
    );
};