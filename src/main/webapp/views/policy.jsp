<%@ page language="java" import="java.util.*"  %>
<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-table.min.css">
    <style type="text/css">
        .title {
            height: 80px;
            margin-top: 60px;
            text-align: center;
        }
        .index{
            float: right;
            margin: 10px;
        }
    </style>

</head>
<body>
<div class="container">
    <div class="title">
        <h1>${adminName}最小特权策略展示</h1>
    </div>
    <div class="index">
        <button id="index" class="btn btn-primary btn-sm">
            返回首页
        </button>
    </div>
    <table id="table"
           data-show-columns="true"
           data-search="true"
           data-show-export="true"
           data-url="data/agentAdmin.json"
           data-show-refresh="true"
           data-show-toggle="true"
           data-pagination="true"
           data-height="500">  </table>

</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-table-editable.js"></script>
<script src="js/bootstrap-editable.js"></script>
<script src="js/bootstrap-table.js"></script>
<script src="js/bootstrap-table-zh-CN.js"></script>
<script src="js/tableExport.js"></script>
<script src="js/bootstrap-table-export.js"></script>
<script>
    function tdclick(tdobject){
        var td=$(tdobject);
        td.attr("onclick", "");
        //1,取出当前td中的文本内容保存起来
        var text=td.text();
        //2,清空td里面的内容
        td.html(""); //也可以用td.empty();
        //3，建立一个文本框，也就是input的元素节点
        var input=$("<input>");
        //4，设置文本框的值是保存起来的文本内容
        input.attr("value",text);
        input.bind("blur",function(){
            var inputnode=$(this);
            var inputtext=inputnode.val();
            var tdNode=inputnode.parent();
            tdNode.html(inputtext);
            tdNode.click(tdclick);
            td.attr("onclick", "tdclick(this)");
        });
        input.keyup(function(event){
            var myEvent =event||window.event;
            var kcode=myEvent.keyCode;
            if(kcode==13){
                var inputnode=$(this);
                var inputtext=inputnode.val();
                var tdNode=inputnode.parent();
                tdNode.html(inputtext);
                tdNode.click(tdclick);
            }
        });

        //5，将文本框加入到td中
        td.append(input);
        var t =input.val();
        input.val("").focus().val(t);
//              input.focus();

        //6,清除点击事件
        td.unbind("click");
    }

</script>
<script>
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
            url: "api/getPolicy?service=${adminName}",
            columns: [
                {
                    checkbox: true
            },
                {
                    field: "subject",
                    title: "管理员角色",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"Subject\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"用户名\">" + value + "</a>";
                    }
                },
                {
                    field: "action",
                    title: "操作",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"Action\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"用户名\">" + value + "</a>";
                    }
                },
                {
                    field: "object",
                    title: "API",
                    formatter: function (value, row, index)
                    {
                        return "<a1 href=\"#\" name=\"Object\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"用户名\">" + value + "</a>";
                    }
                }],
            onClickRow: function (row, $element) {
                curRow = row;
            },
            onLoadSuccess: function (aa, bb, cc) {
                $("#table a1").editable({
                    url: function (params) {
                        var sName = $(this).attr("name");
                        curRow[sName] = params.value;
                        $.ajax({
                            type: 'POST',
                            url: "data/agentAdmin.json",
                            data: curRow,
                            dataType: 'JSON',
                            success: function (data, textStatus, jqXHR) {

                            },
                            error: function () { }
                        });
                    },
                    type: 'text'
                });
            },
        });

        $("#index").click(function () {
            window.location.href="/lpcloud";
        });
    });
</script>
</body>
</html>
