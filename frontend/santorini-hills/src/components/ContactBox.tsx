import React from 'react';
import { formatNumber } from '../utils/parseNumbers';
import KeyPoints from './KeyPoints';

interface ContactBoxProps {
  price: number;
  keypoints?: string[];
}

const ContactBox: React.FC<ContactBoxProps> = ({ price, keypoints = [] }) => {
  

  return (
    <section className="bg-white rounded-xl shadow p-8 flex flex-col gap-2">
      <h3 className="text-4xl font-semibold">
        ${formatNumber(price)} <span className="text-base">/mes</span>
      </h3>
      {keypoints && <KeyPoints points={keypoints} />}
      <div className="font-medium flex flex-col gap-2 mt-3">
        <p>¿Te interesa este inmueble?</p>
        <button className="form-button bg-accent text-white hover:bg-slate-800 font-semibold">
          Contactar propietario
        </button>
      </div>
    </section>
  );
};

export default ContactBox;