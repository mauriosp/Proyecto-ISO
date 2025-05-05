import { useEffect } from "react";
import ReportForm from "../components/ReportForm";
import { useModalContext } from "../context/modal/ModalContext";

const Report = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("Reportar Aviso");
  }, [setMessage]);
  return (
    <>
      <ReportForm />
    </>
  );
};

export default Report;
