layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 申请单管理--申请单管理
     */
    var ApplicationForm = {
        tableId: "applicationFormTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    ApplicationForm.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'applicationFormId', sort: true, title: '编号'},
            {field: 'applicationFormTypeName', sort: true, title: '申请单类型'},
            {field: 'applicationUserName', sort: true, title: '申请人'},
            {field: 'projectTitle', sort: true, title: '所属项目'},
            {field: 'applicationTime', sort: true, title: '申请日期'},
            {field: 'applicationUserDeptName', sort: true, title: '使用部门'},
            {field: 'applicationUserEmail', sort: true, title: 'Email'},
            {field: 'applicationUserPhone', sort: true, title: '联系电话'},
            {field: 'receiveUserName', sort: true, title: '接收人'},
            {field: 'receiveTime', sort: true, title: '接收日期'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    ApplicationForm.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(ApplicationForm.tableId, {where: queryData});
    };

    /**
     * 弹出添加申请单
     */
    ApplicationForm.openAddApplicationForm = function () {
        window.location.href = Feng.ctxPath + "/applicationForm/applicationForm_add";
        // admin.putTempData('formOk', false);
        // top.layui.admin.open({
        //     type: 2,
        //     title: '添加申请单',
        //     content: Feng.ctxPath + '/applicationForm/applicationForm_add',
        //     end: function () {
        //         admin.getTempData('formOk') && table.reload(ApplicationForm.tableId);
        //     }
        // });
    };

    /**
     * 点击编辑申请单
     *
     * @param data 点击按钮时候的行数据
     */
    ApplicationForm.onEditApplicationForm = function (data) {
        window.location.href = Feng.ctxPath + '/applicationForm/applicationForm_update?applicationFormId=' + data.applicationFormId;
        // admin.putTempData('formOk', false);
        // top.layui.admin.open({
        //     type: 2,
        //     title: '申请单详情',
        //     content: Feng.ctxPath + '/applicationForm/applicationForm_update?applicationFormId=' + data.applicationFormId,
        //     end: function () {
        //         admin.getTempData('formOk') && table.reload(ApplicationForm.tableId);
        //     }
        // });
    };

    /**
     * 点击删除申请单
     *
     * @param data 点击按钮时候的行数据
     */
    ApplicationForm.onDeleteApplicationForm = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/applicationForm/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(ApplicationForm.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("applicationFormId", data.applicationFormId);
            ajax.start();
        };
        Feng.confirm("是否删除申请单?", operation);
    };

    ApplicationForm.onImportApplicationForm = function(data) {
        window.location.href = Feng.ctxPath + '/statistic/statistic_import?applicationFormId=' + data.applicationFormId;
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + ApplicationForm.tableId,
        url: Feng.ctxPath + '/applicationForm/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: ApplicationForm.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        ApplicationForm.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        ApplicationForm.openAddApplicationForm();
    });

    // 工具条点击事件
    table.on('tool(' + ApplicationForm.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            ApplicationForm.onEditApplicationForm(data);
        } else if (layEvent === 'delete') {
            ApplicationForm.onDeleteApplicationForm(data);
        } else if (layEvent === 'import') {
            ApplicationForm.onImportApplicationForm(data);
        }
    });
});
