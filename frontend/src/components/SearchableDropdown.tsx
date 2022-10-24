import React, { useEffect, useRef, useState } from "react";

const SearchableDropdown = ({
    options,
    label,
    id,
    selectedVal,
    handleChange,
  }: {
    options: any,
    label: string;
    id: number;
    name: string;
    selectedVal: any;
    handleChange: any;
    toggle?: void;
  }) => {
    const [query, setQuery] = useState("");
    const [isOpen, setIsOpen] = useState(false);
  
    const inputRef = useRef(null);
  
    useEffect(() => {
      document.addEventListener("click", toggle);
      return () => document.removeEventListener("click", toggle);
    }, []);
  
    const selectOption = (option:any) => {
      setQuery(() => "");
      handleChange(option[label]);
      setIsOpen((isOpen) => !isOpen);
    };
  
    function toggle(e:React.MouseEvent<HTMLInputElement> | any) {
      setIsOpen(e && e.target === inputRef.current);
    }
  
    const getDisplayValue = () => {
      if (query) return query;
      if (selectedVal) return selectedVal;
  
      return "";
    };
  
    const filter = (options:any) => {
      return options.filter(
        (option:any) => option[label].toLowerCase().indexOf(query.toLowerCase()) > -1
      );
    };
  
function SearchableDropdown():any {
    return (
      <div className="SearchableDropdown">
        <h1>SearchableDropdown</h1>
        <div className="dropdown">
            <div className="control">
                <div className="selected-value">
                <input
                    ref={inputRef}
                    type="text"
                    value={getDisplayValue()}
                    name="searchTerm"
                    onChange={(e) => {
                    setQuery(e.target.value);
                    handleChange(null);
                    }}
                    onClick={toggle}
                />
                </div>
                <div className={`arrow ${isOpen ? "open" : ""}`}></div>
            </div>

            <div className={`options ${isOpen ? "open" : ""}`}>
                {filter(options).map((option:any, index:number) => {
                return (
                    <div
                    onClick={() => selectOption(option)}
                    className={`option ${
                        option[label] === selectedVal ? "selected" : ""
                    }`}
                    key={`${id}-${index}`}
                    >
                    {option[label]}
                    </div>
                );
                })}
            </div>
        </div>
      </div>
    );
    }
  }
  export default SearchableDropdown;