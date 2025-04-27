import { useRef, useState } from "react";

const VerificationForm = () => {
  const [digits, setDigits] = useState(["", "", "", "", "", ""]);
  const inputsRef = useRef<HTMLInputElement[]>([]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number
  ) => {
    const { value } = e.target;
    if (/^[0-9]?$/.test(value)) {
      const newDigits = [...digits];
      const firstEmptyIndex = newDigits.findIndex((d) => d === "");
      if (firstEmptyIndex !== -1) {
        newDigits[firstEmptyIndex] = value;
        setDigits(newDigits);
        if (firstEmptyIndex < 5) {
          inputsRef.current[firstEmptyIndex + 1].focus();
        }
      }
      if (index !== firstEmptyIndex) e.target.value = "";
    }
  };

  const handleKeyDown = (
    e: React.KeyboardEvent<HTMLInputElement>,
    index: number
  ) => {
    if (e.key === "Backspace") {
      e.preventDefault();
      if (digits[index]) {
        const newDigits = [...digits];
        newDigits[index] = "";
        setDigits(newDigits);
      } else if (index > 0) {
        inputsRef.current[index - 1].focus();
      }
    }
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const code = digits.join("");
    if (code.length === 6) {
      console.log("Código enviado:", code);
      // Espacio para enviar el código a la API
    } else {
      console.error("Código inválido. Debe tener 6 dígitos.");
    }
  };
  return (
    <form className="flex flex-col space-y-2" onSubmit={handleSubmit}>
      <h5 className="text-center w-full">Escribe el código de 6 dígitos que enviamos a tu correo</h5>
      <div className="flex justify-center space-x-2 mt-2">
        {digits.map((digit, i) => (
          <input
            key={i}
            ref={(el) => {
              if (el) inputsRef.current[i] = el;
            }}
            className="font-medium text-sm py-3 px-3 aspect-square w-11 shadow-md rounded-xl border-accent border outline-none text-center"
            type="text"
            maxLength={1}
            value={digit}
            onChange={(e) => handleChange(e, i)}
            onKeyDown={(e) => handleKeyDown(e, i)}
          />
        ))}
      </div>
      <div className="text-center mt-2 text-sm text-neutral-700">
        ¿No recibiste ningún correo?{" "}
        <a className="font-medium text-accent transition-all" href="#">
          Haz click aquí para volver a enviar el correo
        </a>
      </div>
      <button
        type="submit"
        className="form-button hover:bg-slate-800 bg-accent text-white font-semibold"
      >
        Confirmar
      </button>
    </form>
  );
};

export default VerificationForm;