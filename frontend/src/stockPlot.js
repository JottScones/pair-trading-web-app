import React, { Component } from 'react';
import PairPriceChart from './components/pairPriceChart';
import ReactLoading from 'react-loading';

class StockPlot extends Component {
  constructor(props) {
    super(props);
    this.state = {
      stock1: {},
      stock2: {},
      failedAPI: true,
      loading: true,
    }
  }

  async componentDidMount() {
    const [stock1Resp, stock2Resp] = await Promise.all([
      fetch(`api/stored/stock?ticker=${this.props.ticker1}`),
      fetch(`api/stored/stock?ticker=${this.props.ticker2}`),
    ]);

    const stock1 = await stock1Resp.json();
    const stock2 = await stock2Resp.json();

    this.setState({
      stock1: stock1,
      stock2: stock2,
      failedAPI: !(stock1Resp.ok && stock2Resp.ok),
      loading: false,
    })
  }

  render() {
    if (this.state.loading) return (
      <div style={{height: "10vh", display:"flex", justifyContent: "center", alignItems:"center"}} >
        <ReactLoading type={"bars"} color={"#563d7c"} height={'5%'} width={'5%'}/>
      </div>)

    if (this.state.failedAPI) return (<div className="container col-lg-8 pb-5">
      <h3>Price plot from previous year</h3>
      <p className='text-warning'>
        Stock prices not loading! Please refresh. If problem persists contact developer.
      </p>
    </div>)

    return (
      <>
        <div className="container col-lg-8 pb-5">
          <h3>Price plot from previous year</h3>
          <p>Below you can see the plot of stock prices for {this.props.ticker1} and {this.props.ticker2}.
            Even from this visualisation you should be able to get an idea of how well paired these two stocks are.
            For a good paring, the price of both stocks should rise and fall at time and should maintain a constant
            distance from each other. In cases where one stock has a far higher price than the other, the pairing is
            far less easy to see (try GOOGL with another stock).
          </p>
          <PairPriceChart stock1={this.state.stock1} stock2={this.state.stock2}/>
        </div>
      </>
    );
  }

}

export default StockPlot;
