var express = require('express');
var router = express.Router();


router.get('/', function (req, res, next) {
    res.send(null);
});


router.route('/get_request_url').get(function (req, res) {
    requestUrlDB.SEARCH_ALL().then(function (result) {
        var data = JSON.stringify(result);
        res.send(data);
    })
});


module.exports = router;
