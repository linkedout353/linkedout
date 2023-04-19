import React, { useState, useEffect, useRef } from "react";

const ApplyJob = ({ title, company, description, startDate, endDate }) => {
  const [isOpen, setIsOpen] = useState(false);
  const popupRef = useRef(null);

  const handleOpen = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  useEffect(() => {
    const handleOutsideClick = (event) => {
      if (popupRef.current && !popupRef.current.contains(event.target)) {
        handleClose();
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleOutsideClick);
    }

    return () => {
      document.removeEventListener("mousedown", handleOutsideClick);
    };
  }, [isOpen]);

  return (
    <div className="">
      <button
        className="bg-linkedout text-white px-4 py-2 rounded-2xl hover:bg-white hover:text-linkedout hover:ring-1 ring-inset hover:ring-linkedout"
        onClick={() => handleOpen()}
      >
        Apply
      </button>

      {isOpen && (
        <div className="fixed top-0 left-0 h-full w-full bg-black bg-opacity-50 flex justify-center items-center">
          <div ref={popupRef} className="bg-white w-96 p-6 rounded-2xl">
            <div className="flex justify-between"></div>
            {/* Form content goes here */}
            <div className="flex flex-col gap-6">
              <div>
                <h2 className="text-lg font-bold">Success!</h2>
              </div>
              <div>
                <h2 className="text-lg">
                  Your profile and resume was automatically shared with the
                  recruiter.
                </h2>
              </div>
              <button
                className="bg-linkedout ml-auto text-white p-2 rounded-2xl hover:bg-white hover:text-linkedout hover:ring-1 ring-inset hover:ring-linkedout"
                onClick={() => handleClose()}
              >
                Close
              </button>
            </div>
            <button
              className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
              onClick={handleClose}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-6 w-6"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M12.72 7.469l-5.469 5.469a.75.75 0 11-1.061-1.061l5.469-5.469a.75.75 0 111.061 1.061zM7.469 12.72L12.72 7.47a.75.75 0 111.061 1.061l-5.469 5.469a.75.75 0 11-1.061-1.06l5.469-5.47-5.469 5.47a.75.75 0 01-1.061 0z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ApplyJob;
