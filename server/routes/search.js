var express = require('express');
var router = express.Router();
var mysql = require('../common/userDB');

router.post('/in_all_user', function (req, res) {
    mysql.SEARCH_IN_ALL_USER(req.body.keyword)
        .then(function (result) {
            var data = JSON.stringify(result);
            res.send(data);
        }).catch(function () {
            res.send(null)
        });
});

module.exports = router;