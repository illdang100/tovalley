import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import { FaCheck } from "react-icons/fa6";
import { FaRegComment } from "react-icons/fa";
import { LuPencil } from "react-icons/lu";
import { IoSearch } from "react-icons/io5";
import { IoCloseOutline } from "react-icons/io5";
import styles from "../css/lostItem/LostItemList.module.css";
import Footer from "../component/footer/Footer";
import { useNavigate } from "react-router-dom";
import { LostList, PlaceName } from "../typings/db";
import axios from "axios";
import { elapsedTime } from "../composables/elapsedTime";
import ValleyModal from "../component/lostItem/ValleyModal";
import { Cookies } from "react-cookie";

const localhost = process.env.REACT_APP_HOST;

const LostItemListPage = () => {
  const lostItemCategory = ["전체", "물건 찾아요", "주인 찾아요"];
  const navigation = useNavigate();
  const [currentCategory, setCurrentCategory] = useState("전체");
  const [except, setExcept] = useState(false);
  const [search, setSearch] = useState("");
  const [searchPost, setSearchPost] = useState("");
  const [lostList, setLostList] = useState<LostList>();
  const [modalView, setModalView] = useState(false);
  const [selectedPlace, setSelectedPlace] = useState<PlaceName[]>([]);
  const cookies = new Cookies();

  const getLostList = () => {
    const selectedPlaceName = selectedPlace.map((el) => el.waterPlaceId);
    let params: {
      searchWord?: string;
      isResolved: boolean;
      category?: string;
    } = {
      isResolved: !except,
    };

    if (searchPost === search) {
      if (search) params = { ...params, searchWord: search };
    }

    let waterPlaceIdList = "";
    if (selectedPlaceName.length !== 0) {
      for (let i = 0; i < selectedPlaceName.length; i++) {
        waterPlaceIdList =
          waterPlaceIdList + `&waterPlaceId=${selectedPlaceName[i]}`;
      }
    }

    if (currentCategory === "물건 찾아요") {
      params = { ...params, category: "LOST" };
    } else if (currentCategory === "주인 찾아요") {
      params = { ...params, category: "FOUND" };
    }

    axios
      .get(`${localhost}/api/lostItem?${waterPlaceIdList}`, { params })
      .then((res) => {
        console.log(res);
        setLostList({ data: res.data.data.content });
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const closeModal = () => {
    setModalView(false);
  };

  useEffect(() => {
    getLostList();
  }, [currentCategory, searchPost, except, selectedPlace]);

  const clickLostItemPost = (category: string, id: number) => {
    navigation(`/lost-item/${category}/${id}`);
  };

  const toWritePage = () => {
    if (cookies.get("ISLOGIN")) navigation("/lost-item/write");
    else navigation("/login");
  };

  const deleteSelectedItem = (place: PlaceName) => {
    const newList = selectedPlace.filter((item) => {
      return item.waterPlaceId !== place.waterPlaceId;
    });

    setSelectedPlace(newList);
  };

  return (
    <div>
      <Header />
      <div className={styles.lostItemListPage}>
        <div className={styles.top}>
          <div className={styles.categoryWrap}>
            <div className={styles.category}>
              {lostItemCategory.map((item) => {
                return (
                  <div
                    key={item}
                    className={
                      item === currentCategory
                        ? styles.clicked
                        : styles.categoryItem
                    }
                    onClick={() => setCurrentCategory(item)}
                  >
                    {item}
                  </div>
                );
              })}
            </div>
            <div className={styles.writeBtn} onClick={toWritePage}>
              <LuPencil />
              <span>글 작성하기</span>
            </div>
          </div>
          <div className={styles.search}>
            <input
              placeholder="검색어를 입력해주세요."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  setSearchPost(search);
                }
              }}
            />
            <span onClick={() => setSearchPost(search)}>
              <IoSearch />
            </span>
          </div>
        </div>
        <div className={styles.body}>
          <div className={styles.filter}>
            <div
              onClick={() => setExcept((prev) => !prev)}
              className={except ? styles.exceptCheck : styles.checkBox}
            >
              <FaCheck />
            </div>
            <span>해결한 글 제외하기</span>
            <div className={styles.filterList}>
              <div className={styles.searchValley}>
                <span onClick={() => setModalView(true)}>물놀이 장소 선택</span>
              </div>
              {selectedPlace.map((place) => (
                <div key={place.waterPlaceId} className={styles.valleyFilter}>
                  <span onClick={() => deleteSelectedItem(place)}>
                    <IoCloseOutline size="15px" />
                  </span>
                  <span># {place.waterPlaceName}</span>
                </div>
              ))}
            </div>
          </div>
          {lostList?.data.map((item) => {
            return (
              <div
                key={item.id}
                className={styles.lostItemPost}
                onClick={() => clickLostItemPost(item.category, item.id)}
              >
                <div className={styles.lostItemContent}>
                  <div className={styles.title}>
                    <span
                      className={
                        item.category === "FOUND"
                          ? styles.categoryFound
                          : styles.categoryLost
                      }
                    >
                      {item.category === "FOUND"
                        ? "주인 찾아요"
                        : "물건 찾아요"}
                    </span>
                    <h4>{item.title}</h4>
                  </div>
                  <p>{item.content}</p>
                  <div className={styles.postInfo}>
                    <span>{elapsedTime(item.postCreateAt)}</span>
                    <span>{item.author}</span>
                    <div className={styles.comment}>
                      <FaRegComment />
                      <span>{item.commentCnt}</span>
                    </div>
                  </div>
                </div>
                {item.postImage && (
                  <div className={styles.lostItemImage}>
                    <div className={styles.imageContainer}>
                      <img src={item.postImage} alt="postImage" />
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
        {modalView && (
          <ValleyModal
            closeModal={closeModal}
            selectedPlace={selectedPlace}
            setSelectedPlace={setSelectedPlace}
          />
        )}
      </div>
      <Footer />
    </div>
  );
};

export default LostItemListPage;
