const ProfileOption: React.FC<React.PropsWithChildren<React.InputHTMLAttributes<HTMLInputElement>>> = (
  { children, ...inputProps }
) => {
  return (
    <div>
      <input
        {...inputProps}
        children={undefined}
        className="hidden peer"
        type="radio"
      />
      <label className="radius-button" htmlFor={inputProps.id}>
        {children}
      </label>
    </div>
  );
};

export default ProfileOption;
