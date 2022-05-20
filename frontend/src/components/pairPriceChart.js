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

class PairPriceChart extends Component {
  constructor(props) {
    super(props);

    this.state = {
      prices1: [],
      prices2: [],
      labels: []
    }
  }

  async componentDidMount() {
    const prices1 = this.props.stock1.prices;
    const prices2 = this.props.stock2.prices;

    const endDate = this.props.stock1.date;
    const labels = this.getDates(endDate, prices1.length);

    this.setState({
      prices1: prices1,
      prices2: prices2,
      labels: labels
    });
    console.log(this.state);
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
          label: this.props.stock1.ticker,
          data: this.state.prices1,
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
        },
        {
          label: this.props.stock2.ticker,
          data: this.state.prices2,
          borderColor: 'rgb(53, 162, 235)',
          backgroundColor: 'rgba(53, 162, 235, 0.5)',
        },
      ],
    };
  }

  getOptions() {
    return {
      responsive: true,
      plugins: {
        legend: {
          position: 'top',
          color:"white"
        },
        title: {
          display: true,
          text: `Price comparison of ${this.props.stock1.ticker} and ${this.props.stock2.ticker} over the past year.`,
          color: "white"
        },
      },
      scales: {
        x: {
          ticks: {
            color: "white"
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

export default PairPriceChart;
