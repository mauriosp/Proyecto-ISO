import React from 'react';

interface PropertyFeaturesProps {
  bedrooms?: number;
  bathrooms?: number;
  area: number;
}

const PropertyFeatures: React.FC<PropertyFeaturesProps> = ({
  bedrooms,
  bathrooms,
  area
}) => {
  return (
    <div className="flex gap-2 items-center">
      {bedrooms && <p>{bedrooms} habitaciones</p>} •{' '}
      {bathrooms && <p>{bathrooms} baños</p>} • <p>{area} m²</p>
    </div>
  );
};

export default PropertyFeatures;