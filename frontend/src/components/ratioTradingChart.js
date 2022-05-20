import React, { Component } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

class RatioTradingChart extends Component {
  constructor(props) {
    super(props);

    this.state = {
      prices1: [],
      prices2: [],
      labels: []
    }
  }

  async componentDidMount() {
    const ratio = this.props.ratio;
    const mean = this.props.mean;
    const posSD = this.props.posSD;
    const negSD = this.props.negSD;

    const endDate = this.props.date;
    const labels = this.getDates(endDate, mean.length);

    this.setState({
      ratio: ratio,
      mean: mean,
      posSD: posSD,
      negSD: negSD,
      labels: labels
    });
  }

  getDates(endDate, dateNum) {
    let res = [];
    let currDate = new Date(endDate);
    for (let i = dateNum; i > 0; i--) {
      res.push(currDate.toLocaleDateString());
      currDate.setDate(currDate.getDate() - 1);
      // Skip weekends as they are non trading days
      while (currDate.getDay() === 6 || currDate.getDay() === 0) {
        currDate.setDate(currDate.getDate() - 1);
      }
    }
    return res.reverse();
  }

  getData() {
    return {
      labels: this.state.labels,
      datasets: [
        {
          label: `Ratio (${this.props.ticker1}/${this.props.ticker2})`,
          data: this.state.ratio,
          borderColor: 'rgb(53, 162, 235)',
          backgroundColor: 'rgba(53, 162, 235, 0.5)',
        },
        {
          label: "Rolling Mean (30 day)",
          data: this.state.mean,
          borderColor: 'rgb(236, 125, 16)',
          backgroundColor: 'rgba(236, 125, 16, 0.5)',
        },
        {
          label: "+2 x Standard Deviation (30 day)",
          data: this.state.posSD,
          borderColor: 'rgb(9, 129, 74)',
          backgroundColor: 'rgba(9, 129, 74, 0.5)',
        },
        {
          label: "-2 x Standard Deviation (30 day)",
          data: this.state.negSD,
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
        },
      ],
    };
  }

  getOptions() {
    return {
      responsive: true,
      scaleBeginAtZero: false,
      plugins: {
        legend: {
          position: 'top',
          color:"white"
        },
        title: {
          display: true,
          text: `Ratio comparison of ${this.props.ticker1}/${this.props.ticker2} for the previous 6 months.`,
          color: "white"
        },
      },
      scales: {
        x: {
          ticks: {
            color: "white",
          }
        },
        y: {
          ticks: {
            color: "white"
          }
        }
      }
    }
  }



  render() {
    return (
      <div>
        <Line options={this.getOptions()} data={this.getData()} />
      </div>
    );
  }

}

export default RatioTradingChart;
