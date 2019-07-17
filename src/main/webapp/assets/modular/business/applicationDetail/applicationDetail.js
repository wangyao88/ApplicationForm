layui.use(['table', 'ax'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var treetable = layui.treetable;

    var ApplicationDetail = {
        tableId: "applicationDetailTable"
    };

    /**
     * 初始化表格的列
     */
    ApplicationDetail.initColumn = function () {
        return [[
            {field: 'applicationDetailId', title: '明细信息编号'},
            {field: 'statisticId', hide: true, title: '统计信息ID'},
            {field: 'detailDate', sort: true, title: '日期'},
            {field: 'num', sort: true, title: '数量'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'createUserName', sort: true, title: '创建人'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    ApplicationDetail.search = function() {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        ApplicationDetail.initTable(ApplicationDetail.tableId, queryData);
    };

    /**
     * 弹出添加对话框
     */
    ApplicationDetail.openAddDlg = function() {
        window.location.href = Feng.ctxPath + '/applicationDetail/applicationDetail_add?statisticId=' + $("#statisticId").val();
    };

    ApplicationDetail.openBatchAddDlg = function() {
        window.location.href = Feng.ctxPath + '/applicationDetail/applicationDetail_batch_add?statisticId=' + $("#statisticId").val();
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    ApplicationDetail.openEditDlg = function (data) {
        window.location.href = Feng.ctxPath + '/applicationDetail/applicationDetail_update?applicationDetailId=' + data.applicationDetailId;
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    ApplicationDetail.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/applicationDetail/delete", function (data) {
                Feng.success("删除成功!");
                ApplicationDetail.search();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("applicationDetailId", data.applicationDetailId);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    /**
     * 渲染表格
     */
    var tableResult =
    ApplicationDetail.initTable = function (applicationDetailId, data) {
        return table.render({
            elem: '#' + applicationDetailId,
            url:  Feng.ctxPath + '/applicationDetail/list?statisticId=' + $("#statisticId").val(),
            page: true,
            height: "full-98",
            cellMinWidth: 100,
            cols: ApplicationDetail.initColumn()
        });
    };

    ApplicationDetail.initTable(ApplicationDetail.tableId);

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        ApplicationDetail.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        ApplicationDetail.openAddDlg();
    });

    $('#btnBatchAdd').click(function () {
        ApplicationDetail.openBatchAddDlg();
    });

    // 关闭页面
    $('#btnBack').click(function () {
        window.location.href = Feng.ctxPath + "/statistic";
    });

    // 工具条点击事件
    table.on('tool(' + ApplicationDetail.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            ApplicationDetail.openEditDlg(data);
        } else if (layEvent === 'delete') {
            ApplicationDetail.onDeleteItem(data);
        }
    });
});
