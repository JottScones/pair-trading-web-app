import React from 'react';
import './App.css';
import StoredPairs from './storedPairs';
import {MathJaxContext} from 'better-react-mathjax';

class App extends React.Component {
  render() {
    return (
       <MathJaxContext>
          <StoredPairs/>
       </MathJaxContext>
    );
  }
}

export default App;
