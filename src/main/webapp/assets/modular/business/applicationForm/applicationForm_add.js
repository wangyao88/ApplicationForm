$(document).ready(function () {
    $.ajax({
        type : "GET",
        contentType: "application/json;charset=UTF-8",
        url : "/applicationForm/comboxs",
        success : function(data) {
            var dicts = data.dicts;
            $.each(dicts, function(index, dict){
                $("#applicationFormTypeSelect").append("<option value='dict"+dict.dictId+"'>"+dict.name+"</option>");
            });
            $('#applicationFormTypeSelect').searchableSelect({
                afterSelectItem: function () {
                    
                }
            });

            
            var users = data.users;
            $.each(users, function(index, user){
                $("#applicationUserSelect").append("<option value='application"+user.userId+"'>"+user.name+"</option>");
                $("#receiveUserSelect").append("<option value='receive"+user.userId+"'>"+user.name+"</option>");
            });
            $('#applicationUserSelect').searchableSelect();
            $('#receiveUserSelect').searchableSelect();

            var projects = data.projects;
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
        window.location.href = '/applicationForm';
    });

    $("#save_application_btn").click(saveApplicationForm);
});