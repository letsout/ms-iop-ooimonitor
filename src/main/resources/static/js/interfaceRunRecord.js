//数据表格
layui.use(['table', 'form'], function(){
    var table = layui.table;
    var form = layui.form;
    var isEdit = false;
    var editData;

    var url = 'getInterfaceRunRecordInfo';
    table.render({
        elem: '#interfaceRunRecord'
        , url: url
        , page: {
            groups: 3 //只显示 1 个连续页码
            , first: '首页'
            , last: '尾页'
            , limit: 12
            , layout: ['prev', 'page', 'next']
        }
        , id: 'interfaceRunRecord'
        , height: 580
        , cols: [[
            {field: 'interfaceId', align: 'center', width: '10%', title: '接口编码'}
            , {field: 'typeDesc', align: 'center', width: '7%', title: '状态描述',
                templet: function (data) {
                    if (data.typeDesc == "0") {
                        var html = '成功'

                        return html;
                    } else if (data.typeDesc == "-1") {
                        var html = '失败';
                        return html;
                    }
                }}
            , {field: 'runStep', align: 'center', width: '8%', title: '运行步骤',
                templet: function (data) {
                    if (data.runStep == "1") {
                        var html = '文件生成/下载'

                        return html;
                    } else if (data.runStep == "2") {
                        var html = '文件上传/入库';
                        return html;
                    } else if (data.runStep == "3") {
                        var html = '文件校验';
                        return html;
                    }
                }}
            , {field: 'fileName', align: 'center', width: '25%', title: '文件名称'}
            , {field: 'fileNum', align: 'center', width: '8%', title: '文件行数'}
            , {field: 'fileSuccessNum', align: 'center', width: '8%', title: '文件操作成功记录数'}
            , {field: 'errorDesc', align: 'center', width: '13%', title: '错误描述'}
            , {field: 'updateTime', align: 'center', width: '13%', title: '记录时间'}
            ,{field: 'fileTime', align: 'center', width: '8%', title: '文件周期'}
        ]]

    });
    //数据重载（模糊搜索）
    var $ = layui.$, active = {
        reload: function(){
            var demoReload = $('#demoReload').val();
            if(demoReload != null){
                demoReload = '%' + demoReload + '%';
            }

            //执行重载, where: {
            table.reload('interfaceRunRecord', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    interfaceId: demoReload,
                    typeDesc: $("#typeDesc").val(),
                    runStep: $("#runStep").val()
                }
            });
        },
        runInterface: function () {
            var interfaceId = $("#interfaceId").val();
            var runStep = $("#runStep1").val();
            var runTime = $("#runTime").val();
            if(interfaceId != '' && interfaceId != null && runStep != '-1' && runTime != '' && runTime != null) {
                $.ajax({
                    url: "runInterface",
                    data: {
                        interfaceId: interfaceId,
                        step: runStep,
                        time: runTime
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            layer.closeAll();
                            layer.msg("提交成功", {icon: 1});
                            active.reload();
                        } else {
                            layer.closeAll();
                            layer.msg("提交失败", {icon: 5});
                            active.reload();
                        }
                    }
                })
            }else{
                layer.msg("请将接口编码、运行步骤、运行时间填写完整", {icon: 5});
            }
        }
    };
    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })
});