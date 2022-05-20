import React, { Component } from 'react';
import ReactLoading from 'react-loading';

class TradeMeasures extends Component {
  constructor(props) {
    super(props);
    this.state = {
      profit: 0,
      trades: [],
      dates: [],
      failedAPI: true,
      loading: true
    };
  }

  async componentDidMount() {
    const [profitResp, tradeResp, stock1Resp] = await Promise.all([
      fetch(`api/stored/profit?ticker1=${this.props.ticker1}&ticker2=${this.props.ticker2}`),
      fetch(`api/stored/trades?ticker1=${this.props.ticker1}&ticker2=${this.props.ticker2}`),
      fetch(`api/stored/stock?ticker=${this.props.ticker1}`)
    ]);

    const profit = await profitResp.json();
    const trades = await tradeResp.json();
    const stock1 = await stock1Resp.json();

    const dates = this.getDates(stock1.date, trades.length)

    this.setState({
      profit: profit,
      trades: trades,
      dates: dates,
      failedAPI: !(profitResp.ok && tradeResp.ok && stock1Resp),
      loading: false
    })
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

  render() {
    if (this.state.loading) return (
      <div style={{height: "10vh", display:"flex", justifyContent: "center", alignItems:"center"}} >
        <ReactLoading type={"bars"} color={"#563d7c"} height={'5%'} width={'5%'}/>
      </div>)
      
    if (this.state.failedAPI) return (
      <div className="container col-lg-8">
        <h3>Trading Outcome</h3>
        <p className="text-warning">Trading strategy not loading! Please refresh. If problem persists contact developer.</p>
      </div>)

    return (
      <div>
        <div className="container col-lg-8">
          <h3>Trading Outcome</h3>
          <h5 className="text-muted">Calculated Profit: ${Math.round(this.state.profit)}</h5>

          <p>
          To calculate potential profits we apply our strategy to the previous 6 months of price history.
          At each day we check the previous days ratio and compare it to the previous days standard deviation bounds.
          If ratio is greater than 2 standard deviations above the mean, we short {this.props.ticker1} and long {this.props.ticker2}.
          If ratio is greater than 2 standard deviations below the mean, we long {this.props.ticker1} and short {this.props.ticker2}.
          </p>

          <p>
          In our trading simulation we start with a budget of $1000 dollars. Everytime we decide to trade we split our current budget 50/50
          to bet on shorts and longs.
          </p>

          <p>
          Below is a table detailing our trades for each day. The trade signal is based on the previous days metrics.
          </p>

          <table className="table table-dark table-bordered table-borderless">
            <thead>
              <tr>
                  <th scope="col">Date</th>
                  <th scope="col">Trade Signal</th>
                  <th scope='col'>Budget</th>
                  <th scope="col">{this.props.ticker1} yesterday close</th>
                  <th scope="col">{this.props.ticker1} today close</th>
                  <th scope="col">{this.props.ticker2} yesterday close</th>
                  <th scope="col">{this.props.ticker2} today close</th>
              </tr>
            </thead>

            {this.state.trades.map((t,i) => {
              return (
                <tr className="text-center">
                  <th scope="row">{this.state.dates[i]}</th>
                  <td>{
                    t.tradeSignal > 0 ? `Short ${this.props.ticker1} Long ${this.props.ticker2}`:
                    t.tradeSignal < 0 ? `Long ${this.props.ticker1} Short ${this.props.ticker2}`:
                  "No Trade"}</td>
                  <td>{Math.round(t.budget)}</td>
                  <td>{t.price1Start}</td>
                  <td>{t.price1End}</td>
                  <td>{t.price2Start}</td>
                  <td>{t.price2End}</td>
                </tr>
              )
            })}
          </table>
        </div>
      </div>
    );
  }

}

export default TradeMeasures;
