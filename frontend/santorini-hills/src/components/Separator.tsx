import React from "react";

interface SeparatorProps {
  label?: string;
}

const Separator: React.FC<SeparatorProps> = ({ label }) => {
  return (
    <div className="flex items-center">
      <div className="flex-grow bg-neutral-400 h-0.5"></div>
      {label && <span className="px-3 bg-white text-neutral-700">{label}</span>}
      <div className="flex-grow bg-neutral-400 h-0.5"></div>
    </div>
  );
};

export default Separator;