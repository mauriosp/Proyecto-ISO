import React, { useState } from "react";
import ChatInput from "./ChatInput";
import { ChatPreview } from "./ChatWindow";

interface ChatViewProps {
  chat: ChatPreview;
  onClose: () => void;
}

interface Message {
  id: string;
  text: string;
  fromUser: boolean;
  timestamp: Date;
}

const ChatView: React.FC<ChatViewProps> = ({ chat }) => {
  const [messages, setMessages] = useState<Message[]>([]);

  const handleSend = (text: string) => {
    const newMessage: Message = {
      id: Date.now().toString(),
      text,
      fromUser: true,
      timestamp: new Date(),
    };
    setMessages([...messages, newMessage]);
  };

  return (
    <div className="flex-1 flex flex-col bg-gray-50">
      {/* 🔹 Encabezado con nombre del chat */}
      <div className="p-4 shadow-sm bg-white font-semibold text-gray-800">
        {chat.name}
      </div>

      {/* 🔹 Contenedor de mensajes */}
      <div className="flex-1 overflow-y-auto p-4 space-y-2">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`max-w-[80%] px-3 py-2 rounded-lg ${
              msg.fromUser
                ? "bg-blue-500 text-white self-end"
                : "bg-gray-300 text-black self-start"
            }`}
          >
            <div>{msg.text}</div>
            <div className="text-xs opacity-70 mt-1 text-right">
              {msg.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
            </div>
          </div>
        ))}
      </div>

      {/* 🔹 Input de mensaje */}
      <ChatInput onSend={handleSend} />
    </div>
  );
};

export default ChatView;
