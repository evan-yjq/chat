var express = require('express');
var router = express.Router();
var mysql = require('../common/userDB');

router.get('/', function (req, res, next) {
    if (!req.session.islogin) {
        res.render('login');
        return;
    }
    res.render('search');
});

router.post('/', function (req, res, next) {
    if (!req.session.islogin) {
        res.render('login');
    } else {
        mysql.SEARCH(req.body.keyword)
            .then(function (result) {
                var data = JSON.stringify(result);
                // res.render('search', {log: "查询成功", data: data});
                res.send(data);
            }).catch(function (result) {
            res.render('search', {log: "数据库错误"});
        });
    }
});

module.exports = router;