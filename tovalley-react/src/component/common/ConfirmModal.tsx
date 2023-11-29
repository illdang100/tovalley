import React, { FC, useEffect } from "react";
import styles from "../../css/common/ConfirmModal.module.css";

interface Props {
  content: string;
  handleModal?: React.Dispatch<
    React.SetStateAction<{
      view: boolean;
      content: string;
    }>
  >;
}

const ConfirmModal: FC<Props> = ({ content, handleModal }) => {
  useEffect(() => {
    document.body.style.cssText = `
          position: fixed; 
          top: -${window.scrollY}px;
          overflow-y: scroll;
          width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = "";
      window.scrollTo(0, parseInt(scrollY || "0", 10) * -1);
    };
  }, []);

  return (
    <div className={styles.modalContainer}>
      <div className={styles.modalBox}>
        <div className={styles.modalContent}>
          <span>{content}</span>
        </div>
        <div
          className={styles.confirm}
          onClick={() =>
            handleModal
              ? handleModal({ view: false, content: content })
              : window.location.reload()
          }
        >
          확인
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
