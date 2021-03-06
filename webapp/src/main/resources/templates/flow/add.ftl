<#include "../include/common-macro.ftl">
<#include "../include/menu-tree-marco.ftl">
<@main "申请审批">
<div class="main-container" id="main-container">
    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <div class="sidebar" id="sidebar">

            <div class="sidebar-shortcuts" id="sidebar-shortcuts">
                <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
                    <button class="btn btn-success">
                        <i class="icon-signal"></i>
                    </button>

                    <button class="btn btn-info">
                        <i class="icon-pencil"></i>
                    </button>

                    <button class="btn btn-warning">
                        <i class="icon-group"></i>
                    </button>

                    <button class="btn btn-danger">
                        <i class="icon-cogs"></i>
                    </button>
                </div>

                <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
                    <span class="btn btn-success"></span>

                    <span class="btn btn-info"></span>

                    <span class="btn btn-warning"></span>

                    <span class="btn btn-danger"></span>
                </div>
            </div><!-- #sidebar-shortcuts -->

            <ul class="nav nav-list">
                <#if menuTree??>
                      <@menuTreeLoop menuTree.menuTreeList></@menuTreeLoop>
                </#if>

            </ul><!-- /.nav-list -->
        </div>

        <div class="main-content">
            <div class="breadcrumbs">
                <ul class="breadcrumb">
                    <li>
                        <a href="${ctx}">首页</a>
                    </li>
                    <li>
                        <a href="#">申请审批</a>
                    </li>
                </ul>

            </div>

            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="col-md-offset-1 col-md-10">
                            <div class="panel panel-primary menu-add">
                                <div class="panel-heading">
                                    <div class="panel-title">
                                        申请审批
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <form id="function-new">
                                        <div class="menu-add-one">
                                            1、选择流程：
                                            <select name="processId" id="processId" class="form-control">
                                                <option value="">请选择申请流程</option>
                                                <#if processList??>
                                                    <#list processList as process>
                                                        <option value="${process.processId}">${process.processName}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="menu-add-one">
                                            2、点击<a id="processUrl" href="javascript:;">[下载模板]</a>
                                        </div>
                                        <div class="menu-add-one">
                                            3、上传写好的申请(请务必要将写好的申请，命名成：名字+日期)
                                            <input type="hidden" id="flowUrl" name="flowUrl" value="">
                                            <input type="file"  class="form-control" id="file">
                                        </div>

                                        <div class="tip" style="clear: both"></div>
                                        <button class="btn btn-primary menu-add-submit" style="display: block;clear: both">提交</button>
                                        <div style="margin: 28px 0 0 0">
                                            <h5>注意事项</h5>
                                            <ul>
                                                <li>1、选择要申请的流程</li>
                                                <li>2、下载申请模板</li>
                                                <li>3、填写并命名好格式（名字+日期），然后上传</li>
                                            </ul>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</@main>

<script>
    $('.menu-add-submit').click(function () {

        if($('#processId').val() == null || $('#processId').val() == ""){
            showTip("请选择一个流程");
            return false;
        } else if($('#flowUrl').val() == null || $('#flowUrl').val() == ""){
            showTip("请上传提交的内容");
            return false;
        }

        $.ajax({
            url:"${ctx}/flow/add",
            data:$("#function-new").serialize(),
            type:"POST",
            success:function (data) {
                showTip(data);
                setTimeout("window.location.reload()",1500);
            },
            error:function (xr) {
                showTip(xr.responseText);
            }
        })
        return false;
    })

    $("#file").change(function () {
        var formDate = new FormData();
        formDate.append("file",$('#file')[0].files[0]);
        $.ajax({
            url:"http://upload.zkdcloud.cn",
            data:formDate,
            cache: false,
            type:"POST",
            processData: false,
            contentType: false,
            success:function (data) {
                $('#flowUrl').val(data);
            }
        })
    })

    $("#processId").change(function () {
        $.ajax({
            url:"${ctx}/flow/getUrl",
            method:"POST",
            data:{"processId":$('#processId').val()},
            async:false,
            success:function (data) {
                $('#processUrl').attr("href",data);
            },
            error:function (xr) {
                showTip(xr.responseText);
            }
        })
    })
</script>