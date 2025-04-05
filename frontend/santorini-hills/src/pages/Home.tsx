import Modal from "../components/Modal";
import Navbar from "../components/Navbar";
import { useModalContext } from "../context/modal/ModalContext";

const Home = () => {
  const {isOpen} = useModalContext();
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
