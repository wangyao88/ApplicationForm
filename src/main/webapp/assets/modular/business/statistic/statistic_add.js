var ProvinceInfoDlg = {
    data: {
        pid: "",
        pName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;


    // 让当前iframe弹层高度适应
    admin.iframeAuto();
    var ajax = new $ax(Feng.ctxPath + "/statistic/comboxs");
    var result = ajax.start();
    $.each(result.continuationDicts, function(index, continuationDict){
        $("#continuationSelect").append("<option value='"+continuationDict.dictId+"'>"+continuationDict.name+"</option>");
    });

    laydate.render({
        elem: '#beginDate',
        type: 'month'
    });

    laydate.render({
        elem: '#endDate',
        type: 'month'
    });

    form.render();

    // var hasDischargeNumWidth = $("#hasDischargeNum").width();
    // $("#medicalTreatmentSelect").width(hasDischargeNumWidth);
    // $("#continuationSelect").width(hasDischargeNumWidth);

    // 点击省市时
    $('#provinceName').click(function () {
        var formName = encodeURIComponent("parent.ProvinceInfoDlg.data.pName");
        var formId = encodeURIComponent("parent.ProvinceInfoDlg.data.pid");
        var treeUrl = encodeURIComponent("/province/tree");

        layer.open({
            type: 2,
            title: '父级省市',
            area: ['300px', '200px'],
            content: Feng.ctxPath + '/system/commonTree?formName=' + formName + "&formId=" + formId + "&treeUrl=" + treeUrl,
            end: function () {
                $("#provinceId").val(ProvinceInfoDlg.data.pid);
                $("#provinceName").val(ProvinceInfoDlg.data.pName);
            }
        });
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/statistic/add", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
});