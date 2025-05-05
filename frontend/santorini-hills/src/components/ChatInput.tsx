import React, { useState } from "react";
import { FiSend } from "react-icons/fi";

interface ChatInputProps {
  onSend: (message: string) => void;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSend }) => {
  const [text, setText] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (text.trim()) {
      onSend(text.trim());
      setText("");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-2 shadow-inner flex gap-2 bg-white rounded-b-xl">
      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
        className="flex-1 shadow-md rounded-full px-4 py-2 text-sm focus:outline-none"
        placeholder="Escribe un mensaje..."
      />
      <button
        type="submit"
        className="bg-accent text-white p-2 rounded-full hover:bg-slate-800 flex items-center justify-center"
        aria-label="Enviar mensaje"
        >
        <FiSend className="h-4 w-4" />
        </button>

    </form>
  );
};

export default ChatInput;
