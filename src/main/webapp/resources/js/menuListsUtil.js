/**
 * Class serves menuList.jsp
 * works with menuLists of specific restaurant
 */

/*url for exchange JSON data between DataTable and server*/
var ajaxUrl = '/ajax/menuLists/';

/*url for exchange JSON data between main form DataTable
 *represents menu lists, and server, using filter by status*/
var ajaxUrlWithFilter = '/ajax/menuLists/filterByEnabled/';

/*url for link to order_by_dish.jsp*/
var goDishes = '/dishes/';

/*variable links to DataTable represents menu lists in menuList.jsp*/
var datatableApi;

/*variable links to menuLists.edit resource bundle */
var editTitleKey ="menuLists.edit";

/*variable links to menuLists.add resource bundle */
var addTitleKey ="menuLists.add";

/*variable for save current filter value*/
var currentFilterValue = "ALL";

/*function to update DataTable by data from server*/
function updateTable(enabledKey) {
    if (enabledKey == "ALL") {
        $.get(ajaxUrl, updateTableByData);
    }
    else {
        $.get(ajaxUrlWithFilter+enabledKey, updateTableByData);
    }
    currentFilterValue = enabledKey;
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
            /*add column with image depending of Enabled*/
            {
                "orderable": false,
                "data": "enabled",
                "render": function (data, type, row) {
                    if (type == 'display') {
                        if (data){
                            return '<img  src="resources/pictures/menulist.png" />';
                        }
                        else {
                            return '<img  src="resources/pictures/cross.png" />';
                        }
                    }
                    return null;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type == 'display') {
                        return formatDate(date);
                    }
                    return date;
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
                "render": renderEditMenuListBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": renderDeleteBtnWithFilter
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ],
        /*customize row style depending of Enabled*/
        "createdRow": function (row, data, dataIndex) {
            if (!data.enabled) {
                $(row).addClass("disabled");
            }
        },
        "initComplete": makeEditable
    });

    $.datetimepicker.setLocale(localeCode);

    /*set field with datetimepicker*/
    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});

/*function for link to dishes.jsp*/
function linkBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-primary" onclick=location.href="'+ goDishes + row.id +'">' +
            '<span class="glyphicon glyphicon-list-alt"></span></a>';
    }
}

/*render function draw button for update row*/
function renderEditMenuListBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-primary" onclick="updateRow(' + row.id + ');">' +
            '<span class="glyphicon glyphicon-time"></span></a>';
    }
}

/*method to update row in tables */
function updateRow(id) {
    $('#modalTitle').html(i18n[editTitleKey]);
    $.get(ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            if (key === "enabled") {
                $("#" + key).prop("checked", value);
            }
            else {
                form.find("input[name='" + key + "']").val(value);
            }
        });
        $('#editRow').modal();
    });
}