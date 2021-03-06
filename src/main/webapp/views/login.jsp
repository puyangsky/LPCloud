<%@ page language="java" import="java.util.*"  %>
<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <!--<link rel="icon" href="../../favicon.ico">-->

    <title>Login For LPCloud</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://v3.bootcss.com/examples/signin/signin.css" rel="stylesheet">

    <script src="http://v3.bootcss.com/assets/js/ie-emulation-modes-warning.js"></script>
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        #loginSuccess {
            display: none;
            text-align: center;
            margin: 10px;
            font-size: large;
        }
    </style>

</head>

<body>

<div class="container">

    <form class="form-signin">
        <h2 class="form-signin-heading">云管理员权限分配平台</h2>
        <label for="inputEmail" class="sr-only">用户名</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="用户名" required autofocus>
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="密码" required>
        <div class="checkbox">
            <label>
                <input type="checkbox" value="remember-me"> 记住密码
            </label>
        </div>
        <button id="submit" class="btn btn-lg btn-primary btn-block">登录</button>
        <div id="loginSuccess"></div>
    </form>

</div>
<script>
    $("#submit").click(function () {
        var email = $("#inputEmail").val();
        var pwd = $("#inputPassword").val();
        var user = {"email": email, "password": pwd};
        $.ajax({
            url:"api/login",
            type:"POST",
            contentType: 'application/json',
            async:false,
            timeout:1000,
            data: JSON.stringify(user),
            success:function(result){
                if (result == "success") {
//                    $("#loginSuccess").html("登录成功").show().delay(2000).hide(0);
//                    setTimeout(function () {
//                        window.location.href = "/index";
//                    }, 2000);
                    alert("登录成功");
                    window.location.href = "/index";

                }else {
//                    $("#loginSuccess").html("登录失败").show().delay(2000).hide(0);
//                    setTimeout(function () {
//                        window.location.href = "/login";
//                    }, 2000);
//                    alert("用户名或密码错误，登录失败");
                }
            }
        });
    });
</script>
</body>
</html>