//数据表格
layui.use('table', function(){
    var table = layui.table;

    var url = 'getInterfaceInfo';
    // if(param != "" && param != null) {
    //     url = 'tagInfo?str=' + param;
    // }
    table.render({
        elem: '#baseInfo'
        , url: url
        , page: true
        , id: 'baseInfoReload'
        , height: 475
        , cols: [[
            {checkbox: true, aline: 'center', LAY_CHECKED: false, filter: 'test', width: '5%'}
            , {field: 'interfaceId', align: 'center', width: '10%', title: '接口编码'}
            , {field: 'interfaceName', align: 'center', width: '15%', title: '接口名称'}
            , {field: 'interfaceDesc', align: 'center', width: '16%', title: '接口描述'}
            , {field: 'interfaceType', align: 'center', width: '6%', title: '接口类型',
                templet: function (data) {
                    if (data.interfaceType == "1") {
                        var html = '实时'

                        return html;
                    } else {
                        var html = '文件';
                        return html;
                    }
                }}
            , {field: 'interfaceDealType', align: 'center', width: '6%', title: '处理类型',
                templet: function (data) {
                    if (data.interfaceDealType == "1") {
                        var html = '上传'

                        return html;
                    } else {
                        var html = '下载';
                        return html;
                    }
                }}
            , {field: 'interfaceCycle', align: 'center', width: '6%', title: '运行周期',
                templet: function (data) {
                    var html = '';
                    switch (data.interfaceCycle) {
                        case '1': html = '日';break;
                        case '2': html = '周';break;
                        case '3': html = '月';break;
                        case '4': html = '临时';break;
                    }
                    return html;
                }}
            , {field: 'interfaceLocalPath', align: 'center', width: '10%', title: '存放路径'}
            , {field: 'interfaceRemotePath', align: 'center', width: '10%', title: '本地路径'}
            , {
                field: 'interfaceRunTime', align: 'center', width: '6%', title: '运行时间',
                templet: function (data) {
                    var html = '';
                    switch (data.interfaceCycle) {
                        case '1': html = data.interfaceRunTime + '日';break;
                        case '2': html = '星期' + data.interfaceRunTime;break;
                        case '3': html = data.interfaceRunTime + '日';break;
                        case '4': html = data.interfaceRunTime;break;
                    }
                    return html;
                }
            }
            , {fixed: 'right', width: '10%', align: 'center', title: '操作', toolbar: '#barBaseInfo'}
        ]]
    });
    //数据重载（模糊搜索）
    var $ = layui.$, active = {
        reload: function(){
            var demoReload = $('#demoReload');

            //执行重载
            table.reload('tagInfoReload', {
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
                    var checkStatus=table.checkStatus('tagInfoReload')
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
                            url: "tagInfo/"+ids,
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
    // $('.demoTable .layui-btn').on('click', function(){
    //     var type = $(this).data('type');
    //     active[type] ? active[type].call(this) : '';
    // })

    table.on('tool(baseInfo)', function(obj){ //注：tool是工具条事件名，tagInfo是table原始容器的属性 lay-filter="对应的值"
        var dataCur = obj.data;
        var layEvent = obj.event; //获得当前行数据
        if(layEvent === 'del'){ //删除
            layer.confirm('是否删除！', function(index){
                //向服务端发送删除指令
                $.ajax({
                    url: "tagInfo/"+dataCur.labelId,
                    type: "delete",
                    success: function (msg) {
                        if(msg === "success"){
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
            $("#labelId").val(dataCur.labelId);
            $("#labelName").val(dataCur.labelName);
            $("#labelCol").val(dataCur.labelCol);
            $("#dataSourceCode").val(dataCur.dataSourceCode);
            $("#sourceTabCol").val(dataCur.sourceTabCol);

            //修改
            layui.use('form', function(){
                var form = layui.form;
                form.on('submit(formEdit)', function(data){
                    var dataSourceCode = $('#dataSourceCode').val();
                    $.ajax({
                        url:"tagInfo/checkDataSourceCode?dataSourceCode="+dataSourceCode,
                        type: "GET",
                        success: function (code) {
                            if(code == 0){
                                $('.code-msg').show();
                                $('#dataSourceCode').val("");
                            }else {
                                $('.code-msg').hide();
                                data  = data.field;
                                $.ajax({
                                    url: "tagInfo/"+dataCur.labelId,
                                    type: "PUT",
                                    data: data,
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
                            }
                        }
                    });
                    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                });
            });
            /**/
        }
    });
});