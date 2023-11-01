<#import "../template.ftl" as template>
<@template.page>
    <div class="container-fluid py-4">
        <div class="row">
            <div class="col-md-12">
                <div class="card card-body mt-4">
                    <#if item??>
                        <h6 class="mb-0" id="admin_update"><@t key="admin_" + route + "_update" /></h6>
                    <#else>
                        <h6 class="mb-0" id="admin_create"><@t key="admin_" + route + "_create" /></h6>
                    </#if>
                    <hr class="horizontal dark my-3">

                    <form method="post" id="form">
                        <div class="row">
                            <#list keys as key>
                                <@field key />
                            </#list>
                        </div>

                        <div class="d-flex justify-content-end mt-3">
                            <a class="btn btn-light m-0" href="/admin/${route}">
                                <@t key="admin_cancel" />
                            </a>
                            <#if item??>
                                <a class="btn btn-danger m-0 ms-2" href="/admin/${route}/${item.id}/delete">
                                    <@t key="admin_delete" />
                                </a>
                            </#if>
                            <input type="submit"
                                   class="btn bg-gradient-primary m-0 ms-2"
                                   value="<@t key="admin_save" />">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@template.page>

<#macro field key>
    <#if key.type == "ID">
        <#return />
    </#if>
    <div class="col-md-${key.col} mb-3">
        <label for="${key.name}" class="form-label">
            <@t key="admin_${route}_${key.name}" />
        </label>
        <#switch key.type>
            <#case "PASSWORD">
                <input type="password" class="form-control"
                       name="${key.name}" id="${key.name}"
                       <#if !key.editable>disabled</#if>>
                <#break>
            <#default>
                <input type="text" class="form-control"
                       name="${key.name}" id="${key.name}"
                       <#if item??>value="${item[key.name]}"</#if>
                        <#if !key.editable>disabled</#if>>
        </#switch>
    </div>
</#macro>
