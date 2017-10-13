/**
 * Class serves restaurants.jsp
 * works with restaurants
 */

/*url for exchange JSON data between DataTable and server*/
var ajaxUrl = '/ajax/restaurants/';

/*url for link to menuLists.jsp*/
var goMenuLists = '/menuLists/';

/*variable links to DataTable represents restaurants in restaurants.jsp*/
var datatableApi;

/*variable links to restaurants.edit resource bundle */
var editTitleKey ="restaurants.edit";

/*variable links to restaurants.add resource bundle */
var addTitleKey ="restaurants.add";

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
            {
                "data": "name"
            },
            {
                "data": "address"
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
                0,
                "asc"
            ]
        ],
        "createdRow": "",
        "initComplete": makeEditable
    });
});

/*function for link to menuLists.jsp*/
function linkBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-primary" onclick=location.href="'+ goMenuLists + row.id +'">' +
            '<span class="glyphicon glyphicon-list-alt"></span></a>';
    }
}

