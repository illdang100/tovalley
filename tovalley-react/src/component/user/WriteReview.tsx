import React, { FC, useEffect, useState } from "react";
import styles from "../../css/user/WriteReview.module.css";
import { BsCameraFill } from "react-icons/bs";
import {
  FaRegFaceGrinSquint,
  FaRegFaceSmile,
  FaRegFaceFrown,
} from "react-icons/fa6";
import { MdClose } from "react-icons/md";
import { MdOutlineStar } from "react-icons/md";
import axiosInstance from "../../axios_interceptor";
import { IoIosCloseCircle } from "react-icons/io";

interface Props {
  setWriteReviewView: React.Dispatch<React.SetStateAction<boolean>>;
  valleyInfo: {
    id: number;
    title: string;
    addr: string;
    tripDate: string;
    people: number;
    img: string | null;
  };
}

const WriteReview: FC<Props> = ({ setWriteReviewView, valleyInfo }) => {
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

  const qualityBtn = ["깨끗해요", "괜찮아요", "더러워요"];
  const [review, setReview] = useState({
    quality: "",
    content: "",
  });
  const [uploadImg, setUploadImg] = useState<String[]>([]);

  const [star, setStar] = useState({
    one: false,
    two: false,
    three: false,
    four: false,
    five: false,
  });

  const handleWriteReview = (e: any) => {
    e.preventDefault();
    const formData = new FormData();

    const quality =
      review.quality === "깨끗해요"
        ? "CLEAN"
        : review.quality === "괜찮아요"
        ? "FINE"
        : "DIRTY";

    let rating;
    if (star.five === true) rating = 5;
    else if (star.four === true) rating = 4;
    else if (star.three === true) rating = 3;
    else if (star.two === true) rating = 2;
    else rating = 1;

    formData.append("tripScheduleId", `${valleyInfo.id}`);
    formData.append("waterQuality", quality);
    formData.append("rating", `${rating}`);
    formData.append("content", review.content);

    //formData.append("imageContainer", uploadImg)

    axiosInstance
      .post("/api/auth/reviews", formData)
      .then((res) => {
        console.log(res);
        res.status === 200 && setWriteReviewView(false);
      })
      .catch((err) => console.log(err));
  };

  const saveImgFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files!;

    if (!files[0]) return;

    let imgUrlList = [...uploadImg];

    for (let i = 0; i < files.length; i++) {
      const currentImageUrl = URL.createObjectURL(files[i]);
      imgUrlList.push(currentImageUrl);
    }

    if (imgUrlList.length > 5) {
      imgUrlList = imgUrlList.slice(0, 5);
    }

    setUploadImg(imgUrlList);

    if (uploadImg.length + files.length > 5) {
      return alert("최대 5개 사진만 첨부할 수 있습니다.");
    }
  };

  const handleDeleteImage = (id: number) => {
    setUploadImg(uploadImg.filter((_, index) => index !== id));
  };

  return (
    <div className={styles.modalContainer}>
      <form
        className={styles.modalBox}
        onSubmit={handleWriteReview}
        encType="multipart/form-data"
      >
        <div className={styles.writeTitle}>
          <span>리뷰쓰기</span>
          <span onClick={() => setWriteReviewView(false)}>
            <MdClose color="#B5B5B5" size="26px" />
          </span>
        </div>
        <div className={styles.writeDetail}>
          <div className={styles.valleyInfo}>
            <img
              src={
                valleyInfo.img === null
                  ? process.env.PUBLIC_URL + "/img/default-image.png"
                  : valleyInfo.img
              }
              alt="계곡 이미지"
              width="120px"
            />
            <div className={styles.valleyInfoDetail}>
              <h4>{valleyInfo.title}</h4>
              <span>{valleyInfo.addr}</span>
              <div>
                <span>날짜</span>
                <span>{valleyInfo.tripDate}</span>
              </div>
              <div>
                <span>인원</span>
                <span>{valleyInfo.people}</span>
              </div>
            </div>
          </div>
          <div className={styles.writeStar}>
            <StarItem num="one" item={star.one} setStar={setStar} />
            <StarItem num="two" item={star.two} setStar={setStar} />
            <StarItem num="three" item={star.three} setStar={setStar} />
            <StarItem num="four" item={star.four} setStar={setStar} />
            <StarItem num="five" item={star.five} setStar={setStar} />
          </div>
          <div className={styles.reviewContent}>
            <div className={styles.reviewTextarea}>
              <textarea
                placeholder="계곡에 대한 후기를 남겨주세요. (최소 10자 이상)"
                value={review.content}
                onChange={(e) =>
                  setReview({ ...review, content: e.target.value })
                }
                maxLength={5000}
              />
              <span>{review.content.length}/5000</span>
            </div>
            <label htmlFor="input-file">
              <div className={styles.pictureBtn}>
                <span>
                  <BsCameraFill size="26px" />
                </span>
                <input
                  className={styles.imgInput}
                  type="file"
                  accept="image/*"
                  id="input-file"
                  multiple
                  onChange={saveImgFile}
                />
                <span>사진 첨부하기</span>
              </div>
            </label>
            {uploadImg.length !== 0 && (
              <div className={styles.previewImg}>
                {uploadImg.map((image, id) => (
                  <div className={styles.imageContainer}>
                    <div key={id}>
                      <img src={`${image}`} alt={`${image}-${id}`} />
                    </div>
                    <span onClick={() => handleDeleteImage(id)}>
                      <IoIosCloseCircle color="#F6483D" size="30px" />
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
          <div className={styles.writeQuality}>
            <span>수질은 괜찮았나요?</span>
            <div>
              {qualityBtn.map((item, index) => {
                return (
                  <div
                    onClick={() => setReview({ ...review, quality: item })}
                    style={
                      review.quality === item ? { borderColor: "#66A5FC" } : {}
                    }
                  >
                    <span
                      style={
                        review.quality === item
                          ? { color: "#66A5FC" }
                          : { color: "#5E5E5E" }
                      }
                    >
                      {index === 0 ? (
                        <FaRegFaceGrinSquint />
                      ) : index === 1 ? (
                        <FaRegFaceSmile />
                      ) : (
                        <FaRegFaceFrown />
                      )}
                    </span>
                    <span
                      style={
                        review.quality === item ? { color: "#66A5FC" } : {}
                      }
                    >
                      {item}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className={styles.reviewBtn}>
          <button type="button" onClick={() => setWriteReviewView(false)}>
            취소
          </button>
          <button>등록</button>
        </div>
      </form>
    </div>
  );
};

interface StarProps {
  num: string;
  item: boolean;
  setStar: React.Dispatch<
    React.SetStateAction<{
      one: boolean;
      two: boolean;
      three: boolean;
      four: boolean;
      five: boolean;
    }>
  >;
}

const StarItem: FC<StarProps> = ({ num, item, setStar }) => {
  return (
    <span
      onClick={() => {
        if (item === false) {
          num === "one"
            ? setStar({
                one: true,
                two: false,
                three: false,
                four: false,
                five: false,
              })
            : num === "two"
            ? setStar({
                one: true,
                two: true,
                three: false,
                four: false,
                five: false,
              })
            : num === "three"
            ? setStar({
                one: true,
                two: true,
                three: true,
                four: false,
                five: false,
              })
            : num === "four"
            ? setStar({
                one: true,
                two: true,
                three: true,
                four: true,
                five: false,
              })
            : setStar({
                one: true,
                two: true,
                three: true,
                four: true,
                five: true,
              });
        } else {
          num === "one"
            ? setStar({
                one: false,
                two: false,
                three: false,
                four: false,
                five: false,
              })
            : num === "two"
            ? setStar({
                one: true,
                two: false,
                three: false,
                four: false,
                five: false,
              })
            : num === "three"
            ? setStar({
                one: true,
                two: true,
                three: false,
                four: false,
                five: false,
              })
            : num === "four"
            ? setStar({
                one: true,
                two: true,
                three: true,
                four: false,
                five: false,
              })
            : setStar({
                one: true,
                two: true,
                three: true,
                four: true,
                five: false,
              });
        }
      }}
    >
      {item === true ? (
        <MdOutlineStar color="#66A5FC" size="40px" />
      ) : (
        <MdOutlineStar color="#B5B5B5" size="40px" />
      )}
    </span>
  );
};

export default WriteReview;
