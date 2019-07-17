layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 申请单管理--统计信息管理
     */
    var Statistic = {
        tableId: "statisticTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    Statistic.initColumn = function () {
        return [[
            {field: 'statisticId', sort: true, title: '统计信息编号'},
            {field: 'applicationFormId', sort: true, title: '申请单编号'},
            {field: 'provinceName', sort: true, title: '地市'},
            {field: 'mainNum', sort: true, title: '主单数'},
            {field: 'detailNum', sort: true, title: '明细数'},
            {field: 'hasDischargeNum', sort: true, title: '有出院诊断'},
            {field: 'medicalTreatment', sort: true, title: '就医方式 数量'},
            {field: 'beginDate', sort: true, title: '开始日期'},
            {field: 'endDate', sort: true, title: '结束日期'},
            {field: 'continuation', sort: true, title: '连续情况'},
            {field: 'createUserName', sort: true, title: '创建者'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Statistic.search = function () {
        var queryData = {};
        queryData['applicationFormId'] = $("#applicationFormId").val();
        queryData['provinceName'] = $("#provinceName").val();
        table.reload(Statistic.tableId, {where: queryData});
    };

    /**
     * 弹出添加统计信息
     */
    Statistic.openAddStatistic = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加统计信息',
            content: Feng.ctxPath + '/statistic/statistic_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Statistic.tableId);
            }
        });
    };

    /**
     * 点击编辑统计信息
     *
     * @param data 点击按钮时候的行数据
     */
    Statistic.onEditStatistic = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '统计信息详情',
            content: Feng.ctxPath + '/statistic/statistic_update?statisticId=' + data.statisticId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Statistic.tableId);
            }
        });
    };

    /**
     * 点击删除统计信息
     *
     * @param data 点击按钮时候的行数据
     */
    Statistic.onDeleteStatistic = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/statistic/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Statistic.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("statisticId", data.statisticId);
            ajax.start();
        };
        Feng.confirm("是否删除统计信息?", operation);
    };

    Statistic.onDetailStatistic = function (data) {
        window.location.href = '/applicationDetail?statisticId=' + data.statisticId;
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Statistic.tableId,
        url: Feng.ctxPath + '/statistic/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Statistic.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Statistic.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Statistic.openAddStatistic();
    });

    // 工具条点击事件
    table.on('tool(' + Statistic.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Statistic.onEditStatistic(data);
        } else if (layEvent === 'delete') {
            Statistic.onDeleteStatistic(data);
        } else if (layEvent === 'detail') {
            Statistic.onDetailStatistic(data);
        }
    });
});
