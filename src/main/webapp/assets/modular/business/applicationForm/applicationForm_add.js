$(document).ready(function () {
    $.ajax({
        type : "GET",
        contentType: "application/json;charset=UTF-8",
        url : "/dict/allDict",
        success : function(dicts) {
            $.each(dicts, function(index, dict){
                $("#aapplicationFormTypeSelect").append("<option value='dict"+dict.dictId+"'>"+dict.name+"</option>");
            });
            $('#aapplicationFormTypeSelect').searchableSelect();
        },
        error : function(e){
        }
    });

    $.ajax({
        type : "GET",
        contentType: "application/json;charset=UTF-8",
        url : "/mgr/allUser",
        success : function(users) {
            $.each(users, function(index, user){
                $("#applicationUserSelect").append("<option value='application"+user.userId+"'>"+user.name+"</option>");
                $("#receiveUserSelect").append("<option value='receive"+user.userId+"'>"+user.name+"</option>");
            });
            $('#applicationUserSelect').searchableSelect();
            $('#receiveUserSelect').searchableSelect();
        },
        error : function(e){
        }
    });

    $.ajax({
        type : "GET",
        contentType: "application/json;charset=UTF-8",
        url : "/project/allProject",
        success : function(projects) {
            $.each(projects, function(index, project){
                $("#projectSelect").append("<option value='project"+project.projectId+"'>"+project.title+"</option>");
            });
            $('#projectSelect').searchableSelect();
        },
        error : function(e){
        }
    });

    laydate.render({
        elem: '#applicationTime',
        value: new Date()
    });

    laydate.render({
        elem: '#receiveTime',
        value: new Date()
    });

    $("#cancel_application_btn").click(function () {
        window.location.href = '/applicationForm/list?condition=""';
    });

    $("#save_application_btn").click(function () {
        var applicationFormTypeId = $("#applicationFormTypeId").val();
        var applicationFormId = $("#applicationFormId").val();
        var applicationUser = $("#applicationUser").val();
        var receiveUser = $("#receiveUser").val();
        var projectId = $("#projectId").val();
        var applicationTime = $("#applicationTime").val();
        var receiveTime = $("#receiveTime").val();
        var description = descriptionEditor.txt.html();
        var use = useEditor.txt.html();
        // validate();
        $.ajax({
            type : "GET",
            contentType: "application/json;charset=UTF-8",
            url : "/applicationForm/add",
            data: {
                applicationFormId: applicationFormId,
                applicationFormTypeId: applicationFormTypeId,
                applicationUser: applicationUser,
                receiveUser: receiveUser,
                projectId: projectId,
                applicationTime: applicationTime,
                receiveTime: receiveTime,
                description: description,
                use: use
            },
            success : function(result) {
                window.location.href = '/applicationForm/list?condition=""';
            },
            error : function(e){
            }
        });
    });
});


// $(function(){
// var onAutocompleteSelect =function(value, data) {
//     //根据返回结果自定义一些操作
// };
// var options = {
//     serviceUrl: Feng.ctxPath + "/mgr/searchByName",//获取数据的后台页面
//     // width: 140,//提示框的宽度
//     delimiter: /(,|;)\s*/,//分隔符
//     onSelect: onAutocompleteSelect,//选中之后的回调函数
//     deferRequestBy: 0, //单位微秒
//     noCache: true //是否启用缓存 默认是开启缓存的
// };
// a1 = $('#applicationUser').autocomplete(options);

    // 表单提交事件
    // form.on('submit(btnSubmit)', function (data) {
    //     var ajax = new $ax(Feng.ctxPath + "/project/add", function (data) {
    //         Feng.success("添加成功！");
    //
    //         //传给上个页面，刷新table用
    //         admin.putTempData('formOk', true);
    //
    //         //关掉对话框
    //         admin.closeThisDialog();
    //     }, function (data) {
    //         Feng.error("添加失败！" + data.responseJSON.message)
    //     });
    //     ajax.set(data.field);
    //     ajax.start();
    // });
// });
