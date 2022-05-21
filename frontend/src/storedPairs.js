import React from 'react';
import StockPlot from './stockPlot';
import MetricPlot from './metricPlot';
import DistanceMeasure from './distanceMeasures';
import TradeMeasures from './tradeMeasures';
import ReactLoading from 'react-loading';
import './App.css';

class StoredPairs extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      stocks: [],
      showResults: false,
      ticker1: "",
      ticker2: "",
      loading: true,
      failedAPI: true
    }
  }

  async componentDidMount() {
    const response = await fetch('api/stored');
    const body = await response.json();
    this.setState({
      loading: false,
      failedAPI: !response.ok,
      stocks: body,
    });
  }

  handleSubmit(e) {
    e.preventDefault();
    console.log('handling submit')
    console.log(e.target.ticker1.value)
    this.setState({
      ticker1: e.target.ticker1.value,
      ticker2: e.target.ticker2.value,
      showResults: true,
    })
  }

  render() {
    if (this.state.loading) return (
      <div style={{height: "100vh", display:"flex", justifyContent: "center", alignItems:"center"}} >
        <ReactLoading type={"bars"} color={"#563d7c"} height={'5%'} width={'5%'}/>
      </div>)

    return (
      <div className="text-white">
        <h1 className="text-center"> Pair Trading Evaluation </h1>
        <div className="mx-auto col-lg-5 p-5">
          <p className="text-justify">
            In this page you can select two stocks and compare how correlated they are. You will be able to view:
          </p>
          <ul>
            <li>Metrics of price correlation.</li>
            <li>Time series graph to compare price over previous year.</li>
            <li>Estimated profit using pair trading algorithm.</li>
          </ul>
        </div>
        <div className="container col-lg-6 p-3 mb-5 rounded" style={{backgroundColor: "#563d7c"}}>
          <h2 className={(this.state.failedAPI ? "text-warning":"")}> {(this.state.failedAPI ? "Data not loading! Please refresh. If problem persists contact developer.":"Stock selection form")}</h2>
          <form className="form-group" onSubmit={this.handleSubmit.bind(this)} >
            <label htmlFor="ticker1">Please choose 2 stock tickers to evaluate.</label>
            <div className="row">
              <div className="col-sm-6">
                  <div className="form-group">
                    <select className="form-control" id='ticker1' defaultValue={""} required>
                      {this.state.stocks.map(s => <option key={s} value={s}>{s}</option>)}
                    </select>
                  </div>
              </div>
              <div className="col-sm-6">
                  <div className="form-group">
                      <select className="form-control" id='ticker2' defaultValue={""} required>
                        {this.state.stocks.map(s => <option key={s} value={s}>{s}</option>)}
                      </select>
                  </div>
              </div>
            </div>
            <div className="row justify-content-center pt-3">
              <input style={{width: "auto"}} className="form-control btn btn-dark" type="submit" value="Submit"/>
            </div>
          </form>
        </div>

        <h2 className="text-center">Results</h2>
        {

          this.state.showResults ?
            (
              <>
                <StockPlot key={this.state.ticker1+this.state.ticker2} ticker1={this.state.ticker1} ticker2={this.state.ticker2}/>
                <DistanceMeasure key={this.state.ticker1+this.state.ticker2+'1'} ticker1={this.state.ticker1} ticker2={this.state.ticker2}/>
                <MetricPlot key={this.state.ticker1+this.state.ticker2+'2'} ticker1={this.state.ticker1} ticker2={this.state.ticker2}/>
                <TradeMeasures key={this.state.ticker1+this.state.ticker2+'3'} ticker1={this.state.ticker1} ticker2={this.state.ticker2}/>
              </>
          ) :
            <p className="text-center text-danger">Submit stock choice when ready.</p>
        }
      </div>
    );
  }

}

export default StoredPairs;
