$(window).keyup(function(event){
    if(event.keyCode == 13) {
        $("#js-btn-login").click();
        return true;
    }
});
$(document).ready(function () {
    if (window != top) {
        top.location.href = location.href;
    }
});

$("#js-btn-login").click(function () {
    $.ajax({
        url: "userLogin",
        type: "POST",
        data: {
            "userName": $.base64.encode($("#username").val(), "utf-8"),
            "password": $.base64.encode($("#password").val(), "utf-8")
        },
        success: function (result) {
            if(result.code == "0" ){
                window.location.replace("/ooimonitor");
            }else{
                if($('div').hasClass('yh1')){
                    $(".yh1").remove();
                }
                if($('div').hasClass('yh2')){
                    $(".yh2").remove();
                }
                $("#password").after("<div style='color: red' class='yh2'>用户名或密码错误！！</div>")
            }
        }

    });
})