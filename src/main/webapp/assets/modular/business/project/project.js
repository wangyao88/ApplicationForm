layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 申请单管理--项目管理
     */
    var Project = {
        tableId: "projectTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    Project.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'projectId', hide: true, sort: true, title: 'id'},
            {field: 'title', sort: true, title: '名称'},
            {field: 'provinceName', sort: true, title: '省市'},
            {field: 'createrName', sort: true, title: '创建者'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Project.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(Project.tableId, {where: queryData});
    };

    /**
     * 弹出添加项目
     */
    Project.openAddProject = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加项目',
            content: Feng.ctxPath + '/project/project_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Project.tableId);
            }
        });
    };

    /**
     * 点击编辑项目
     *
     * @param data 点击按钮时候的行数据
     */
    Project.onEditProject = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '项目详情',
            content: Feng.ctxPath + '/project/project_update?projectId=' + data.projectId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Project.tableId);
            }
        });
    };

    /**
     * 点击删除项目
     *
     * @param data 点击按钮时候的行数据
     */
    Project.onDeleteProject = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/project/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Project.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("projectId", data.projectId);
            ajax.start();
        };
        Feng.confirm("是否删除项目 " + data.title + "?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Project.tableId,
        url: Feng.ctxPath + '/project/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Project.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Project.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Project.openAddProject();
    });

    // 工具条点击事件
    table.on('tool(' + Project.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Project.onEditProject(data);
        } else if (layEvent === 'delete') {
            Project.onDeleteProject(data);
        }
    });
});
