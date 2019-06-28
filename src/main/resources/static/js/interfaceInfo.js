var addModak;//获取模态的dom，以便关闭
var editModak;
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


//打开添加的模态框
function openAddModak(){
    layui.use(['layer'],function () {
        var layer = layui.layer,$=layui.$;
        addModak = layer.open({
            type:1,//类型
            area:['450px','400px'],//定义宽和高
            title:'添加标签信息',//题目
            shadeClose:false,//点击遮罩层关闭
            content: $('#addInfo'),//打开的内容
            cancel: function () {
                $('.clearAdd').val("");
            }
        });
    });
}

//添加
layui.use('form', function(){
    var form = layui.form;
    form.on('submit(formAdd)', function(data){
        var interfaceId = $('#interfaceId').val();
        $.ajax({
            url: "checkInterface?interfaceId=" + interfaceId,
            type: "GET",
            success: function (result) {
                if (result.code == 0) {
                    data = data.field;
                    $.ajax({
                        url: "saveInterfaceInfo",
                        contentType: "application/json;charset=UTF-8",
                        type: "POST",
                        data: JSON.stringify(data),
                        dataType:'json',
                        success:function (result) {
                            if(result.code == 0){
                                $('.alert').html('添加成功').addClass('alert-success').show().delay(2500).fadeOut();
                                setTimeout(function(){
                                    $('.alert').removeClass("alert-success");
                                },2500)
                                $('.clearAdd').val("");
                                layer.close(addModak);
                                reload();
                                }else {
                                $('.alert').html('添加失败').addClass('alert-danger').show().delay(2500).fadeOut();
                                setTimeout(function(){
                                    $('.alert').removeClass("alert-danger");
                                },2500)
                                $('.clearAdd').val("");
                                layer.close(addModak);
                            }
                            }
                    });
                }else {
                    $('#id-add-msg').show();
                    $('#interfaceId').val("接口号已存在！！！");
                }
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});

//导入
layui.use('upload', function(){
    var $ = layui.jquery
        ,upload = layui.upload;
    upload.render({
        elem: '#load'
        ,url: 'interfaceInfo/upload'
        ,accept: 'file'
        ,done: function(res){
            if(res==true){
                $('.alert').html('导入成功').addClass('alert-success').show().delay(2500).fadeOut();
                setTimeout(function(){
                    $('.alert').removeClass("alert-success");
                },2500)
                reload();
            }
            else {
                $('.alert').html('导入失败').addClass('alert-danger').show().delay(2500).fadeOut();
                setTimeout(function(){
                    $('.alert').removeClass("alert-danger");
                },2500)
            }
        }
    });
});

//数据重跑
layui.use('form', function(){
    var form = layui.form;
    form.on('submit(run)', function(data){
        var data  = data.field;
        $.ajax({
            url: "run",
            type: "GET",
            data:data,
            success:function (result) {
                if(result.code==0){
                    alert("success!!!");
                }
            }
        });
    });
});
//数据表格
layui.use('table', function(){
    var table = layui.table;

    var url = 'interfaceInfo';
    if(param != "" && param != null) {
        url = 'interfaceInfo?str=' + param;
    }
    table.render({
        elem: '#interfaceInfo'
        , url: url
        , page: true
        , id: 'interfaceInfoReload'
        , height: 475
        , cols: [[
           /* {checkbox: true, aline: 'center', LAY_CHECKED: false, filter: 'test', width: '5%'}
            ,*/ {field: 'interfaceId', align: 'center', width: '7%', title: '接口编号'}
            , {field: 'desc', align: 'center', width: '25%', title: '接口描述'}
            , {field: 'type', align: 'center', width: '7%', title: '接口类型',
            templet:function (result) {
                var html='';
                if(result.type == 1){
                    html= '<span>上传</span>';
                }else {
                    html= '<span>下载</span>';
                }
                return html;
            }}
            , {field: 'updateType', align: 'center', width: '8%', title: '更新类型',
                templet:function (result) {
                var html = '';
                    if(result.updateType == 1){
                        html='<span>日接口</span>'
                    }else if(result.updateType == 2){
                        html='<span>月接口</span>'
                    }else {
                        html='<span>周接口</span>'
                    }
                    return html;
                }}
            , {field: 'updateTime', align: 'center', width: '10%', title: '更新账期'}
            , {field: 'blocPath', align: 'center', width: '29%', title: '数据文件位置'}
            , {fixed: 'right', width: '13%', align: 'center', title: '操作', toolbar: '#barinterfaceInfo'}
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
        getCheckData:function () {
            layer.confirm('是否删除！', function(index){
                    var checkStatus=table.checkStatus('interfaceInfoReload')
                        ,data=checkStatus.data;
                    var ids='';
                    if (data==""){
                        layer.msg("没有选中数据",{icon:2});
                        return;
                    }
                    if (data.length>0){
                        for (var i=0;i<data.length;i++){
                            ids+=data[i].labelId+","
                        }
                        $.ajax({
                            url: "interfaceInfo/"+ids,
                            success: function (msg) {
                                if(msg === "success"){

                                    layer.close(index);
                                    reload();
                                }
                            }
                        })
                    }
                }
            )}
    };
    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })

    table.on('tool(interfaceInfo)', function(obj){ //注：tool是工具条事件名，interfaceInfo是table原始容器的属性 lay-filter="对应的值"
        var dataCur = obj.data;
        var layEvent = obj.event; //获得当前行数据
        if(layEvent === 'del'){ //删除
            layer.confirm('是否删除！', function(index){
                //向服务端发送删除指令
                $.ajax({
                    url: "deleteInterfaceId/"+dataCur.interfaceId,
                    type: "delete",
                    success: function (result) {
                        if(result.code ==0){
                            obj.del();
                            layer.close(index);
                        }
                    }
                })
            });
        } else if(layEvent === 'edit'){ //编辑
            //do something
            layui.use(['layer'],function () {
                var layer = layui.layer,$=layui.$;
                editModak = layer.open({
                    type:1,//类型
                    area:['450px','400px'],//定义宽和高
                    title:'修改标签信息',//题目
                    shadeClose:false,//点击遮罩层关闭
                    content: $('#editInfo'),//打开的内容
                    cancel: function () {
                        $('.clearAdd').val("");
                    }
                });
            });
            $("#editinterfaceId").val(dataCur.interfaceId);
            $("#editdescribe").val(dataCur.describe);
            var select = 'dd[lay-value='+dataCur.updateType+']';
            $('#editupdateType').siblings("div.layui-form-select").find('dl').find(select).click();
            var select = 'dd[lay-value='+dataCur.type+']';
            $('#edittype').siblings("div.layui-form-select").find('dl').find(select).click();
            $("#editblocPath").val(dataCur.blocPath);

            //修改
            layui.use('form', function(){
                var form = layui.form;
                form.on('submit(formEdit)', function(data){
                    var interfaceId = $('#editinterfaceId').val();
                    $.ajax({
                        url: "checkInterface?interfaceId=" + interfaceId,
                        type: "GET",
                        success: function (result) {
                            if(result.code == -1){
                                $('.code-msg').hide();
                                data  = data.field;
                                $.ajax({
                                    url: "editInterfaceInfo",
                                    contentType: "application/json;charset=UTF-8",
                                    type: "POST",
                                    data: JSON.stringify(data),
                                    dataType:"json",
                                    success:function () {
                                        $('.alert').html('修改成功').addClass('alert-success').show().delay(2500).fadeOut();
                                        setTimeout(function(){
                                            $('.alert').removeClass("alert-success");
                                        },2500);
                                        $('.clearEdit').val("");
                                        layer.close(editModak);
                                        //同步更新缓存对应的值
                                        reload();
                                    }
                                });

                            }else {
                                $('.code-msg').show("接口号不存在！！！");
                            }
                        }
                    });
                    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                });
            });
            /**/
        }else if(layEvent === 'run'){
            //do something
            layui.use(['layer'],function () {
                var layer = layui.layer,$=layui.$;
                editModak = layer.open({
                    type:1,//类型
                    area:['400px','250px'],//定义宽和高
                    title:'数据重跑',//题目
                    shadeClose:false,//点击遮罩层关闭
                    content: $('#run'),//打开的内容
                    cancel: function () {
                        $('.clearAdd').val("");
                    }
                });

            });
        }
    });
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

layui.use('laydate', function(){
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#updateTime' //指定元素
    });
});

layui.use('laydate', function(){
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#updateTime' //指定元素
    });
});