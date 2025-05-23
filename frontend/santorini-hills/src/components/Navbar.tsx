import { Link } from "react-router";
import { useLocation } from "react-router";
import name from "../assets/logo/Name.png";
import { useUserContext } from "../context/user/UserContext";
import AuthButtons from "./AuthButtons";
import UserButtons from "./ProfileButton";
import SearchBar from "./SearchBar";

const Navbar = () => {
  const { user } = useUserContext();
  const location = useLocation();
  const showSearchBar = location.pathname === "/";
  return (
    <nav className="w-full bg-white shadow">
      <div className="max-w-7xl mx-auto flex gap-10 justify-between items-center p-4">
        <Link to={""} className="w-1/3">
          <img src={name} alt="Name" className="w-full object-contain" />
        </Link>
        <div className="w-full">
          {showSearchBar && <SearchBar />}
        </div>
        <div className="w-1/4">
          {user === null ? <AuthButtons /> : <UserButtons />}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
