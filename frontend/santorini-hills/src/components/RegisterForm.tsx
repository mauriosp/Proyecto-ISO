import { useRegisterContext } from "../context/registerForm/RegisterContext";
import {
  EmailStage,
  PasswordStage,
  PersonalInfoStage,
  ProfileStage,
} from "./RegisterFormStages";

const RegisterForm = () => {
  const { stage: currentStage } = useRegisterContext();
  const formStages = [
    EmailStage,
    PasswordStage,
    PersonalInfoStage,
    ProfileStage,
  ];
  const CurrentStageComponent = formStages[currentStage];
  return (
    <>
      <CurrentStageComponent />
    </>
  );
};

export default RegisterForm;
