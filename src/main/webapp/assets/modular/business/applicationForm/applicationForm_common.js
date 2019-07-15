function saveApplicationForm() {
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
            type : "POST",
            url : applicationFormId === empty ? "/applicationForm/add" : "/applicationForm/update",
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
                window.location.href = '/applicationForm';
            },
            error : function(e){
            }
        });
    }
}