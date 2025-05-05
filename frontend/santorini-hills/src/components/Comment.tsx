import React from "react";
import { FaStar, FaRegStar, FaStarHalfAlt } from "react-icons/fa";

interface CommentCardProps {
  author: string;
  text: string;
  rating: number;
  date: string;
  avatarUrl?: string;
}

const Comment: React.FC<CommentCardProps> = ({
  author,
  text,
  rating,
  date,
  avatarUrl = "/default-avatar.png", // usa un avatar por defecto si no hay imagen
}) => {
  const renderStars = () => {
    const stars = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;

    for (let i = 0; i < fullStars; i++) {
      stars.push(<FaStar key={`star-full-${i}`} className="text-yellow-400" />);
    }
    if (hasHalfStar) {
      stars.push(<FaStarHalfAlt key="star-half" className="text-yellow-400" />);
    }
    while (stars.length < 5) {
      stars.push(<FaRegStar key={`star-empty-${stars.length}`} className="text-gray-300" />);
    }
    return stars;
  };

  return (
    <div className="flex gap-4 items-start mb-4">
      <img
        src={avatarUrl}
        alt={`${author} avatar`}
        className="w-10 h-10 rounded-full object-cover"
      />
      <div>
        <p className="font-semibold">{author}</p>
        <p className="text-sm text-gray-500">{date}</p>
        <div className="flex gap-1 mt-1">{renderStars()}</div>
        <p className="mt-2">{text}</p>
      </div>
    </div>
  );
};

export default Comment;
