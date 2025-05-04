import React from 'react';
import { formatNumber } from '../utils/parseNumbers';
import KeyPoints from './KeyPoints';
import { ModalView, useModalContext } from "../context/modal/ModalContext";

interface ContactBoxProps {
  price: number;
}

const ContactBox: React.FC<ContactBoxProps> = ({ price }) => {
  const keyPoints = [
    'Administración incluida',
    'Parqueadero',
    'Zona BBQ'
  ];
  const { openModal } = useModalContext();
  const handleClick = (view:ModalView) => {
    openModal(view);
  };
  return (
    <section className="bg-white rounded-xl shadow p-8 flex flex-col gap-2">
      <h3 className="text-4xl font-semibold">
        ${formatNumber(price)} <span className="text-base">/mes</span>
      </h3>
      <KeyPoints points={keyPoints} />
      <div className="font-medium flex flex-col gap-2 mt-3">
        <p>¿Te interesa este inmueble?</p>
        <button className="form-button bg-accent text-white hover:bg-slate-800 font-semibold">
          Contactar propietario
        </button>
        <button onClick={()=>handleClick("report")} className="form-button bg-red-700 text-white hover:bg-red-800 font-semibold">
          Reportar aviso
        </button>
      </div>
    </section>
  );
};

export default ContactBox;