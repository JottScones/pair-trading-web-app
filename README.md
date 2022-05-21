# Pair Trading Web App
Hosted app can be found [here](https://jottscones-pair-trading.herokuapp.com) (For best experience use large screen device)

## A small note on implementation
The web app backend is implemented using Spring Boot, Java and MongoDB. The frontend is implemented using React and JavaScript.
The backend provides various api endpoints for the frontend to access stock data and functions. Unit tests are written using JUnit
and are run after each push to the main branch using GitHub Actions. The stock data is stored on a MongoDB Atlas cluster.

## App overview
The pair trading strategy works by finding two stocks that are highly correlated. 
Sometimes, the ratio between their stock prices may abnormally spike - meaning one is overvalued and the other undervalued. 
One can profit from betting that their stock prices will return to within normal bounds.

The trading pair app provides an interface to compare the effectiveness of applying this trading strategy to two selected stocks.

<img width="500" alt="image" src="https://user-images.githubusercontent.com/47277374/169629582-82a9147c-2b9a-4b6a-a6f2-64346c7b2b32.png">

### Stock Price Visualisation
Once two stocks are selected the frontend makes an api call to the backend to retrieve the stock price history.
Using Chartjs, the two stock prices are then overlaid one another to visually compare them.

<img width="800" alt="image" src="https://user-images.githubusercontent.com/47277374/169629629-5a06f727-3eea-4b03-8c81-1534d1c3cf35.png">


### Stock Distance metrics
The backend can also calculate distance metrics to quantify how well paired two stocks are.
The two metrics currently implemented are: Pearson's Correlation and Sum of squared difference.

### Stock Trading metrics
The trading strategy requires the use of various stock metrics such as rolling mean, standard deviation and ratio.
These are then visualised again using a chart.

<img width="800" alt="image" src="https://user-images.githubusercontent.com/47277374/169629816-d2b3e3c0-6903-4fc2-9245-84a3a053e847.png">


### Stock Trade Simulation
The backend can also simulate the trades made using the strategy on the selected stocks. It can return profit and a breakdown
of trade decisions on a day by day basis.

