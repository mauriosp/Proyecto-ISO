import { useEffect } from "react";
import VerificationForm from "../components/VerificationForm";
import { useModalContext } from "../context/modal/ModalContext";

const Verification = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("Verifica tu cuenta");
  }, [setMessage]);
  return (
    <>
      <VerificationForm />
    </>
  );
};

export default Verification;
