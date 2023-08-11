import React from "react";
import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LineElement,
  LinearScale,
  PointElement,
  Title,
  Tooltip,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export const options = {
  responsive: true,
  scales: {
    x: {
      grid: {
        display: false,
      },
    },
  },
  maintainAspectRatio: false,
};

const labels = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"];

export const data = {
  labels,
  datasets: [
    {
      label: "사망",
      data: [1, 3, 9, 3, 2, 1],
      borderColor: "#9396DB",
      backgroundColor: "#9396DB",
      borderWidth: 2,
    },
    {
      label: "실종",
      data: [0, 4, 8, 9, 0, 5],
      borderColor: "#89C4D1",
      backgroundColor: "#89C4D1",
      borderWidth: 2,
    },
    {
      label: "부상",
      data: [10, 11, 13, 6, 8, 1],
      borderColor: "#FEA520",
      backgroundColor: "#FEA520",
      borderWidth: 2,
    },
  ],
};

const AccidentChart = () => {
  return <Line options={options} data={data} />;
};

export default AccidentChart;
