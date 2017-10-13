/**
 * Class serves orders_dishes.jsp
 * use to update exist orders
 * and as final 4-th step of creation new order
 * - specify the dishes quantities
 */

/*url for exchange JSON data between DataTable and server*/
var ajaxUrl = '/ajax/orders_dishes/';

/*url for finally load data to server*/
var ajaxOrdersUrl = '/ajax/orders/update';

/*url for send dishes ids to server*/
var ajaxDishesUrl = '/ajax/orders_dishes/dishesIds';

/*url for get all dishes of actual menu list from server*/
var ajaxAllDishesUrl = '/ajax/orders_dishes/allDishesOfMenuList';

/*url for get name and data of actual menu list from server*/
var ajaxCurrentMenuListUrl = '/ajax/orders_dishes/currentMenuList';

/*variable links to DataTable represents dishes and dishes quantities in orders_dishes.jsp*/
var datatableApi;

/*variable links to dishes.add resource bundle */
var addTitleKey ="dishes.add";

/*function to update DataTable by data from server*/
function updateTable() {
    $.get(ajaxUrl, updateTableByData,showTotalPrice());
    showTotalPrice();
}

/*DataTable represents dishes in modal window initialization*/
function dishDataTableInit() {
    dishDTApi = $('#dishDT').DataTable({
        "ajax": {
            "url": ajaxAllDishesUrl,
            "dataSrc": ""
        },
        "destroy": true,
        "paging": false,
        "searching": false,
        "info": true,
        "columns": [
            {
                "data": "description"
            },
            {
                "data": "price"
            },
            {
                'targets': 0,
                'className': 'dt-body-center',
                'searchable': false,
                'orderable': false,
                'width': '1%',
                'render': function ( data, type, row ) {
                    return '<input type="checkbox">';
                }

            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ],
        /*customize row style and checkbox value depending of choose*/
        "createdRow": function (row, data, dataIndex) {
            if (data.choose) {
                $(row).find('input[type="checkbox"]').prop('checked', true);
                $(row).addClass("selected");
            }
        }
    });
}

/*document.ready function*/
$(function () {
    datatableApi = $('#datatable').DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        rowId: 'id',
        "paging": false,
        "searching": false,
        "info": true,
        "columns": [
            {
                "data": "description"
            },
            {
                "data": "price"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": renderPlusBtn
            },
            {
                "data": "quantity",
                "className": "dt-center"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "className": "dt-center",
                "render": renderMinusBtn
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

/*open menuList to get other dishes*/
function openDishList() {

    $.ajax({
        type: "POST",
        url: ajaxDishesUrl,
        data: getRequestParamIdsOnly(datatableApi.rows().data() ),
        success: function () {
            //DataTable for dishes modal window initialisation
            dishDataTableInit();

            // Handle multiple choice checkbox of dishes
            $('#dishDT tbody').on('click', 'input[type="checkbox"]', function(e){
                var $row = $(this).closest('tr');
                if(this.checked){
                    $row.addClass('selected');
                } else {
                    $row.removeClass('selected');
                }
                e.stopPropagation();
            });

            /*open modal window for dish selection*/
            $('#selectDishes').modal();

            $.get(ajaxCurrentMenuListUrl, {}, function(data){
                $('#modalTitleMenuList').html(data);
            });
        }
    });
}

/*render function draw button for set quantity to zero*/
function renderDeleteBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-danger" onclick="deleteRow(' + row.id + ');">'+
            '<span class="glyphicon glyphicon-remove-circle"></span></a>';
    }
}

/*render function draw button for increase quantity of current Dish by 1*/
function renderPlusBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-default" onclick="plus('+row.id+');">'+
            '<span class="glyphicon glyphicon-plus"></span></a>';
    }
}

/*render function draw button for decrease quantity of current Dish by 1*/
function renderMinusBtn(data, type, row) {
    if (type == 'display') {
        return '<a class="btn btn-default" onclick="minus('+row.id+');">'+
            '<span class="glyphicon glyphicon-minus"></span></a>';
    }
}

/*function for increase quantity of current Dish by 1*/
function plus(id) {
    var index = '#' + id;
    var d = datatableApi.row(index).data();
    d.quantity++;
    datatableApi
        .row(index)
        .data( d )
        .draw();
    showTotalPrice();
}

/*function for decrease quantity of current Dish by 1*/
function minus(id) {
    var index = '#' + id;
    var d = datatableApi.row(index).data();
    d.quantity--;
    if (d.quantity <= 0){
        deleteRow(id);
        datatableApi.draw();
    }else {
        datatableApi
            .row(index)
            .data( d )
            .draw();
    }
    showTotalPrice();
}

/*function for set to zero quantity of current Dish */
function deleteRow(id) {
    var index = '#' + id;
    datatableApi.row(index).remove().draw();
    showTotalPrice();
}

/*function for finally load data to server
* and redirect to exact page from which
 * this page (orders_dishes.jsp) was called*/
function complete() {
    $.ajax({
        type: "POST",
        url: ajaxOrdersUrl,
        data: getRequestParamFull(datatableApi.rows().data() ),
        success: function () {
            location.href = localStorage.getItem("ordersDishesPostRedirectUrl");
        }
    });
}

/*refresh datatableApi according dishes selected in dishDTApi*/
function refreshDishes() {
    removeDeselectedDishes();
    addSelectedDishes();
    datatableApi.draw();
    showTotalPrice();
    $('#selectDishes').modal('hide');
}

/*remove dishes from datatableApi which were deselected in dishDTApi
* row.selected equals that choose == true*/
function removeDeselectedDishes() {
    var selectedDishes =  dishDTApi.rows( '.selected').data();
    var oldDishes =  datatableApi.rows().data();
    var dishesForDelete = [];
    outer: for (var i = 0; i < oldDishes.length; i++) {
        for (var j = 0; j < selectedDishes.length; j++) {
            if (selectedDishes[j].id == oldDishes[i].id) continue outer;
        }
        dishesForDelete.push('#' + oldDishes[i].id);
    }

    for (var k = 0; k < dishesForDelete.length; k++){
        datatableApi.row(dishesForDelete[k]).remove();
    }
}

/*add in datatableApi dishes was selected in dishDTApi
 * in case that they not present in  datatableApi*/
function addSelectedDishes() {
    var selectedDishes =  dishDTApi.rows( '.selected').data();
    var oldDishes =  datatableApi.rows().data();
    outer: for (var i = 0; i < selectedDishes.length; i++) {
        for (var j = 0; j < oldDishes.length; j++) {
            if (selectedDishes[i].id == oldDishes[j].id) continue outer;
        }
        datatableApi.row.add({
            "id": selectedDishes[i].id,
            "description" : selectedDishes[i].description,
            "price" :selectedDishes[i].price,
            "quantity" : 1
        });
    }
}

/*function to get arrays of dishes and according dishes quantities
* ignore dishes with null quantities*/
function getRequestParamFull(arr) {
    var dishIds=[];
    var dishQuantityValues=[];
    var dishNullQuantityIndexes=[];
    for (var i = 0; i < arr.length; i++){
        if (arr[i].quantity == 0){
            dishNullQuantityIndexes.push(i)
        }else {
            dishQuantityValues.push(arr[i].quantity)
        }
    }
    for (var i = 0; i < arr.length; i++){
        if (($.inArray(i,dishNullQuantityIndexes))==-1){
            dishIds.push(arr[i].id)
        }
    }
    return "dishIds=" + dishIds+"&dishQuantityValues="+ dishQuantityValues+"&totalPrice="+accountTotalPrice();
}

/*function to get arrays of dishes and according dishes quantities
 * ignore dishes with null quantities*/
function getRequestParamIdsOnly(arr) {
    var dishIds=[];
    for (var i = 0; i < arr.length; i++){
         dishIds.push(arr[i].id)
    }
    return "dishIds=" + dishIds;
}

/*show accounted total price in page*/
function showTotalPrice() {
    var totalPrice = accountTotalPrice();
    $('#totalPrice').html(totalPrice);
    $('#totalPriceHeader').html(totalPrice);
}

/*account actual total price */
function accountTotalPrice() {
    var totalPrice=0;
    var data = datatableApi.rows().data();
    data.each(function (value, index) {
        totalPrice = totalPrice + value.price*value.quantity;
    });
    return totalPrice.toFixed(2);
}