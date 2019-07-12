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
        var empty = "";
        var applicationFormTypeId = $("#applicationFormTypeId").val();
        var applicationFormId = $("#applicationFormId").val();
        var applicationUser = $("#applicationUser").val();
        var receiveUser = $("#receiveUser").val();
        var projectId = $("#projectId").val();
        var applicationTime = $("#applicationTime").val();
        var receiveTime = $("#receiveTime").val();
        var description = descriptionEditor.txt.html();
        var useText = useTextEditor.txt.html();
        if(applicationTime === empty) {
            alert("申请日期为必填项");
            return false;
        }else if(receiveTime === empty) {
            alert("接受日期为必填项");
            return false;
        }else if(descriptionEditor.txt.text() === empty && description.indexOf('img') === -1) {
            alert("内容描述为必填项");
            return false;
        }else if(useTextEditor.txt.text() === empty && useText.indexOf('img') === -1) {
            alert("用途为必填项");
            return false;
        }else{
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
                    useText: useText
                },
                success : function(result) {
                    window.location.href = '/applicationForm/list?condition=""';
                },
                error : function(e){
                }
            });
        }
    });
});