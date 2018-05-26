$(function () {
    // 数据对象
    var batchTask = {};

    var dataSourceArray = []; // 数据源列表
    var sourceDBArray = []; // Source 数据库
    var sourceTableArray = []; // Source 表
    var timeColumnObject = {}; // 时间列
    var sinkDBArray = []; // Sink 数据库

    // Vue 实例
    var app = new Vue({
        el: '#app',
        data: {
            batchTask: batchTask,
            dataSourceArray: dataSourceArray,
            sourceDBArray: sourceDBArray,
            sourceTableArray: sourceTableArray,
            timeColumnObject: timeColumnObject,
            sinkDBArray: sinkDBArray
        },
        watch: {
            "batchTask.datasource_id": function (oldValue) {
                queryAllSourceDB(oldValue);
            },
            "batchTask.source_db": function(oldValue) {
                queryAllSourceTable(app.batchTask.datasource_id, oldValue);
            },
            "batchTask.source_table": function (oldValue) {
                queryAllTimeColumn(app.batchTask.datasource_id, app.batchTask.source_db, oldValue);
            },
            "batchTask.mode": function (oldValue) {
                if (oldValue == 'COMPLETE') app.batchTask.time_column = null;
            }
        },
        methods: {
            insertOrUpdateBatchTask: function () {
                if (_.isNull(id)) {
                    insertBatchTask(app.batchTask);
                } else {
                    updateBatchTask(app.batchTask);
                }

            }
        }
    });

    /**
     * 新增批量任务
     *
     * @param task 批量任务
     *
     * */
    function insertBatchTask(task) {
        $.ajax('/api/batch', {
            method: 'PUT',
            data: task
        }).done(function () {
            alert('保存成功！');
            window.location.href = '/batch/';
        });
    }

    /**
     * 修改批量任务
     *
     * @param task 批量任务
     *
     * */
    function updateBatchTask(task) {
        $.ajax('/api/batch', {
            method: 'POST',
            data: task
        }).done(function () {
            alert('保存成功！');
            window.location.href = '/batch/';
        });
    }

    /**
     * 查询所有数据源
     * */
    function queryAllDataSource() {
        $.get('/api/datasource').done(function (data) {
            app.dataSourceArray = data;
        });
    }

    /**
     * 查询所有 Source 数据库
     *
     * @param id 数据源 ID
     *
     * */
    function queryAllSourceDB(id) {
        var url = '/api/datasource/db/' + id;
        $.get(url).done(function (data) {
            app.sourceDBArray = data;
        });
    }

    /**
     * 查询所有 Sink 数据库
     *
     * */
    function queryAllSinkDB() {
        $.get('/api/hive/db').done(function (data) {
            app.sinkDBArray = data;
        });
    }

    /**
     * 查询所有 Source 表
     *
     * @param id 数据源 ID
     * @param db 数据库名
     *
     * */
    function queryAllSourceTable(id, db) {
        var url = '/api/datasource/table/' + id + '/' + db;
        $.get(url).done(function (data) {
            app.sourceTableArray = data;
        });
    }

    /**
     * 查询所有时间列
     *
     * @param id
     * @param db
     * @param table
     *
     * */
    function queryAllTimeColumn(id, db, table) {
        var url = '/api/datasource/timecolumn/' + id + '/' + db + '/' + table;
        $.get(url).done(function (data) {
            app.timeColumnObject = data;
        });
    }

    /**
     * 根据 ID 查询批量任务
     *
     * @param id 批量任务 ID
     *
     * */
    function queryBatchTaskById(id) {
        var url = '/api/batch/' + id;
        $.get(url).done(function (data) {
            app.batchTask = data;
        });
    }

    queryAllDataSource();
    queryAllSinkDB();
    if (!_.isNull(id)) {
        queryBatchTaskById(id);
        app.batchTask.id = id;
    }
});