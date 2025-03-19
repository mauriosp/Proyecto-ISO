import { IconType } from "react-icons";

interface Props {
  Icon: IconType;
  text: string;
  buttonStyles: string;
}

const SocialLoginButton: React.FC<Props> = ({Icon, text, buttonStyles }) => {
  return (
    <a
      className={`${buttonStyles} hover:bg-zinc-200 border-2 border-accent text-accent font-medium`}
      href="#"
    >
      <div className="flex items-center justify-center gap-3">
        <Icon size={32} />
        {text}
      </div>
    </a>
  );
};

export default SocialLoginButton;