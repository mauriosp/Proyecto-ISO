interface StageContainerProps {
  children: React.ReactNode;
  title: string;
  subtitle?: React.ReactNode;
}

const StageContainer = ({ children, title, subtitle }: StageContainerProps) => (
  <div className="flex flex-col w-10/12 gap-10">
    <div className="text-center">
      <h2 className="text-2xl font-semibold">{title}</h2>
      {subtitle && <p>{subtitle}</p>}
    </div>
    {children}
  </div>
);

export default StageContainer;
