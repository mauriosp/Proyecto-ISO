import LoginForm from "../components/LoginForm";
import { useModalContext } from "../context/modal/ModalContext";

const Login = () => {
  const { setMessage } = useModalContext();
  setMessage("Inicia sesión")
  return (
    <>
      <LoginForm />
    </>
  );
};

export default Login;
