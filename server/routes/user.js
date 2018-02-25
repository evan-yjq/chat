var express = require('express');
var router = express.Router();
var userDB = require('../common/userDB');

router.route('/').get(function (req, res, next) {
    if (!req.session.isLogin) {
        var out = {'result': false};
        res.send(out);
        return;
    }
    // res.render('index', {isLogin: req.session.isLogin, username: req.session.username});
    var out = {'result': true, 'isLogin': req.session.isLogin, 'username': req.session.username};
    res.send(out);
});

router.route('/sign_in').get(function (req, res, next) {
    if (!req.session.isLogin) {
        var out = {'result': false};
        res.send(out);
        return;
    }
    // res.render('index', {isLogin: req.session.isLogin, username: req.session.username});
    var out = {'result': true, 'isLogin': req.session.isLogin, 'username': req.session.username};
    res.send(out);
}).post(function (req, res, next) {
    userDB.VERIFY(req.body.username, req.body.password)
        .then(function (result) {
            req.session.username = req.body.username;
            req.session.userId = result;
            req.session.isLogin = true;
            // res.render('index', {log: "登陆成功",isLogin:req.session.isLogin,username:req.session.username});
            var out = {'result': true, 'log': '登陆成功', 'isLogin': req.session.isLogin, 'username': req.session.username};
            res.send(out);
        }).catch(function (result) {
        // res.render('login', {log: result});
        var out = {'result': false, 'log': result};
        res.send(out);
    });
});

router.route('/register').get(function (req, res, next) {
    var out = {'result': true};
    res.send(out);
}).post(function (req, res, next) {
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

router.route('/sign_out').get(function (req, res, next) {
    req.session.isLogin = false;
    req.session.username = undefined;
    req.session.userId = undefined;
    var out = {'result': true};
    res.send(out);
});

router.route('/update').get(function (req, res, next) {
    if (!req.session.isLogin) {
        var out = {'result': true};
        res.send(out);
        return;
    }
    res.render('update');
}).post(function (req, res, next) {
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
