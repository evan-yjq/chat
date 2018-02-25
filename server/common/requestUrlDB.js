var mysql = require('mysql');
var Promise = require('bluebird');
var conn = require('./conn');
var pool = mysql.createPool(conn);

var sql = {
    SELECT: 'select * from request_url',
    INSERT: 'insert into request_url(key, value) values(?, ?)',
    DELETE: 'delete form request_url where key=?',
    UPDATE: 'update request_url set value=? where key=?'
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

//新增链接
function put(key, value) {
    return promiseQuery(sql.INSERT,[key,value]);
}

//查找所有链接
function searchAll() {
    return promiseQuery(sql.SELECT);
}

//删除链接
function del(key) {
    return promiseQuery(sql.DELETE,key);
}

//更新链接
function update(key,value) {
    return promiseQuery(sql.UPDATE,[value,key]);
}

module.exports = {
    SEARCH_ALL: searchAll,
    PUT: put,
    DEL: del,
    UPDATE: update
};
