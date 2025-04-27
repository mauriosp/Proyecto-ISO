import React from 'react';

interface KeyPointsProps {
  points: string[];
}

const KeyPoints: React.FC<KeyPointsProps> = ({ points }) => {
  return (
    <div>
      <h4 className="font-medium">Puntos clave:</h4>
      <ul className="list-disc pl-4">
        {points.map((point, index) => (
          <li key={index}>{point}</li>
        ))}
      </ul>
    </div>
  );
};

export default KeyPoints;