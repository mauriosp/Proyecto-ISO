import Modal from "../components/Modal";
import Navbar from "../components/Navbar";
import { useModalContext } from "../context/modal/ModalContext";
import { useUserContext } from "../context/user/UserContext";
import { useEffect } from "react";

const Home = () => {
  const { user } = useUserContext();
  const { isOpen, openModal } = useModalContext();

  useEffect(() => {
    if (!user?.isVerified) {
      openModal("verification");
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  
  return (
    <>
      <Navbar />
      {isOpen && (
        <div className="flex justify-center items-center bg-neutral-100">
          <div className="min-w-md">
            <Modal />
          </div>
        </div>
      )}
    </>
  );
};

export default Home;
