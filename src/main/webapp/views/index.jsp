<%@ page language="java" import="java.util.*"  %>
<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="cn">
<head>
    <!--<meta charset="utf-8">-->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <link href="css/bootstrap-editable.css" rel="stylesheet" />
    <link href="css/bootstrap-table.min.css" rel="stylesheet" />

    <style type="text/css">
        .title {
            height: 80px;
            margin-top: 60px;
            text-align: center;
        }
        .config{
            float: right;
            margin: 10px;
        }
        .form-body{
            text-align: center;
        }
        .form-row{
            margin: 10px;
        }
        .mylabel {
            text-align: right;
            font-size: medium;
            padding-top: 5px;
        }
        #result {
            display: none;
            font-size: medium;
            color: red;
            text-align: center;
        }

        .logout{
            float: right;
            margin: 10px;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle!important;
        }
    </style>

</head>
<body>
<div class="container">
    <div class="title">
        <h1>OpenStack最小特权管理员列表展示</h1>
    </div>

    <div class="logout">
        <button id="logout" class="btn btn-primary btn-sm">
            退出登录
        </button>
    </div>

    <div id="result">更新成功！</div>

    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary btn-sm config" data-toggle="modal" data-target="#myModal">
        更改配置
    </button>
    <button id="build" type="button" class="btn btn-primary btn-sm config" data-toggle="modal" data-target="#roleModal">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加
    </button>
    <table id="table"
           data-show-columns="true"
           data-search="true"
           data-show-export="true"
           data-url="api/getRole"
           data-show-refresh="true"
           data-show-toggle="true"
           data-pagination="true"
           data-height="500">  </table>


    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        更改LPCloud配置
                    </h4>
                </div>
                <div class="modal-body">

                    <form class="form form-body">
                        <div class="row form-row">
                            <div class="col-xs-4 mylabel">
                                    期望管理员数量：
                            </div>
                            <div class="col-xs-6">
                                <input type="text" id="adminCount" class="form-control" placeholder="正整数，默认为1" required>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <button id="update" type="button" class="btn btn-primary">提交更改</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <div class="modal fade" id="roleModal" tabindex="-1" role="dialog" aria-labelledby="roleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="roleModalLabel">
                        增加规则
                    </h4>
                </div>
                <div class="modal-body">

                    <form class="form form-body">
                        <div class="row form-row">
                            <div class="col-xs-4 mylabel">
                                管理员角色：
                            </div>
                            <div class="col-xs-6">
                                <input type="text" id="role" class="form-control" placeholder="角色" required>
                            </div>
                        </div>
                        <div class="row form-row">
                            <div class="col-xs-4 mylabel">
                                管理员名称：
                            </div>
                            <div class="col-xs-6">
                                <input type="text" id="username" class="form-control" placeholder="名称" required>
                            </div>
                        </div>
                        <div class="row form-row">
                            <div class="col-xs-4 mylabel">
                                管理员职能：
                            </div>
                            <div class="col-xs-6">
                                <input type="text" id="duty" class="form-control" placeholder="职能" required>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <button id="addRole" type="button" class="btn btn-primary">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


</div>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-table-editable.js"></script>
<script src="js/bootstrap-editable.js"></script>
<script src="js/bootstrap-table.js"></script>
<script src="js/bootstrap-table-zh-CN.js"></script>
<script src="js/tableExport.js"></script>
<script src="js/bootstrap-table-export.js"></script>
<script>
    $("#update").click(function () {
        var count = $("#adminCount").val();
        var data = {"count": count};
        $.ajax({
            url:"api/update",
            type:"POST",
            contentType: 'application/json',
            data: JSON.stringify(data),
            success:function(result) {
                $("#myModal").modal('hide');
                alert("更新成功");
                location.reload();
            }
        });
    });

    $("#addRole").click(function () {
        var role = $("#role").val();
        var username = $("#username").val();
        var duty = $("#duty").val();
        var data = {
            "role": role,
            "username":username,
            "duty":duty
        };
        $.ajax({
            url:"api/addRole",
            type:"POST",
            contentType: 'application/json',
            data: JSON.stringify(data),
            success:function(result){
                $("#roleModal").modal('hide');
                alert("添加成功");
                location.reload();
            }
        });
    });

    $("#logout").click(function () {
        $.ajax({
            url:"api/logout",
            type:"POST",
            success:function(result){
                if (result == "success") {
                    window.location.href="/login";
                }
                location.reload();
            }
        });
    });


</script>
<script type="text/javascript">

    var curRow = {};
    $(function () {
        $("#table").bootstrapTable({
            toolbar: "#toolbar",
            idField: "Id",
            pagination: true,
            showRefresh: true,
            search: true,
            clickToSelect: true,
            queryParams: function (param) {
                return {};
            },
            url: "api/getRole",
            columns: [
//                {
//                    checkbox: false
//                },
                {
                    field: "role",
                    title: "管理员角色",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"role\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"管理员角色\">" + value + "</a1>";
                    }
                },
                {
                    field: "username",
                    title: "管理员名称",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"username\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"管理员名称\">" + value + "</a1>";
                    }
                },
                {
                    field: "duty",
                    title: "管理员主要职能",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"duty\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"管理员主要职能\">" + value + "</a1>";
                    }
                },
                {
                    field: "url",
                    title: "职能详情"
                }],
            onClickRow: function (row, $element) {
                curRow = row;
            },
            onLoadSuccess: function (aa, bb, cc) {}
        });
    });
</script>
</body>
</html>
