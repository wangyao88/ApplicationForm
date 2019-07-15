$(document).ready(function () {
    laydate.render({
        elem: '#applicationTime',
        value: new Date()
    });

    laydate.render({
        elem: '#receiveTime',
        value: new Date()
    });

    function getIdFromUrl(param) {
        return window.location.search.substr(param.length+2);
    }

    $.ajax({
        type : "GET",
        contentType: "application/json;charset=UTF-8",
        url : "/applicationForm/detail/" + getIdFromUrl("applicationFormId"),
        success : function(data) {
            var applicationForm = data.applicationForm;
            $("#applicationFormId").val(applicationForm.applicationFormId);
            $("#applicationTime").val(applicationForm.applicationTimeStr);
            $("#receiveTime").val(applicationForm.receiveTimeStr);
            descriptionEditor.txt.html(applicationForm.description);
            useTextEditor.txt.html(applicationForm.useText);

            var dicts = data.dicts;
            $.each(dicts, function(index, dict){
                $("#applicationFormTypeSelect").append("<option value='dict"+dict.dictId+"'>"+dict.name+"</option>");
            });

            $("#applicationFormTypeSelect option[value='dict"+applicationForm.applicationFormTypeId+"']").attr("selected", true);
            $('#applicationFormTypeSelect').searchableSelect();

            var users = data.users;
            $.each(users, function(index, user){
                $("#applicationUserSelect").append("<option value='application"+user.userId+"'>"+user.name+"</option>");
                $("#receiveUserSelect").append("<option value='receive"+user.userId+"'>"+user.name+"</option>");
            });
            $("#applicationUserSelect option[value='application"+applicationForm.applicationUser+"']").attr("selected", true);
            $('#applicationUserSelect').searchableSelect();
            $("#receiveUserSelect option[value='receive"+applicationForm.receiveUser+"']").attr("selected", true);
            $('#receiveUserSelect').searchableSelect();

            var projects = data.projects;
            $.each(projects, function(index, project){
                $("#projectSelect").append("<option value='project"+project.projectId+"'>"+project.title+"</option>");
            });
            $("#projectSelect option[value='project"+applicationForm.projectId+"']").attr("selected", true);
            $('#projectSelect').searchableSelect();
        },
        error : function(e){
        }
    });

    $("#cancel_application_btn").click(function () {
        window.location.href = '/applicationForm';
    });

    $("#save_application_btn").click(saveApplicationForm);
});