import React, { FC } from "react";
import { ArcElement, Chart as ChartJS, Legend, Tooltip } from "chart.js";
import { Doughnut } from "react-chartjs-2";

ChartJS.register(ArcElement, Tooltip, Legend);

interface Props {
  waterQualityReviews: {
    깨끗해요: number;
    괜찮아요: number;
    더러워요: number;
  };
}

const ValleyQualityChart: FC<Props> = ({ waterQualityReviews }) => {
  const labels =
    waterQualityReviews.깨끗해요 === undefined &&
    waterQualityReviews.괜찮아요 === undefined &&
    waterQualityReviews.더러워요 === undefined
      ? []
      : ["깨끗해요", "괜찮아요", "더러워요"];

  const data = {
    labels,
    datasets: [
      {
        data: [
          waterQualityReviews.깨끗해요 === undefined &&
          waterQualityReviews.괜찮아요 === undefined &&
          waterQualityReviews.더러워요 === undefined
            ? -1
            : waterQualityReviews.깨끗해요,
          waterQualityReviews.깨끗해요 === undefined &&
          waterQualityReviews.괜찮아요 === undefined &&
          waterQualityReviews.더러워요 === undefined
            ? 0
            : waterQualityReviews.괜찮아요,
          waterQualityReviews.깨끗해요 === undefined &&
          waterQualityReviews.괜찮아요 === undefined &&
          waterQualityReviews.더러워요 === undefined
            ? 0
            : waterQualityReviews.더러워요,
        ],
        backgroundColor: [
          waterQualityReviews.깨끗해요 === undefined &&
          waterQualityReviews.괜찮아요 === undefined &&
          waterQualityReviews.더러워요 === undefined
            ? "#b8b8b8"
            : "#AADCF2",
          "#82BEF5",
          "#C7B9B1",
        ],
      },
    ],
  };

  const options = {
    plugins: {
      tooltip: {
        enabled: false,
      },
    },
  };

  return (
    <Doughnut
      data={data}
      options={
        waterQualityReviews.깨끗해요 === undefined &&
        waterQualityReviews.괜찮아요 === undefined &&
        waterQualityReviews.더러워요 === undefined
          ? options
          : {}
      }
    />
  );
};

export default ValleyQualityChart;
