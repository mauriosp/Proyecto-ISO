import React from "react";
import ChatList from "./ChatList";
import ChatView from "./ChatView";

interface ChatWindowProps {
  isOpen: boolean;
  chats: ChatPreview[];
  activeChat: ChatPreview | null;
  onSelectChat: (chat: ChatPreview) => void;
  onClose: () => void;
}

export interface ChatPreview {
  id: string;
  name: string;
  lastMessage: string;
}

const ChatWindow: React.FC<ChatWindowProps> = ({
  isOpen,
  chats,
  activeChat,
  onSelectChat,
  onClose,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed bottom-20 right-4 w-[400px] h-[500px] bg-white rounded-xl shadow-2xl flex overflow-hidden z-50">
      {/* Lista de chats a la izquierda */}
      <div className="w-[140px] h-full border-r border-gray-200 shadow-md overflow-y-auto">
        <ChatList chats={chats} onSelect={onSelectChat} />
      </div>

      {/* Chat abierto o mensaje de selección */}
      <div className="flex-1 h-full flex flex-col">
        {activeChat ? (
          <ChatView chat={activeChat} onClose={onClose} />
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-400">
            Selecciona un chat
          </div>
        )}
      </div>
    </div>
  );
};

export default ChatWindow;
