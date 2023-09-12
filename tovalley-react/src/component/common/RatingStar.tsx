import React, { FC } from "react";
import {
  MdOutlineStar,
  MdOutlineStarHalf,
  MdOutlineStarBorder,
} from "react-icons/md";

interface Props {
  rating: number;
  size: string;
}

const RatingStar: FC<Props> = ({ rating, size }) => {
  let ratingStar;

  if (rating - Math.floor(rating) === 0)
    ratingStar = (
      <>
        {Array(Math.floor(rating))
          .fill(0)
          .map(() => {
            return <MdOutlineStar color="#66A5FC" size={size} />;
          })}
        {Array(5 - Math.floor(rating))
          .fill(0)
          .map(() => {
            return <MdOutlineStarBorder color="#66A5FC" size={size} />;
          })}
      </>
    );
  else
    ratingStar = (
      <>
        {Array(Math.floor(rating))
          .fill(0)
          .map(() => {
            return <MdOutlineStar color="#66A5FC" size={size} />;
          })}
        <MdOutlineStarHalf color="#66A5FC" size={size} />
        {Array(4 - Math.floor(rating))
          .fill(0)
          .map(() => {
            return <MdOutlineStarBorder color="#66A5FC" size={size} />;
          })}
      </>
    );

  return <div>{ratingStar}</div>;
};

export default RatingStar;
