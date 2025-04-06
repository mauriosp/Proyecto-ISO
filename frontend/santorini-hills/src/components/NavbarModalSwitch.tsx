import { useEffect, useRef, useState } from "react";
import { IconType } from "react-icons";

interface NavbarModalSwitchProps {
  Icon: IconType;
  children: React.ReactNode;
}

const NavbarModalSwitch = ({ Icon, children }: NavbarModalSwitchProps) => {
    const [isOpen, setIsOpen] = useState(false);
    const wrapperRef = useRef<HTMLDivElement>(null);
  
    useEffect(() => {
      function handleClickOutside(e: MouseEvent) {
        if (wrapperRef.current && !wrapperRef.current.contains(e.target as Node)) {
          setIsOpen(false);
        }
      }
      document.addEventListener("mousedown", handleClickOutside);
      return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);
  
    return (
      <div className="relative hover:bg-black/10 rounded-md transition-all" ref={wrapperRef}>
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="flex items-center hover:cursor-pointer p-3 gap-2"
        >
          <Icon size={20}/>
        </button>
        {isOpen && (
          <div className="absolute w-56 max-h-80 overflow-y-auto top-full right-0 mt-2 bg-white rounded shadow-lg border-t-4 border-accent p-2">
            {children}
          </div>
        )}
      </div>
    );
  };

  export default NavbarModalSwitch;
