var param;

$(function(){
    param = $.Request("text");
    if(param == undefined || param == null ){
        param = "";
    }
})
//重载
function reload() {
    //重载
    layui.use('table', function(){
        var table = layui.table;
        table.reload('interfaceInfoReload', {
            url: "interfaceInfo",
            where: {
                str: $('#demoReload').val()
            }
        });
    });
}


//数据表格
layui.use('table', function(){
    var table = layui.table;

    var url = 'interfaceRecord';
    if(param != "" && param != null) {
        url = 'interfaceRecord?str=' + param;
    }
    table.render({
        elem: '#interfaceRecord'
        , url: url
        , page: true
        , id: 'interfaceRecord'
        , height: 475
        , cols: [[
            /* {checkbox: true, aline: 'center', LAY_CHECKED: false, filter: 'test', width: '5%'}
             ,*/ {field: 'interfaceId', align: 'center', width: '10%', title: '接口编号'}
            , {field: 'state', align: 'center', width: '20%', title: '状态',
                    templet:function (result) {
                        var html='';
                        if(result.state == 1){
                            html= '<span>文件生成成功</span>';
                        }else if(result.state == 2){
                            html= '<span>文件上传成功</span>';
                        }else if(result.state == 3){
                            html= '<span>文件校验失败</span>';
                        }else if(result.state == 0){
                            html= '<span>接口运行成功</span>';
                        }else if(result.state == -1){
                            html= '<span>'+result.reason+'</span>';
                        }
                        return html;
                    }}
            , {field: 'updateTime', align: 'center', width: '10%', title: '更新时间'}
            , {field: 'reason', align: 'center', width: '40%', title: '信息'}
            , {field: 'fileCount', align: 'center', width: '10%', title: '文件记录条数'}
            , {field: 'successCount', align: 'center', width: '10%', title: '成功条数'}
            /*, {
                field: 'success', align: 'center', width: '9%', title: '是否成功',
                templet: function (result) {
                    if (result.code == 0) {
                        var html = '<img src="./static/images/success.png" style="width: 25%"/>'

                        return html;
                    } else {
                        var html = '<img src="./static/images/fail.png" style="width: 25%"/>';
                        return html;
                    }
                }
            }*/
        ]]
    });
    //数据重载（模糊搜索）
    var $ = layui.$, active = {
        reload: function(){
            var demoReload = $('#demoReload');

            //执行重载
            table.reload('interfaceInfoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    str: demoReload.val()
                }
            });
        },
    };
    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })

});

$(function () {
    $(".checkLabelId").keyup(function() {
        var c = $(this);
        if (/[^\d]/.test(c.val())) {
            var temp = c.val().replace(/[^\d]/g, '');
            $(this).val(temp);
        }
    });
});


function sftpRun(){
    var time = $("#sftpTime").val();
    $.ajax({
        url: "sftp",
        type: "GET",
        data: {"time":time},
        success:function (result) {
            if (result.code==0){

            }
        }
    });
}

function commit() {
    var interfaceId = $('#interfaceId').val();
    var updateType = $('#updateType').val();
    var isSuccess = $('#isSuccess').val();
    var updateTime = $('#updateTime').val();

    //重载
    layui.use('table', function(){
        var table = layui.table;
        table.reload('interfaceRecord', {
            url: "/interfaceRecord/find"
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
            ,where: {
                interfaceId: interfaceId,
                updateType: updateType,
                isSuccess: isSuccess,
                updateTime: updateTime
            }
        });
    });
}

layui.use('laydate', function(){
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#sftpTime' //指定元素
    });
});

layui.use('laydate', function(){
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#updateTime' //指定元素
    });
});