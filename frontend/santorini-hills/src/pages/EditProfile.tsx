import { useEffect } from "react";
import EditForm from "../components/ProfileSettings";
import { useModalContext } from "../context/modal/ModalContext";

const EditProfile = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("Edita tus datos personales");
  }, [setMessage]);
  return (
    <>
      <EditForm />
    </>
  );
};

export default EditProfile;
