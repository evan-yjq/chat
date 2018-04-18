var mysql = require('mysql');
var Promise = require('bluebird');
var conn = require('./conn');
var pool = mysql.createPool(conn);

var sql = {
    SELECT: 'select * from user WHERE account=?',
    INSERT: 'insert into user(account, password) values(?, ?)',
    DELETE: 'delete form user where account=?',
    CHECK: 'select * from user where account=? or email=?',
    GETFRIENDS:'select u.id,u.account,u.nickname,u.profile,f.relationship,c.classification from c_friends f ' +
                'left join c_classification c on f.user_id=c.user_id and f.classification_id=c.id ' +
                'left join chat.user u on f.friend_id=u.id where f.user_id=?'
};

//异步操作
function promiseQuery(sql, sqlParams) {
    return new Promise(function (resolve, reject) {
        pool.getConnection(function (err, connection) {
            connection.query(sql, sqlParams, function (err, results) {
                if (err) {
                    reject('[ERROR]' + err.message);
                } else {
                    resolve(results);
                }
                connection.release();
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
function put(account, password, email) {
    promiseQuery(sql.CHECK,[account,email])
        .then(function (re) {
            if (re === undefined || re.length === 0){
                return promiseQuery(sql.INSERT, [account, password, email]);
            }else {
                return Promise.rejected();
            }
        });
}

//删除用户
function del(account) {
    return promiseQuery(sql.DELETE, account)
}

//验证登录
function verify(account) {
    return promiseQuery(sql.CHECK, [account, account])
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
        promiseQuery(sql.SELECT2, [username, id])
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

function get_friends(id) {
    return promiseQuery(sql.GETFRIENDS, id)
}

module.exports = {
    SEARCH: search,
    PUT: put,
    DEL: del,
    VERIFY: verify,
    UPDATE: update,
    FRIENDS:get_friends
};