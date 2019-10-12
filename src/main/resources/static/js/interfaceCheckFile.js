//数据表格
layui.use(['table', 'form'], function () {
    var table = layui.table;
    var form = layui.form;
    var isEdit = false;
    var editData;

    var url = 'getCheckFile';
    table.render({
        elem: '#interfaceCheckFile'
        , url: url
        // , page: {
        //     groups: 3 //只显示 1 个连续页码
        //     , first: '首页'
        //     , last: '尾页'
        //     , limit: 12
        //     , layout: ['prev', 'page', 'next']
        // }
        , id: 'interfaceCheckFile'
        , height: 580
        , cols: [[
            {field: 'numbers', align: 'center', width: '5%', title: '序号'}
            , {field: 'interfaceId', align: 'center', width: '10%', title: '接口编号'}
            , {field: 'filetype', align: 'center', width: '10%', title: '文件类型'}
            , {field: 'filename', align: 'center', width: '25%', title: '文件名称'}
            , {field: 'filecontent', align: 'center', width: '42%', title: '文件内容'}
            , {field: 'date', align: 'center', width: '10%', title: '日期'}
        ]]

    });
    //数据重载（模糊搜索）
    var $ = layui.$, active = {
        reload: function () {
            var fileDate = $('#fileDate').val().replace("-", "");
            console.log("fileDate==" + fileDate)
            //执行重载
            table.reload('interfaceCheckFile', {
                // page: {
                //     curr: 1 //重新从第 1 页开始
                // }
                // ,
                where: {
                    fileDate: fileDate
                }
            });
        },
        runInterface: function () {
            var fileDate = $("#fileDate").val();
            if (fileDate != '' && fileDate != null) {
                $.ajax({
                    url: "runInterface",
                    data: {
                        fileDate: fileDate
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
            } else {
                layer.msg("请填写校验文件时间", {icon: 5});
            }
        }
    };
    $('.demoTable .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })
});