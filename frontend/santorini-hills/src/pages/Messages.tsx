import { useEffect, useState } from "react";
import MessagesList from "../components/MessagesList";
import { useModalContext } from "../context/modal/ModalContext";

const Messages = () => {
  const { setMessage } = useModalContext();
  const [mensajes, setMensajes] = useState([
    {
      idUsuario: "123",
      contenido: "Hola, ¿este producto sigue disponible?",
      fechaMensaje: new Date().toISOString(),
      estadoMensaje: false,
    },
  ]);

  useEffect(() => {
    setMessage("Mensajes recibidos");
  }, [setMessage]);

  const handleResponder = (index: number, respuesta: string) => {
    const nuevos = [...mensajes];
    nuevos[index].respuestaMensaje = {
      contenido: respuesta,
      fechaRespuesta: new Date().toISOString(),
    };
    nuevos[index].estadoMensaje = true;
    setMensajes(nuevos);
  };

  return (
    <>
      <MessagesList mensajes={mensajes} onResponder={handleResponder} />
    </>
  );
};

export default Messages;
