var express = require('express');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');

var user = require('./routes/user');
var search = require('./routes/search');

var app = express();

// var server = require('http').createServer(app);
// var io = require('socket.io')(server);

// view engine setup
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/user', user);
app.use('/search', search);

// catch 404 and forward to error handler
app.use(function (req, res) {
    res.send(404)
});

// error handler
app.use(function (err, req, res) {
    res.send(err.status || 500);
});

module.exports = app;
