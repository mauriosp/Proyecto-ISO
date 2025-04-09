import { Link } from "react-router";
import { useUserContext } from "../context/user/UserContext";
import AuthButtons from "./AuthButtons";
import UserButtons from "./ProfileButton";
import SearchBar from "./SearchBar";
import logo from "../assets/logo/Logo.png";
import name from "../assets/logo/Name.png";

const Navbar = () => {
  const { user } = useUserContext();
  return (
    <nav className="w-full bg-white shadow">
      <div className="max-w-7xl mx-auto flex gap-10 justify-between items-center p-4">
        <Link to={""} className="w-auto">
          <div className="flex items-center -ml-45">
            <img src={logo} alt="Logo" className="w-15 h-15 object-contain" />
            <img src={name} alt="Name" className="h-8 object-contain" />
          </div>
        </Link>
        <div className="w-full">
          <SearchBar />
        </div>
        <div className="w-1/4">
          {user === null ? <AuthButtons /> : <UserButtons />}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
