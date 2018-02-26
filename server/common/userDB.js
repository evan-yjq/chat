var mysql = require('mysql');
var Promise = require('bluebird');
var conn = require('./conn');
var pool = mysql.createPool(conn);

var sql = {
    SELECT: 'select * from user WHERE account=?',
    INSERT: 'insert into user(account, password, login_time) values(?, ?, ?)',
    DELETE: 'delete form user where account=?'
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
    var sql = "select * from user where account like '%" + keyword + "%'";
    return promiseQuery(sql);
}

//新增用户
function put(account, password, login_time) {
    return promiseQuery(sql.INSERT, [account, password, login_time]);
}

//删除用户
function del(account) {
    return promiseQuery(sql.DELETE, account)
}

//验证登录
function verify(account) {
    return promiseQuery(sql.SELECT, account)
}

//更新用户资料
function update(id, account, password, email) {
    var a = '';
    if (account !== '') a = a + "account='" + account + "'";
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