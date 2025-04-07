import { useEffect, useState } from "react";
import { UserContext } from "./UserContext";
import { User } from "../../models/user";

const LOCAL_STORAGE_KEY = "loggedUser";

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    
    useEffect(() => {
        const storedUser = localStorage.getItem(LOCAL_STORAGE_KEY);
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        } else {
            // Usuario por defecto si no hay nada guardado (solo para pruebas)
            setUser({
                name: "Juan Pérez",
                email: "juanperez@email.com",
                password: "12345678",
                phone: "+573001112233",
                profile: "owner",
                isVerified: true,
            });
        }
    }, []);

    
    useEffect(() => {
        if (user) {
            localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(user));
        } else {
            localStorage.removeItem(LOCAL_STORAGE_KEY);
        }
    }, [user]);

    const logout = () => {
        setUser(null);
    };

    return (
        <UserContext.Provider value={{ user, setUser, logout }}>
            {children}
        </UserContext.Provider>
    );
};
