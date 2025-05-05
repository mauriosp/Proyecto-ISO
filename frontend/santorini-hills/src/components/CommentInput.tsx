import React, { useState } from "react";
import { FaStar } from "react-icons/fa";

interface CommentInputProps {
  onAddComment: (text: string, rating: number) => void;
}

const CommentInput: React.FC<CommentInputProps> = ({ onAddComment }) => {
  const [text, setText] = useState("");
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (text.trim() && rating > 0) {
      onAddComment(text.trim(), rating);
      setText("");
      setRating(0);
      setHoverRating(0);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3 mt-4">
      <div className="flex gap-1">
        {[1, 2, 3, 4, 5].map((star) => (
          <button
            key={star}
            type="button"
            onClick={() => setRating(star)}
            onMouseEnter={() => setHoverRating(star)}
            onMouseLeave={() => setHoverRating(0)}
            className="focus:outline-none"
          >
            <FaStar
              className={`w-6 h-6 transition-colors ${
                (hoverRating || rating) >= star
                  ? "text-yellow-400"
                  : "text-gray-300"
              }`}
            />
          </button>
        ))}
      </div>

      <textarea
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Escribe tu comentario..."
        className="p-2 rounded border border-gray-300 bg-white text-sm resize-none"
        rows={3}
      />

      <button
        type="submit"
        disabled={rating === 0}
        className="self-end bg-accent text-white px-4 py-2 rounded font-medium hover:bg-accent/90 disabled:opacity-50"
      >
        Comentar
      </button>
    </form>
  );
};

export default CommentInput;
