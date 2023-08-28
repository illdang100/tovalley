import React, { FC, useState } from "react";
import styles from "../../css/common/PagingBtn.module.css";

interface Props {
  totalPages: number;
}

const PagingBtn: FC<Props> = ({ totalPages }) => {
  const [page, setPage] = useState(1);
  const [currPage, setCurrPage] = useState(page);

  let firstNum = currPage - (currPage % 5) + 1;
  let lastNum = currPage - (currPage % 5) + 5;

  return (
    <div className={styles.paging}>
      <button
        onClick={() => {
          setPage(page - 1);
          setCurrPage(page - 2);
        }}
        disabled={page === 1}
      >
        &lt;
      </button>
      <button
        onClick={() => setPage(firstNum)}
        aria-current={page === firstNum ? "page" : undefined}
      >
        {firstNum}
      </button>
      {Array(4)
        .fill(0)
        .map((_, i) => {
          if (firstNum + 1 + i > totalPages) {
            return null;
          } else {
            if (i <= 2) {
              return (
                <button
                  key={i + 1}
                  onClick={() => {
                    setPage(firstNum + 1 + i);
                  }}
                  aria-current={page === firstNum + 1 + i ? "page" : undefined}
                >
                  {firstNum + 1 + i}
                </button>
              );
            } else if (i >= 3) {
              return (
                <button
                  key={i + 1}
                  onClick={() => setPage(lastNum)}
                  aria-current={page === lastNum ? "page" : undefined}
                >
                  {lastNum}
                </button>
              );
            }
          }
        })}
      <button
        onClick={() => {
          setPage(page + 1);
          setCurrPage(page);
        }}
        disabled={page === totalPages}
      >
        &gt;
      </button>
    </div>
  );
};

export default PagingBtn;
