//数据表格
layui.use(['table', 'form'], function(){
    var table = layui.table;
    var form = layui.form;
    var isEdit = false;
    var editData;

    var url = 'getInterfaceInfo';
    table.render({
        elem: '#baseInfo'
        , url: url
        , page: {
                groups: 3 //只显示 1 个连续页码
                , first: '首页'
                , last: '尾页'
                , limit: 12
                , layout: ['prev', 'page', 'next']
            }
        , id: 'baseInfoReload'
        , height: 580
        , cols: [[
            {checkbox: true, aline: 'center', LAY_CHECKED: false, filter: 'test', width: '5%'}
            , {field: 'interfaceId', align: 'center', width: '7%', title: '接口编码'}
            , {field: 'interfaceName', align: 'center', width: '12%', title: '接口名称'}
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
            , {field: 'interfaceLocalPath', align: 'center', width: '10%', title: '17主机路径'}
            , {field: 'interfaceVgopPath', align: 'center', width: '6%', title: '228主机路径'}
            , {field: 'interfaceRemotePath', align: 'center', width: '10%', title: '集团路径'}
            , {field: 'fileName', align: 'center', width: '6%', title: '文件名称'}
            , {fixed: 'right', width: '10%', align: 'center', title: '操作', toolbar: '#barBaseInfo'}
        ]]
    });
    //数据重载（模糊搜索）
    var $ = layui.$, active = {
        reload: function(){
            var demoReload = $('#demoReload').val();
            if(demoReload != null){
                demoReload = '%' + demoReload + '%';
            }

            //执行重载
            table.reload('baseInfoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    fuzzyQueryInfo: demoReload,
                    interfaceType: $("#interfaceType").val(),
                    interfaceDealType: $("#interfaceDealType").val(),
                    interfaceCycle: $("#interfaceCycle").val()
                }
            });
        },
        getCheckData:function () {
            var checkStatus=table.checkStatus('baseInfoReload')
                ,data=checkStatus.data;
            var ids='';
            if (data==""){
                layer.msg("没有选中数据",{icon:2});
                return;
            }
            if (data.length>0){
                for (var i=0;i<data.length;i++){
                    ids+= '"' + data[i].interfaceId+'",';
                }
                layer.confirm('确定删除？', function(index) {
                    ids = ids.substring(0,ids.length-1);
                    ids = '(' + ids + ')';
                    $.ajax({
                        url: "deleteInterfaceInfoById",
                        data: {
                            interfaceId: ids
                        },
                        success: function (data) {
                            if(data.code == 0){
                                layer.closeAll();
                                layer.msg("删除成功",{icon:1});
                                active.reload();
                            }else{
                                layer.closeAll();
                                layer.msg("删除失败",{icon:5});
                                active.reload();
                            }
                        }
                    })
                })
            }
        },
        add: function () {
            var title = '添加接口';
            var url = 'insertInterfaceInfo';
            if(isEdit){
                title = '编辑接口';
                url = 'updateInterfaceInfoById';
                console.log(editData);
                $("#interfaceId1").val(editData.interfaceId);
                $("#interfaceId1").attr("readonly",true);
                $("#interfaceName1").val(editData.interfaceName);
                $("#interfaceDesc1").val(editData.interfaceDesc);
                $('select[id="interfaceType1"]').siblings("div.layui-form-select").find('dl dd[lay-value="'+editData.interfaceType+'"]').click();
                $('select[id="interfaceDealType1"]').siblings("div.layui-form-select").find('dl dd[lay-value="'+editData.interfaceDealType+'"]').click();
                $("#interfaceLocalPath1").val(editData.interfaceLocalPath);
                $("#interfaceRemotePath1").val(editData.interfaceRemotePath);
                $("#interfaceVgopPath1").val(editData.interfaceVgopPath);
                $("#interfaceFileName1").val(editData.fileName);
            }else{
                $("#interfaceId1").val('');
                $("#interfaceId1").attr("readonly",false);
                $("#interfaceName1").val('');
                $("#interfaceDesc1").val('');
                $('select[id="interfaceType1"]').siblings("div.layui-form-select").find('dl dd[lay-value="1"]').click();
                $('select[id="interfaceDealType1"]').siblings("div.layui-form-select").find('dl dd[lay-value="1"]').click();
                $('select[id="interfaceCycle1"]').siblings("div.layui-form-select").find('dl dd[lay-value="1"]').click();
                $("#interfaceVgopPath1").val('');
                $("#interfaceLocalPath1").val('');
                $("#interfaceRemotePath1").val('');
                $("#interfaceFileName1").val('');
            }
            layui.layer.open({
                type: 1
                , title: title
                , area: ['493px', '650px']
                , offset: 'auto'
                , content: $('#insertBaseInfo')
                , success: function () {//打开成功
                    layui.use(['form'], function() {
                        form = layui.form;
                        form.on('select(interface-cycle)', function(data){
                            layui.form.render();
                        });
                        form.on('submit(Save)', function () {
                            var interfaceId1 = $("#interfaceId1").val();
                            var interfaceName1 = $("#interfaceName1").val();
                            var interfaceDesc1 = $("#interfaceDesc1").val();
                            var interfaceType1 = $("#interfaceType1").val();
                            var interfaceDealType1 = $("#interfaceDealType1").val();
                            var interfaceCycle1 = $("#interfaceCycle1").val();
                            var interfaceLocalPath1 = $("#interfaceLocalPath1").val();
                            var interfaceRemotePath1 = $("#interfaceRemotePath1").val();
                            var interfaceFileName1 = $("#interfaceFileName1").val();
                            var interfaceVgopPath1= $("#interfaceVgopPath1").val();
                            if(interfaceId1 == '' || !isNaN(interfaceId1)){}else{
                                layer.msg("接口编号为数字",{icon:5});
                                $("#interfaceId1").focus();
                            }
                            if(interfaceName1 == '' || interfaceName1 == null){
                                layer.msg("请输入接口名称",{icon:5});
                                $("#interfaceName1").focus();
                            }
                            if(interfaceDesc1 == '' || interfaceDesc1 == null){
                                layer.msg("请输入接口描述",{icon:5});
                                $("#interfaceDesc1").focus();
                            }
                            if(interfaceType1 == '' || interfaceType1 == null){
                                layer.msg("请选择接口类型",{icon:5});
                                $("#interfaceType1").focus();
                            }
                            if(interfaceDealType1 == '' || interfaceDealType1 == null){
                                layer.msg("请选择文件处理类型",{icon:5});
                                $("#interfaceDealType1").focus();
                            }
                            if(interfaceCycle1 == '' || interfaceCycle1 == null){
                                layer.msg("请选择接口运行周期",{icon:5});
                                $("#interfaceCycle1").focus();
                            }
                            if(interfaceLocalPath1 == '' || interfaceLocalPath1 == null){
                                layer.msg("请输入存放路径",{icon:5});
                                $("#interfaceLocalPath1").focus();
                            }
                            if(interfaceRemotePath1 == '' || interfaceRemotePath1 == null){
                                layer.msg("请输入本地路径",{icon:5});
                                $("#interfaceRemotePath1").focus();
                            }
                            if(interfaceFileName1 == '' || interfaceFileName1 == null){
                                layer.msg("请输入接口文件名称",{icon:5});
                                $("#interfaceFileName1").focus();
                            }
                            if(interfaceVgopPath1 == '' || interfaceVgopPath1 == null){
                                layer.msg("请输入228主机路径",{icon:5});
                                $("#interfaceVgopPath1").focus();
                            }

                            $.ajax({
                                url: url,
                                type: "POST",
                                data: JSON.stringify({
                                    interfaceId:interfaceId1,
                                    interfaceName:interfaceName1,
                                    interfaceDesc:interfaceDesc1,
                                    interfaceType:interfaceType1,
                                    interfaceDealType:interfaceDealType1,
                                    interfaceCycle:interfaceCycle1,
                                    interfaceLocalPath:interfaceLocalPath1,
                                    interfaceRemotePath:interfaceRemotePath1,
                                    fileName:interfaceFileName1,
                                    interfaceVgopPath:interfaceVgopPath1
                                }),
                                dataType: "json",
                                contentType: "application/json",
                                success: function (data) {
                                    if(data.code == 0){
                                        layer.closeAll();
                                        layer.msg("成功",{icon:1});
                                        active.reload();
                                        isEdit = false;
                                    }else{
                                        layer.closeAll();
                                        layer.msg(data.msg,{icon:5});
                                        active.reload();
                                        isEdit = false;
                                    }
                                }
                            });
                        });
                    });
                    if(isEdit) {
                        $('select[id="interfaceCycle1"]').siblings("div.layui-form-select").find('dl dd[lay-value="' + editData.interfaceCycle + '"]').click();
                    }
                    isEdit = false;
                }
            })
        }
    };
    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })

    table.on('tool(baseInfo)', function(obj){ //注：tool是工具条事件名，tagInfo是table原始容器的属性 lay-filter="对应的值"
        var dataCur = obj.data;
        var layEvent = obj.event; //获得当前行数据
        if(layEvent === 'del'){ //删除
            layer.confirm('是否删除！', function(index){
                //向服务端发送删除指令
                var ids = dataCur.interfaceId;
                ids = '("' + ids + '")';
                $.ajax({
                    url: "deleteInterfaceInfoById",
                    data: {
                        interfaceId: ids
                    },
                    success: function (data) {
                        if(data.code == 0){
                            layer.closeAll();
                            layer.msg("删除成功",{icon:1});
                            active.reload();
                        }else{
                            layer.closeAll();
                            layer.msg("删除失败",{icon:5});
                            active.reload();
                        }
                    }
                })
            });
        } else if(layEvent === 'edit'){ //编辑
            //do something
            isEdit = true;
            editData = dataCur;
            active['add'].call(this);
        }
    });
});