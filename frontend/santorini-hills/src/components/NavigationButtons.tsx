interface NavigationButtonsProps {
  onBack?: () => void;
  canContinue: boolean;
}

const NavigationButtons = ({ 
  onBack, 
  canContinue 
}: NavigationButtonsProps) => (
  <div className={`flex ${onBack ? "justify-between" : "justify-end"} w-full absolute bottom-10 left-0 px-10`}>
    {onBack && (
      <button
        onClick={onBack}
        className="form-button max-w-40 hover:bg-black/10 text-accent border-2 border-accent font-semibold"
      >
        Atrás
      </button>
    )}
    {canContinue && (
      <button
        type="submit"
        className="form-button max-w-40 hover:bg-slate-800 bg-accent text-white font-semibold"
      >
        Siguiente
      </button>
    )}
  </div>
);

export default NavigationButtons;