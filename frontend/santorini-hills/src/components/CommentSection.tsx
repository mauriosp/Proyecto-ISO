import React from 'react';

interface Comment {
  id: number;
  text: string;
  author: string;
}

interface CommentsSectionProps {
  comments?: Comment[];
}

const CommentsSection: React.FC<CommentsSectionProps> = ({ comments = [] }) => {
  return (
    <section className="bg-white rounded-xl shadow p-8 flex flex-col gap-2">
      <h3 className="text-2xl font-medium">Comentarios</h3>
      <div className="p-4">
        {comments.length > 0 ? (
          comments.map(comment => (
            <div key={comment.id} className="mb-2">
              <p className="font-medium">{comment.author}</p>
              <p>{comment.text}</p>
            </div>
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