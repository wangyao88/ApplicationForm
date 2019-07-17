layui.use(['table', 'admin', 'ax', 'ztree'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;

    /**
     * 系统管理--省市管理
     */
    var Province = {
        tableId: "provinceTable",
        condition: {
            provinceId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Province.initColumn = function () {
        return [[
            {field: 'provinceId', sort: true, title: '省市编号'},
            {field: 'simpleName', sort: true, title: '省市简称'},
            {field: 'fullName', sort: true, title: '省市全称'},
            {field: 'sort', sort: true, title: '排序'},
            {field: 'description', sort: true, title: '备注'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Province.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['provinceId'] = Province.condition.provinceId;
        table.reload(Province.tableId, {where: queryData});
    };

    /**
     * 选择省市时
     */
    Province.onClickProvince = function (e, treeId, treeNode) {
        Province.condition.provinceId = treeNode.id;
        Province.search();
    };

    /**
     * 弹出添加
     */
    Province.openAddProvince = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加省市',
            content: Feng.ctxPath + '/province/province_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Province.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    Province.exportExcel = function () {
        var checkRows = table.checkStatus(Province.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑省市
     *
     * @param data 点击按钮时候的行数据
     */
    Province.onEditProvince = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改省市',
            content: Feng.ctxPath + '/province/province_update?provinceId=' + data.provinceId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Province.tableId);
            }
        });
    };

    /**
     * 点击删除省市
     *
     * @param data 点击按钮时候的行数据
     */
    Province.onDeleteProvince = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/province/delete", function () {
                Feng.success("删除成功!");
                table.reload(Province.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("provinceId", data.provinceId);
            ajax.start();
        };
        Feng.confirm("是否删除省市 " + data.simpleName + "?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Province.tableId,
        url: Feng.ctxPath + '/province/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Province.initColumn()
    });

    //初始化左侧省市树
    var ztree = new $ZTree("provinceTree", "/province/tree");
    ztree.bindOnClick(Province.onClickProvince);
    ztree.init();
    // zTree.getZTreeObj("provinceTree").expandAll(false);

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Province.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Province.openAddProvince();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Province.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Province.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Province.onEditProvince(data);
        } else if (layEvent === 'delete') {
            Province.onDeleteProvince(data);
        }
    });
});
