var express = require('express');
var router = express.Router();
var userDB = require('../common/userDB');

//登录接口
router.route('/sign_in').post(function (req, res) {
    userDB.VERIFY(req.body.account)
        .then(function (data) {
            if (data.length > 0 && data[0].password === req.body.password) {
                res.send(data[0]);
            }else{
                res.send(null)
            }
        }).catch(function () {
            res.send(null)
        });
});

//注册接口
router.route('/register').post(function (req, res) {
    userDB.PUT(req.body.account, req.body.password, req.body.email)
        .then(function (data) {
            if (data !== undefined){
                var dstPath = './public/users/' + data.insertId;
                if (!fs.existsSync(dstPath)){
                    fs.mkdirSync(dstPath);
                }
                res.send(data.insertId+"");
            }else{
                res.send(null)
            }
        }).catch(function () {
            res.send(null)
        })
});

router.route('/get_friends').post(function (req, res) {
    userDB.FRIENDS(req.body.id)
        .then(function (data) {
            if (data !== undefined && data.length > 0){
                res.send(data);
            }else{
                res.send(null)
            }
        }).catch(function () {
            res.send(null)
        })
});

router.route('/add_friend').post(function (req, res) {
    userDB.ADD_FRIEND(req.body.userId,req.body.friendId)
        .then(function () {
            userDB.ADD_FRIEND(req.body.friendId,req.body.userId)
                .then(function () {
                    res.send('ok')
                })
        }).catch(function () {
        res.send(null)
    })

});

var process = require('child_process');

router.route('/face_dist').post(function(req, res){
    process.exec('/home/face/cmake-build-debug/face '+req.body.userId+' '+ req.body.type,function (error, stdout) {
        if (error !== null) {
            console.log('exec error: ' + error);
        }
        var arr = stdout.split('\n');
        arr.splice(0,1);
        console.log(arr);
        if (req.body.type === '1'){
            if (arr[0] === 'ok') {
                userDB.UPDATE_BIND_FACE('true', req.body.userId);
                res.send('ok');
            }else{
                res.send(null)
            }
        }else if (req.body.type === '2'){
            if (arr[[arr.length-1]] === 'true'){
                res.send(arr[arr.length-1]+'-'+arr[0]);
            } else if (arr.length === 1) {
                res.send(arr[0])
            }else{
                res.send('不是本人')
            }
        }
    });
});

var multiparty = require('multiparty');
var fs = require('fs');
router.post('/face_upload',function (req,res) {
    /* 生成multiparty对象，并配置上传目标路径 */
    var form = new multiparty.Form();
    /* 设置编辑 */
    form.encoding = 'utf-8';
    //设置文件存储路径
    form.uploadDir = './public/tmp';
    //设置文件大小限制
    form.maxFilesSize = 10 * 1024 * 1024;
    //上传后处理
    form.parse(req, function(err, fields, files) {
        if(err) {
            console.log('parse error:' + err);
        }else {
            var inputFile = files.img[0];
            var uploadedPath = inputFile.path;
            var arr = inputFile.originalFilename.split('-');
            var dstPath = './public/users/' + arr[0];
            if (!fs.existsSync(dstPath)){
                fs.mkdirSync(dstPath);
            }
            dstPath = dstPath + '/' + arr[1];
            //重命名为真实文件名
            fs.rename(uploadedPath, dstPath, function(err) {
                if(err) {
                    console.log('rename error:' + err);
                }else {
                    fs.unlink(uploadedPath, function() {
                        if (err) throw err;
                    });
                }
            })
        }
        res.writeHead(200, {'content-type': 'text/plain;charset=utf-8'});
        res.end('ok');
    })
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
