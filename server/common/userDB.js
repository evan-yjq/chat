var mysql = require('mysql');
var Promise = require('bluebird');
var conn = require('./conn');
var pool = mysql.createPool(conn);

var sql = {
    SELECT: 'select * from user WHERE username=?',
    INSERT: 'insert into user(username, password, email) values(?, ?, ?)',
    DELETE: 'delete form user where username=?',
    SELECT2: 'select * from user WHERE username = ? and id != ?'
};

//异步操作
function promiseQuery(sql, sqlParams) {
    return new Promise(function (resolve, reject) {
        pool.getConnection(function (err, connection) {
            connection.query(sql, sqlParams, function (err, results) {
                console.log('sql:\n',sql,'/',sqlParams,'\nresults:\n',results);
                if (err) {
                    reject('[ERROR]' + err.message);
                } else {
                    resolve(results);
                }
            });
        });
    });
}

//根据关键词模糊查找用户
function search(keyword) {
    var sql = "select * from user where username like '%" + keyword + "%'";
    return promiseQuery(sql);
}

//新增用户
function put(username, password, email) {
    return promiseQuery(sql.SELECT, username)
        .then(function (data) {
            return promiseQuery(sql.INSERT, [username, password, email]);
        });
}

//删除用户
function del(username) {
    return promiseQuery(sql.DELETE, username)
}

//验证登录
function verify(username, password) {
    return promiseQuery(sql.SELECT, username)
        .then(function (data) {
            if (data.length > 0 && data[0].password === password) {
                return Promise.resolve(data[0].id);
            } else {
                return Promise.reject('用户名密码错误');
            }
        });
}

//更新用户资料
function update(id, username, password, email) {
    var a = '';
    if (username !== '') a = a + "username='" + username + "'";
    if (password !== '') {
        if (a !== '') a = a + ',';
        a = a + "password='" + password + "'";
    }
    if (email !== '') {
        if (a !== '') a = a + ',';
        a = a + "email='" + email + "'";
    }
    if (a === '') {
        return Promise.reject('未作任何修改');
    } else {
        return promiseQuery(sql.SELECT2, [username, id])
            .then(function (data) {
                if (data.length > 0) {
                    return Promise.reject('用户名已存在');
                } else {
                    var sql = "update user set " + a + " where id = '" + id + "'";
                    return promiseQuery(sql)
                }
            });
    }
}

module.exports = {
    SEARCH: search,
    PUT: put,
    DEL: del,
    VERIFY: verify,
    UPDATE: update
};