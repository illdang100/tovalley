import { useState } from "react";

export const useSaveImg = () => {
  const [uploadImg, setUploadImg] = useState<String[]>([]);
  const [imgFiles, setImgFiles] = useState<File[]>([]);

  const saveImgFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const files = e.target.files;

      if (!files[0]) return;

      let imgUrlList = [...uploadImg];
      let imgList = [...imgFiles];

      for (let i = 0; i < files.length; i++) {
        const currentImageUrl = URL.createObjectURL(files[i]);
        imgUrlList.push(currentImageUrl);
        imgList.push(files[i]);
      }

      if (imgUrlList.length > 5) {
        imgUrlList = imgUrlList.slice(0, 5);
        imgList = imgList.slice(0, 5);
      }

      setUploadImg(imgUrlList);
      setImgFiles(imgList);

      if (uploadImg.length + files.length > 5) {
        return alert("최대 5개 사진만 첨부할 수 있습니다.");
      }
    }
  };

  const handleDeleteImage = (id: number) => {
    setUploadImg(uploadImg.filter((_, index) => index !== id));
    setImgFiles(imgFiles.filter((_, index) => index !== id));
  };

  return {
    uploadImg,
    setUploadImg,
    imgFiles,
    saveImgFile,
    handleDeleteImage,
  };
};
