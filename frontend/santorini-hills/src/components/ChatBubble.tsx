import React from "react";
import { MessageSquare } from "lucide-react";

interface ChatBubbleProps {
  onClick: () => void;
}

const ChatBubble: React.FC<ChatBubbleProps> = ({ onClick }) => (
  <button
    onClick={onClick}
    className="fixed bottom-4 right-4 bg-blue-600 hover:bg-blue-700 text-white rounded-full p-4 shadow-xl z-50"
  >
    <MessageSquare size={24} />
  </button>
);

export default ChatBubble;
