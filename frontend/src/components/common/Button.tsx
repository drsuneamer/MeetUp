import React from 'react';

type ButtonProps = React.HTMLProps<HTMLButtonElement> & {
  type?: 'button' | 'submit' | 'reset';
  className?: string;
};

const Button = ({
  type = 'button',
  className = '',
  children,
  ...props
}: ButtonProps) => {
  return (
    <button type={type} className={` ${className}`} {...props}>
      {children}
    </button>
  );
};

export default React.memo(Button);