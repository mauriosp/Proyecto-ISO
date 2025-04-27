import RegisterForm from "../components/RegisterForm";
import RegisterFormProvider from "../context/registerForm/RegisterFormProvider";

const Register = () => {
  return (
    <>
      <RegisterFormProvider>
        <RegisterForm />
      </RegisterFormProvider>
    </>
  );
};

export default Register;
