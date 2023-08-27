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

const labels = ["깨끗해요", "괜찮아요", "더러워요"];

const ValleyQualityChart: FC<Props> = ({ waterQualityReviews }) => {
  const data = {
    labels,
    datasets: [
      {
        data: [
          waterQualityReviews.깨끗해요,
          waterQualityReviews.괜찮아요,
          waterQualityReviews.더러워요,
        ],
        backgroundColor: ["#FF7DAC", "#FEC807", "#9396DB"],
      },
    ],
  };

  return <Doughnut data={data} />;
};

export default ValleyQualityChart;
