import React, { useState } from "react";
import CommentInput from "./CommentInput";
import Comment from "./Comment";
import { useUserContext } from "../context/user/UserContext"; // ajusta esta ruta si es necesario

interface CommentData {
  id: number;
  author: string;
  text: string;
  rating: number;
  date: string;
  avatarUrl?: string;
}

interface CommentsSectionProps {
  initialComments?: CommentData[];
}

const CommentsSection: React.FC<CommentsSectionProps> = ({ initialComments = [] }) => {
  const { user } = useUserContext();
  const [comments, setComments] = useState<CommentData[]>(initialComments);

  const handleAddComment = (text: string, rating: number) => {
    if (!user) return; // Opcional: podrías manejar el caso donde no haya usuario autenticado

    const newComment: CommentData = {
      id: Date.now(),
      author: user.name,
      text,
      rating,
      date: new Date().toLocaleDateString(),
      avatarUrl: user.photo || undefined,
    };
    setComments([newComment, ...comments]);
  };

  return (
    <section className="bg-white rounded-xl shadow p-8 flex flex-col gap-4">
      <h3 className="text-2xl font-medium">Comentarios</h3>
      {user ? (
        <CommentInput onAddComment={handleAddComment} />
      ) : (
        <p className="text-sm text-gray-600">Inicia sesión para dejar un comentario.</p>
      )}
      <div className="mt-4">
        {comments.length > 0 ? (
          comments.map((comment) => (
            <Comment
              key={comment.id}
              author={comment.author}
              text={comment.text}
              rating={comment.rating}
              date={comment.date}
              avatarUrl={comment.avatarUrl}
            />
          ))
        ) : (
          <p className="text-sm text-center text-neutral-600">
            No hay comentarios aún
          </p>
        )}
      </div>
    </section>
  );
};

export default CommentsSection;
