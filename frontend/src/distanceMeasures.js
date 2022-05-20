import React, { Component } from 'react';
import {MathJax} from 'better-react-mathjax';
import ReactLoading from 'react-loading';

class DistanceMeasure extends Component {
  constructor(props) {
    super(props);
    this.state = {
      distance: -1,
      pearson: -2,
      failedAPI: true,
      loading: true
    };
  }

  async componentDidMount() {
    const [distResp, pearsonResp] = await Promise.all([
      fetch(`api/stored/distance?ticker1=${this.props.ticker1}&ticker2=${this.props.ticker2}`),
      fetch(`api/stored/pearson?ticker1=${this.props.ticker1}&ticker2=${this.props.ticker2}`),
    ]);

    const distance = await distResp.json();
    const pearson = await pearsonResp.json();

    this.setState({
      distance,
      pearson,
      failedAPI: !(distResp.ok && pearsonResp.ok),
      loading: false
    });

  }

  render() {
    if (this.state.loading) return (
      <div style={{height: "10vh", display:"flex", justifyContent: "center", alignItems:"center"}} >
        <ReactLoading type={"bars"} color={"#563d7c"} height={'5%'} width={'5%'}/>
      </div>)

    if (this.state.failedAPI) return (
      <div className="container pt-5 pb-5 col-lg-8">
        <div className="pb-3">
        <h3>Distance Measures</h3>
        <p className='text-warning'>
          Distance functions failed! Please refresh. If problem persists contact developer.
        </p>
    </div></div>)

    const distanceFormula = '\\(\\sum_{i=1}^n{(\\frac{x_i}{x_1}-\\frac{y_i}{y_1}})^2\\)';
    const pearsonFormula = '\\(\\frac{\\sum_{(x_i - \\overline{x})(y_i - \\overline{y})}}{\\sigma_x \\sigma_y}\\)';

    return (
      <div className="container pt-5 pb-5 col-lg-8">
      <div className="pb-3">
        <h3>Distance Measures</h3>
        <p>What can we do if we can't visually tell how well two stocks are paired?</p>
        <p>Typically we try to quantify how good a pairing is by using metrics.
          There are various metrics that have been used to rate how well paired certain
          stocks are. Here we will use the sum of the square differences and Pearson's correlation.
        </p>
        <h4>Sum of square differences</h4>
          <p>
          This metric quantifies pairing through calculating the difference between
          the normalised price of each stock. Perfectly paired stocks should increase by the same amount
          at the same time so their distance should be 0. In our case, the lower the difference value the better.
          The equation of this measure is: <MathJax>{distanceFormula}</MathJax>
          </p>
        <h5 className="text-muted">
        Sum of Square Differences: {Math.round(this.state.distance * 100) / 100}
        </h5>
      </div>
       <div className="pb-3">
        <h4>Pearson's correlation</h4>
          <p>
          With sum of square differences, other than being close to zero, it's hard
          to determine how good a pairing is. One must decide a cut off for the distance measure.
          With Pearson correlation, we determin the linear correlation between two sets of data.
          The resulting coefficient is a value in [-1,1]. Any Pearson coefficient greater that 0.5
          represents a strong positive correlation (i.e. the two stocks pair well).
          The equation of this measure is: <MathJax>{pearsonFormula}</MathJax>
          </p>
        <h5 className="text-muted">
        Pearson Correlation: {Math.round(this.state.pearson * 100) / 100}
        </h5>
        <p>The correlation of {this.props.ticker1} and {this.props.ticker2} is
        {" " + (this.state.pearson > 0 ? 'positive':'positive')} and
        {" " + (Math.abs(this.state.pearson) >= 0.5 ? 'strong' : 'weak')}.
        </p>
      </div>
      </div>
    );
  }

}

export default DistanceMeasure;
