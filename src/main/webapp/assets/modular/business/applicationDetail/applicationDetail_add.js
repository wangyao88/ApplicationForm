layui.use(['form', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var laydate = layui.laydate;

    laydate.render({
        elem: '#detailDate',
        type: 'month'
    });

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/applicationDetail/add", function (data) {
            Feng.success("添加成功！");
            window.location.href = Feng.ctxPath + "/applicationDetail?statisticId=" + $("#statisticId").val();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //返回按钮
    $("#backupPage").click(function () {
        window.location.href = Feng.ctxPath + "/applicationDetail?statisticId=" + $("#statisticId").val();
    });
});