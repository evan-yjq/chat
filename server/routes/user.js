var express = require('express');
var router = express.Router();
var userDB = require('../common/userDB');

router.route('/sign_in').post(function (req, res) {
    console.log(req.body.account);
    userDB.VERIFY(req.body.account)
        .then(function (data) {
            if (data.length > 0 && data[0].password === req.body.password) {
                res.send(data[0]);
            } else {
                res.send(null)
            }
        }).catch(function () {
            res.send(null);
        });
});

router.route('/register').post(function (req, res, next) {
    userDB.PUT(req.body.username, req.body.password, req.body.email)
    .then(function () {
        // res.render('login', {log: "注册成功"});
        var out = {'result': true, 'log': '注册成功'};
        res.send(out);
    }).catch(function (result) {
        // res.render('login', {log: result});
        var out = {'result': false, 'log': result};
        res.send(out);
    });
});

router.route('/update').post(function (req, res, next) {
    if (!req.session.isLogin) {
        var out = {'result': false};
        res.send(out);
    } else {
        userDB.UPDATE(req.session.userId, req.body.username, req.body.password, req.body.email)
            .then(function () {
                req.session.username = req.body.username;
                // res.render('index', {log: "修改成功", isLogin: req.session.isLogin, username: req.session.username});
                var out = {'result': true, 'log': '修改成功', 'isLogin': req.session.isLogin, 'username': req.session.username};
                res.send(out);
            }).catch(function (result) {
            // res.render('update', {log: result});
            var out = {'result': false, 'log': result};
            res.send(out);
        });
    }
});

module.exports = router;
