import { useEffect, useState } from "react";
import { UserContext } from "./UserContext";
import { User } from "../../models/user";

const LOCAL_STORAGE_KEY = "loggedUser";

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null);

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const ownerUser: User = {
    id: "6818e6d9e5af3367a7c74c13",
    name: "Juan Pérez",
    email: "juanperez@email.com",
    password: null,
    phone: "+573001112233",
    profile: "owner",
    isVerified: true,
  };

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const renterUser: User = {
    id: "6823ed3bc8684975706e4a7e",
    name: "Santiago Si",
    email: "santiago.colonia@uao.edu.co",
    password: null,
    phone: "+573001112244",
    profile: "renter",
    isVerified: true,
  };

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const adminUser: User = {
    id: "2",
    name: "Admin",
    email: "admin@santorini.com",
    password: null,
    phone: "+573001112233",
    profile: "admin",
    isVerified: true,
  };

  useEffect(() => {
    const storedUser = localStorage.getItem(LOCAL_STORAGE_KEY);
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    } else {
      // Usuario por defecto si no hay nada guardado (solo para pruebas)

      // Solo para pruebas (owner)
      //setUser(ownerUser);

      // Solo para pruebas (renter)
      setUser(renterUser);
      
      // Solo para pruebas (admin)
      //setUser(adminUser);

      // No logeado por defecto
      //setUser(null);
    }
  }, []);
  /*
    useEffect(() => {
        const storedUser = localStorage.getItem(LOCAL_STORAGE_KEY);
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);*/

  useEffect(() => {
    if (user) {
      localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(LOCAL_STORAGE_KEY);
    }
  }, [user]);

  const logout = () => {
    setUser(null);
    window.location.href = "/";
  };

  return (
    <UserContext.Provider value={{ user, setUser, logout }}>
      {children}
    </UserContext.Provider>
  );
};
