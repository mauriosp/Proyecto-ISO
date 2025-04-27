import { useEffect } from "react";
import LoginForm from "../components/LoginForm";
import { useModalContext } from "../context/modal/ModalContext";

const Login = () => {
  const { setMessage } = useModalContext();
  useEffect(() => {
      setMessage("Inicia sesión en tu cuenta");
    }, [setMessage]);
  return (
    <>
      <LoginForm />
    </>
  );
};

export default Login;
