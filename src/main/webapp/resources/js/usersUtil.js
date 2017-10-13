/**
 * Class serves users.jsp
 * works with users
 */

/*url for exchange JSON data between DataTable and server*/
var ajaxUrl = 'ajax/admin/users/';

/*url for link to orders.jsp*/
var goOrdersUrl = '/orders/';

/*variable links to DataTable represents users in users.jsp*/
var datatableApi;

/*variable links to users.edit resource bundle */
var editTitleKey ="users.edit";

/*variable links to users.add resource bundle */
var addTitleKey ="users.add";

/*function to update DataTable by data from server*/
function updateTable() {
    $.get(ajaxUrl, updateTableByData);
}

/*document.ready function*/
$(function () {
    datatableApi = $('#datatable').DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "searching": false,
        "info": true,
        "columns": [
            /*add column with image depending of Role and Enabled*/
            {
                "orderable": false,
                "render": function (data, type, row) {
                    if (type == 'display') {
                        if (!row.enabled){
                            return '<img  src="resources/pictures/ghost.png" />';
                        }
                        var isAdmin = false;
                        for (var i = 0; i < row.roles.length; i++){
                            if (row.roles[i] === "ROLE_ADMIN"){
                                isAdmin = true;
                            }
                        }
                        if (isAdmin){
                            return '<img  src="resources/pictures/admin.png" />';
                        }
                        else {
                            return '<img  src="resources/pictures/user.png" />';
                        }
                    }
                    return null;
                }
            },
            {
                "data": "name"
            },
            /*adjust e-mail column*/
            {
                "data": "email",
                "render": function (data, type, row) {
                    if (type == 'display') {
                        return '<a href="mailto:' + data + '">' + data + '</a>';
                    }
                    return data;
                }
            },
            /*internationalization of enum Role values*/
            {
                "data": "roles",
                "render": function (data, type, row) {
                    if (type == 'display') {
                        var localRoles = [];
                        for (var i = 0; i < data.length; i++){
                            localRoles.push(i18n["roles." + data[i]])
                        }
                        return localRoles;
                    }
                    return data;
                }
            },
            {
                "data": "totalOrdersAmount",
                "render": function (date, type, row) {
                    return date.toFixed(2);
                }
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": linkBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                1,
                "asc"
            ]
        ],
        /*customize row style depending of Role and Enabled*/
        "createdRow": function (row, data, dataIndex) {
            var isAdmin = false;
            for (var i = 0; i < data.roles.length; i++){
                if (data.roles[i] === "ROLE_ADMIN"){
                    isAdmin = true;
                }
            }
            $(row).addClass(isAdmin ? 'isAdmin' : 'isUser');
            if (!data.enabled) {
                $(row).addClass("disabled");
            }
        },
        "initComplete": makeEditable
    });

    datatableApi.each(function(){
        if ($(this).text() == 'N') {
            $(this).css('background-color','#f00');
        }
    });
});

/*function for link to orders.jsp*/
function linkBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-primary" onclick=location.href="'+ goOrdersUrl + row.id +'">' +
            '<span class="glyphicon glyphicon-list-alt"></span></a>';
    }
}

/*render function draw button for update row*/
function renderEditBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-primary" onclick="updateRow(' + row.id + ');">' +
            '<span class="glyphicon glyphicon-edit"></span></a>';
    }
}

/*method to update row in tables */
function updateRow(id) {
    $('#modalTitle').html(i18n[editTitleKey]);
    $("#ROLE_ADMIN").prop("checked", false);
    $("#ROLE_USER").prop("checked", false);

    $.get(ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            if (key === "roles") {
                for (var i = 0; i < value.length; i++) {
                    $("#" + value[i]).prop("checked", true);
                }
            } 
            else if (key === "enabled") {
                $("#" + key).prop("checked", value);
            }
            else {
                form.find("input[name='" + key + "']").val(value);
            }
        });
        $('#editRow').modal();
    });
}