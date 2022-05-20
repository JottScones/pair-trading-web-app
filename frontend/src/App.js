import logo from './logo.svg';
import React from 'react';
import './App.css';
import StoredPairs from './storedPairs';
import {MathJaxContext} from 'better-react-mathjax';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from 'react-router-dom';

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
