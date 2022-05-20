import React, { Component } from 'react';
import RatioTradingChart from './components/ratioTradingChart';
import ReactLoading from 'react-loading';

class MetricPlot extends Component {
  constructor(props) {
    super(props);
    this.state = {
      date: "",
      ratio: [],
      mean: [],
      posSD: [],
      negSD: [],
      failedAPI: true,
      loading: true
    };
  }

  async componentDidMount() {
    const [stock1Resp, response] = await Promise.all([
      fetch(`api/stored/stock?ticker=${this.props.ticker1}`),
      fetch(`api/stored/metrics?ticker1=${this.props.ticker1}&ticker2=${this.props.ticker2}`),
    ]);
    const date = (await stock1Resp.json()).date;
    const {ratio, mean, posSD, negSD} = await response.json();
    this.setState({
      date,
      ratio,
      mean,
      posSD,
      negSD,
      failedAPI: !(stock1Resp.ok && response.ok),
      loading: false
    });
  }

  render() {
    if (this.state.loading) return (
      <div style={{height: "10vh", display:"flex", justifyContent: "center", alignItems:"center"}} >
        <ReactLoading type={"bars"} color={"#563d7c"} height={'5%'} width={'5%'}/>
      </div>)

    if (this.state.failedAPI) return (
      <div className="container col-lg-8 pb-5">
        <h3>Pair Trading Visualiser</h3>
        <p className="text-warning">Trading metrics not loading! Please refresh. If problem persists contact developer.</p>
      </div>
    )
    return (
      <>
        <div className="container col-lg-8 pb-5">
          <h3>Pair Trading Visualiser</h3>
          <p>
            Once we have found a good pair of stocks we can implement our trading strategy.
            Based on the history of the stocks, we know the prices should rise and fall in unison.
            Ideally this means that the ratio of their prices should stay consistent.
            When the ratio massively deviates from the mean, it means there is a disparity in price between
            our pair. One stock may be overvalued and the other undervalued. To profit off of this,
            we can short the overvalued stock and long the undervalued one.
          </p>

          <p>
            The trading algorithm uses rolling average and standard deviation of the ratio of prices
            to decide when to trade. If the ratio rises above 2*SD or dips below -2*SD we know to purchase
            our shorts and longs.
          </p>

          <p>
            Below is a chart representing the metrics our trading strategy tracks.
          </p>
          <RatioTradingChart ticker1={this.props.ticker1} ticker2={this.props.ticker2} date={this.state.date} ratio={this.state.ratio} mean={this.state.mean} posSD={this.state.posSD} negSD={this.state.negSD}/>
        </div>
      </>
    );
  }

}

export default MetricPlot;
