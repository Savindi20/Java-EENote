<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.CustomerDTO" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.ItemDTO" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Item Manage</title>
    <meta content="width=device-width initial-scale=1" name="viewport">
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/styles.css" rel="stylesheet">
    <link crossorigin="anonymous" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"
          integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" rel="stylesheet">
    <style>
        ul > li {
            cursor: pointer;
        }
    </style>
</head>
<body>
<%
    List<ItemDTO> allItems = (List<ItemDTO>) request.getAttribute("items");
%>
<!--header-->
<header class="jumbotron bg-primary text-white p-3">
    <h1 class="position-absolute" id="nav"></h1>
    <ul class="list-group list-group-horizontal text-danger justify-content-end font-weight-bold">
        <li class="list-group-item bg-white" id="lnkHome"><a href="index.html">Home</a></li>
        <li class="list-group-item bg-white" id="lnkCustomer"><a href="customer.jsp">Customer</a></li>
        <li class="list-group-item bg-danger text-white" id="lnkItem"><a class="text-white" href="item.jsp">Item</a></li>
        <li class="list-group-item bg-white" id="lnkOrders"><a href="purchase-order.html">Orders</a></li>
    </ul>
</header>

<!--item content-->
<main class="container-fluid" id="itemContent">
    <section class="row">
        <div class="col-12 col-lg-4">
            <h1>Item Form</h1>
            <form id="ItemForm">
                <div class="form-group">
                    <label for="itemCode">Item Code</label>
                    <input class="form-control" id="itemCode" type="text" name="code">
                    <span class="control-error" id="lblCode"></span>
                </div>
                <div class="form-group">
                    <label for="itemName">Item Name</label>
                    <input class="form-control" id="itemName" type="text" name="description">
                    <span class="control-error" id="lblname"></span>
                </div>
                <div class="form-group">
                    <label for="itemQty">Item Qty</label>
                    <input class="form-control" id="itemQty" type="text" name="qtyOnHand">
                    <span class="control-error" id="lblqty"></span>
                </div>
                <div class="form-group">
                    <label for="itemPrice">Unit Price</label>
                    <input class="form-control" id="itemPrice" type="text" name="unitPrice">
                    <span class="control-error" id="lblprice"></span>
                </div>
            </form>
            <div class="btn-group">
                <button class="btn btn-primary" type="button" id="btnItem">Save Item</button>
                <button class="btn btn-danger" type="button" id="btnDelete">Remove</button>
                <button class="btn btn-warning" type="button" id="btnUpdate">Update</button>
                <button class="btn btn-success" type="button" id="btnGetAll">Get All</button>
                <button class="btn btn-danger" id="btn-clear1">Clear All</button>
            </div>

        </div>
        <div class="col-12 col-lg-8 mt-3">
            <table class="table table-bordered table-hover">
                <thead class="bg-danger text-white">
                <tr>
                    <th>Item Code</th>
                    <th>Item Name</th>
                    <th>Item Qty</th>
                    <th>Item Price</th>
                </tr>
                </thead>
                <tbody id="tblItem">
                <%
                    if (allItems!=null){
                        for (ItemDTO items : allItems) {
                %>
                <tr>
                    <td><%=items.getCode()%></td>
                    <td><%=items.getDescription()%></td>
                    <td><%=items.getQtyOnHand()%></td>
                    <td><%=items.getUnitPrice()%></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </section>

</main>

<script src="assets/js/jquery-3.6.1.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>

<script>
    //load all items from database
    getAllItems();

    //Button events
    // add item
    $("#btnItem").click(function(){
        let formData = $("#ItemForm").serialize();
        $.ajax({
            url:"Item?option=add",
            method:"post",
            data:formData,
            success:function(res){
                getAllItems();
            }
        });
    });

    //delete customer
    $("#btnDelete").click(function(){
        let id = $("#itemCode").val();
        $.ajax({
            url:"Item?code="+id+"&option=remove",
            method:"post",
            success:function(res){
                getAllItems();
            }
        });
    });

    //update customer
    $("#btnUpdate").click(function(){
        let formData = $("#ItemForm").serialize();
        $.ajax({
            url:"Item?option=update",
            method:"post",
            data:formData,
            success:function(res){
                getAllItems();
            }
        });
    });

    //getAll customer
    $("#btnGetAll").click(function(){
        getAllItems();
    });

    //getAll customer function
    function getAllItems(){
        $("#tblItem").empty();
        $.ajax({
            url:"Item",
            success:function(res){
                for (let c of res) {
                    let iCode=c.code;
                    let iDescription=c.description;
                    let iQtyOnHand=c.qtyOnHand;
                    let iUnitPrice=c.unitPrice;

                    let row ="<tr><td>"+iCode+"</td><td>"+iDescription+"</td><td>"+iQtyOnHand+"</td><td>"+iUnitPrice+"</td></tr>";
                    $("#tblItem").append(row);
                }
                bindRowClickEvents();
                setTextFieldValues("","","","");
            }
        });
    }

    //bind events for the table rows function
    function bindRowClickEvents(){
        $("#tblItem>tr").click(function (){
            let code = $(this).children(":eq(0)").text();
            let description = $(this).children(":eq(1)").text();
            let qtyOnHand = $(this).children(":eq(2)").text();
            let unitPrice = $(this).children(":eq(3)").text();

            $('#itemCode').val(code);
            $('#itemName').val(description);
            $('#itemQty').val(qtyOnHand);
            $('#itemPrice').val(unitPrice);
        });
    }

    //set text fields values function
    function setTextFieldValues(code,description,qtyOnHand,unitPrice){
        $('#itemCode').val(code);
        $('#itemName').val(description);
        $('#itemQty').val(qtyOnHand);
        $('#itemPrice').val(unitPrice);
    }
</script>

</body>
</html>
