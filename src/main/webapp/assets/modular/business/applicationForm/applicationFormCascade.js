function cascade(item) {
    console.log(item);
    var id = item[0].dataset.value;
    if(id.indexOf('dict') != -1) {
        $("#applicationFormTypeId").val(id.replace("dict", ""));
    }
    if(id.indexOf('application') != -1) {
        $.ajax({
            type : "GET",
            contentType: "application/json;charset=UTF-8",
            url : "/mgr/cascadeUser",
            data: {
                userId: id.replace("application", "")
            },
            dataType: "json",
            success : function(result) {
                var user = result.data;
                $("#applicationUser").val(user.userId);
                $("#applicationUserDeptName").val(user.deptName);
                $("#applicationUserEmail").val(user.email);
                $("#applicationUserPhone").val(user.phone);
            },
            error : function(e){
                alert(e);
            }
        });
    }
    if(id.indexOf('receive') != -1) {
        $("#receiveUser").val(id.replace("receive", ""));
    }
    if(id.indexOf('project') != -1) {
        $("#projectId").val(id.replace("project", ""));
    }
}