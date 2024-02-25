import React, { FC, useEffect, useState } from "react";
import styles from "../../css/lostItem/ValleyModal.module.css";
import { MdClose } from "react-icons/md";
import { PlaceName } from "../../typings/db";
import axios from "axios";

interface Props {
  closeModal: () => void;
  selectedPlace: PlaceName[];
  setSelectedPlace: React.Dispatch<React.SetStateAction<PlaceName[]>>;
  writePage?: boolean;
}

const localhost = process.env.REACT_APP_HOST;

const ValleyModal: FC<Props> = ({
  closeModal,
  selectedPlace,
  setSelectedPlace,
  writePage,
}) => {
  const [selectedList, setSelectedList] = useState<PlaceName[]>([]);
  const [waterPlaceList, setWaterPlaceList] = useState<PlaceName[]>([]);
  const [selectPlace, setSelectPlace] = useState<PlaceName>();
  const [detailName, setDetailName] = useState("");
  const [searchText, setSearchText] = useState("");
  const CHOSUNG_LIST = [
    "ㄱ",
    "ㄲ",
    "ㄴ",
    "ㄷ",
    "ㄸ",
    "ㄹ",
    "ㅁ",
    "ㅂ",
    "ㅃ",
    "ㅅ",
    "ㅆ",
    "ㅇ",
    "ㅈ",
    "ㅉ",
    "ㅊ",
    "ㅋ",
    "ㅌ",
    "ㅍ",
    "ㅎ",
  ];

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

  useEffect(() => {
    const getWaterPlaceList = async () => {
      try {
        const res = await axios.get(`${localhost}/api/water-place`);
        setWaterPlaceList(res.data.data);
      } catch (err) {
        console.log(err);
      }
    };
    getWaterPlaceList();
  }, []);

  // 한글을 초성으로 변환하는 함수
  const getInitialSound = (word: string) => {
    const CHOSUNG_START = 44032;
    const CHOSUNG_BASE = 588;

    const chosungIndex = Math.floor(
      (word.charCodeAt(0) - CHOSUNG_START) / CHOSUNG_BASE
    );

    return CHOSUNG_LIST[chosungIndex];
  };

  const addSelectedList = (place: PlaceName) => {
    let include = false;
    selectedList.map((item) => {
      if (item.waterPlaceId === place.waterPlaceId) {
        include = true;
      }
      return null;
    });

    if (!include) {
      const newList = selectedList.concat(place);
      setSelectedList(newList);
    }
  };

  const deleteSelectItem = (place: PlaceName) => {
    const newList = selectedList.filter((item) => {
      return item.waterPlaceId !== place.waterPlaceId;
    });
    setSelectedList(newList);
  };

  const clickSelect = () => {
    if (writePage && selectPlace) {
      setSelectedPlace([selectPlace]);
      closeModal();
    } else {
      let include = false;
      selectedPlace.map((item) => {
        selectedList.map((place) => {
          if (item.waterPlaceId === place.waterPlaceId) {
            include = true;
          }
          return null;
        });
        return null;
      });
      if (!include) setSelectedPlace(selectedPlace.concat(selectedList));
      closeModal();
    }
  };

  return (
    <div className={styles.modalContainer}>
      <div className={styles.modalWrap}>
        <div className={styles.searchContainer}>
          <input
            className={styles.searchInput}
            placeholder="계곡을 검색하세요."
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
          />
          <div className={styles.searchBtn}>
            <button onClick={closeModal}>취소</button>
            <button onClick={clickSelect}>선택</button>
          </div>
        </div>
        <div className={styles.scroll}>
          <div className={styles.selectedPlace}>
            {!writePage &&
              selectedList.map((item) => (
                <div key={item.waterPlaceId}>
                  <span onClick={() => deleteSelectItem(item)}>
                    <MdClose />
                  </span>
                  <span>{item.waterPlaceName}</span>
                </div>
              ))}
          </div>
          <div className={styles.placeNameContainer}>
            {searchText !== ""
              ? waterPlaceList.map((el) => {
                  if (el.waterPlaceName.includes(searchText)) {
                    return (
                      <div
                        key={el.waterPlaceId}
                        className={styles.placeNameWrap}
                        onMouseOver={() => setDetailName(el.waterPlaceName)}
                        onMouseLeave={() => setDetailName("")}
                      >
                        {detailName === el.waterPlaceName &&
                          el.waterPlaceName.length >= 4 && (
                            <span className={styles.detailName}>
                              {el.waterPlaceName}
                            </span>
                          )}
                        <span
                          key={el.waterPlaceId}
                          className={
                            writePage && selectPlace === el
                              ? styles.placeNameSelect
                              : styles.placeName
                          }
                          onClick={() => {
                            setDetailName("");
                            if (writePage) setSelectPlace(el);
                            else addSelectedList(el);
                          }}
                        >
                          {el.waterPlaceName}
                        </span>
                      </div>
                    );
                  } else return null;
                })
              : searchText === "" &&
                CHOSUNG_LIST.map((chosung) => {
                  return (
                    <div key={chosung} className={styles.placeNameBox}>
                      <span className={styles.placeTitle}>{chosung}</span>
                      {waterPlaceList.map((el) => {
                        if (getInitialSound(el.waterPlaceName) === chosung)
                          return (
                            <div
                              key={el.waterPlaceId}
                              className={styles.placeNameWrap}
                              onMouseOver={() =>
                                setDetailName(el.waterPlaceName)
                              }
                              onMouseLeave={() => setDetailName("")}
                            >
                              {detailName === el.waterPlaceName &&
                                el.waterPlaceName.length >= 4 && (
                                  <span className={styles.detailName}>
                                    {el.waterPlaceName}
                                  </span>
                                )}
                              <span
                                key={el.waterPlaceId}
                                className={
                                  writePage && selectPlace === el
                                    ? styles.placeNameSelect
                                    : styles.placeName
                                }
                                onClick={() => {
                                  setDetailName("");
                                  if (writePage) setSelectPlace(el);
                                  else addSelectedList(el);
                                }}
                              >
                                {el.waterPlaceName}
                              </span>
                            </div>
                          );
                        else return null;
                      })}
                    </div>
                  );
                })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ValleyModal;
