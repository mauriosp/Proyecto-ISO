import React, { useState } from "react";
import ChatBubble from "./ChatBubble";
import ChatWindow, { ChatPreview } from "./ChatWindow";

const ChatSystem: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [activeChat, setActiveChat] = useState<ChatPreview | null>(null);

  const chats: ChatPreview[] = [
    { id: "1", name: "Juan Pérez", lastMessage: "Hola, ¿todo bien?" },
    { id: "2", name: "Maria Ruiz", lastMessage: "Nos vemos mañana." },
  ];

  return (
    <>
      <ChatBubble onClick={() => setIsOpen(!isOpen)} />
      <ChatWindow
        isOpen={isOpen}
        chats={chats}
        activeChat={activeChat}
        onSelectChat={setActiveChat}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
};

export default ChatSystem;
