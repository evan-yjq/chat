var express = require('express');
var router = express.Router();
var userDB = require('../common/userDB');

//登录接口
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

//注册接口
router.route('/register').post(function (req, res) {
    userDB.PUT(req.body.account, req.body.password)
        .then(function (data) {
            if (data !== undefined){
                res.send(data.insertId+"");
            }else{
                res.send(null);
            }
        }).catch(function () {
            res.send(null);
        });
});

// router.route('/update').post(function (req, res) {
//     if (!req.session.isLogin) {
//         var out = {'result': false};
//         res.send(out);
//     } else {
//         userDB.UPDATE(req.session.userId, req.body.username, req.body.password, req.body.email)
//             .then(function () {
//                 req.session.username = req.body.username;
//                 // res.render('index', {log: "修改成功", isLogin: req.session.isLogin, username: req.session.username});
//                 var out = {'result': true, 'log': '修改成功', 'isLogin': req.session.isLogin, 'username': req.session.username};
//                 res.send(out);
//             }).catch(function (result) {
//             // res.render('update', {log: result});
//             var out = {'result': false, 'log': result};
//             res.send(out);
//         });
//     }
// });

module.exports = router;
