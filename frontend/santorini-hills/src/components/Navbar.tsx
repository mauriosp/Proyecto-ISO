import { Link } from "react-router";
import { useUserContext } from "../context/user/UserContext";
import AuthButtons from "./AuthButtons";
import UserButtons from "./ProfileButton";
import SearchBar from "./SearchBar";

const Navbar = () => {
  const { user } = useUserContext();
  return (
    <nav className="flex gap-10 justify-between items-center p-4 max-w-7xl mx-auto">
      <Link to={""} className="w-1/5">
        <h1 className="">Santorini Hills</h1>
      </Link>
      <div className="w-full">
        <SearchBar />
      </div>
      <div className="w-1/4">
        {user === null ? <AuthButtons /> : <UserButtons />}
      </div>
    </nav>
  );
};

export default Navbar;
