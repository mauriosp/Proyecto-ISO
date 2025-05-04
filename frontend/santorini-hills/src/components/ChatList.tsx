import React from "react";
import { ChatPreview } from "./ChatWindow";

interface ChatListProps {
  chats: ChatPreview[];
  onSelect: (chat: ChatPreview) => void;
}

const ChatList: React.FC<ChatListProps> = ({ chats, onSelect }) => {
    return (
      <div className="flex flex-col h-full overflow-y-auto">
        {chats.map((chat) => (
          <div
            key={chat.id}
            className="p-4 hover:bg-gray-100 cursor-pointer shadow-sm"
            onClick={() => onSelect(chat)}
          >
            <div className="font-semibold">{chat.name}</div>
            <div className="text-sm text-gray-500 truncate">{chat.lastMessage}</div>
          </div>
        ))}
      </div>
    );
  };
  

export default ChatList;
