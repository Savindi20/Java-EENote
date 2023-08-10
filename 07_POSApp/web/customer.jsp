<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.CustomerDTO" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Customer Manage</title>
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
//    ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
//    allCustomers.add(new CustomerDTO("C001","Savii","Matara",100000));
//    allCustomers.add(new CustomerDTO("C002","Sewmi","Galle",100000));
//    allCustomers.add(new CustomerDTO("C003","Kamal","Colombo",75000));
//    allCustomers.add(new CustomerDTO("C004","Nimal","Panadura",50000));
//    allCustomers.add(new CustomerDTO("C005","Sunil","Kandy",25000));

//    initialize database connection
//    List<CustomerDTO> allCustomers = new ArrayList<>();
    List<CustomerDTO> allCustomers = (List<CustomerDTO>) request.getAttribute("customers");
%>
<!--header-->
<header class="jumbotron bg-primary text-white p-3">
    <h1 class="position-absolute" id="nav"></h1>
    <ul class="list-group list-group-horizontal text-danger justify-content-end font-weight-bold">
        <li class="list-group-item bg-white" id="lnkHome"><a href="index.html">Home</a></li>
        <li class="list-group-item bg-danger text-white" id="lnkCustomer"><a class="text-white" href="customer.jsp">Customer</a>
        </li>
        <li class="list-group-item bg-white" id="lnkItem"><a href="item.html">Item</a></li>
        <li class="list-group-item bg-white" id="lnkOrders"><a href="purchase-order.html">Orders</a></li>
    </ul>
</header>

<!--customer content-->
<main class="container-fluid" id="customerContent">
    <section class="row">
        <div class="col-12 col-lg-4">
            <h1>Customer Registration</h1>
            <form id="CustomerForm">
                <div class="form-group">
                    <label for="txtCustomerID">Customer ID</label>
                    <input class="form-control" id="txtCustomerID" required type="text" name="id">
                    <span class="control-error" id="lblcusid"></span>
                </div>
                <div class="form-group">
                    <label for="txtCustomerName">Customer Name</label>
                    <input class="form-control" id="txtCustomerName" type="text" name="name">
                    <span class="control-error" id="lblcusname"></span>
                </div>
                <div class="form-group">
                    <label for="txtCustomerAddress">Customer Address</label>
                    <input class="form-control" id="txtCustomerAddress" type="text" name="address">
                    <span class="control-error" id="lblcusaddress"></span>
                </div>
                <div class="form-group">
                    <label for="txtCustomerSalary">Customer Salary</label>
                    <input class="form-control" id="txtCustomerSalary" type="text" name="salary">
                    <span class="control-error" id="lblcussalary"></span>
                </div>
            </form>

            <div class="btn-group">
                <button class="btn btn-primary" type="button" id="btnCustomer">Save Customer</button>
                <button class="btn btn-danger" type="button" id="btnCusDelete">Remove</button>
                <button class="btn btn-warning" type="button" id="btnUpdate">Update</button>
                <button class="btn btn-success" type="button" id="btnGetAll">Get All</button>
                <button class="btn btn-danger" id="btn-clear1">Clear All</button>
            </div>

        </div>
        <div class="col-12 col-lg-8 mt-3">
            <table class="table table-bordered table-hover">
                <thead class="bg-danger text-white">
                <tr>
                    <th>Customer ID</th>
                    <th>Customer Name</th>
                    <th>Customer Address</th>
                    <th>Customer Salary</th>
                </tr>
                </thead>
                <tbody id="tblCustomer">
                <%
                    if (allCustomers!=null){
                        for (CustomerDTO customer : allCustomers) {
                %>
                <tr>
                    <td><%=customer.getId()%></td>
                    <td><%=customer.getName()%></td>
                    <td><%=customer.getAddress()%></td>
                    <td><%=customer.getSalary()%></td>
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
    getAllCustomers();

    function setTextfieldValues(id,name,address,salary){
        $('#txtCustomerID').val(id);
        $('#txtCustomerName').val(name);
        $('#txtCustomerAddress').val(address);
        $('#txtCustomerSalary').val(salary);
    }
    function bindRowClickEvents(){
        $("#tblCustomer>tr").click(function (){
            let id = $(this).children(":eq(0)").text();
            let name = $(this).children(":eq(1)").text();
            let address = $(this).children(":eq(2)").text();
            let salary = $(this).children(":eq(3)").text();

            $('#txtCustomerID').val(id);
            $('#txtCustomerName').val(name);
            $('#txtCustomerAddress').val(address);
            $('#txtCustomerSalary').val(salary);
        });
    }

    $("#btnCustomer").click(function(){
        let formData = $("#CustomerForm").serialize();
        $.ajax({
            url:"Customer?option=add",
            method:"post",
            data:formData,
            success:function(res){
            }
        });
    });

    $("#btnCusDelete").click(function(){
        let id = $("#txtCustomerID").val();
        $.ajax({
            url:"Customer?id="+id+"&option=remove",
            method:"post",
            success:function(res){
            }
        });
    });

    $("#btnUpdate").click(function(){
        let formData = $("#CustomerForm").serialize();
        $.ajax({
            url:"Customer?option=update",
            method:"post",
            data:formData,
            success:function(res){
            }
        });
    });

    function getAllCustomers(){
        $("#tblCustomer").empty();
        $.ajax({
            url:"Customer",
            success:function(res){
                for (let c of res) {
                    let cusID=c.id;
                    let cusName=c.name;
                    let cusAddress=c.address;
                    let cusSalary=c.salary;

                    let row ="<tr><td>"+cusID+"</td><td>"+cusName+"</td><td>"+cusAddress+"</td><td>"+cusSalary+"</td></tr>";
                    $("#tblCustomer").append(row);
                }
                bindRowClickEvents();
            }
        });
    }

    $("#btnGetAll").click(function(){
        getAllCustomers();
    });

</script>
</body>
</html>
