import { useEffect } from "react";
import MessageForm from "../components/MessageForm";
import { useModalContext } from "../context/modal/ModalContext";

const Message = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
    setMessage("Contactar con el usuario");
  }, [setMessage]);
  return (
    <>
      <MessageForm />
    </>
  );
};

export default Message;
