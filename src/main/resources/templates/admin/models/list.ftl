<#import "../template.ftl" as template>
<@template.page>
    <div class="container-fluid py-4">
        <div class="d-sm-flex justify-content-between">
            <div>
                <a href="/admin/${route}/create" class="btn btn-icon btn-outline-primary" id="admin_create">
                    <@t key="admin_" + route + "_create" />
                </a>
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="table-responsive">
                        <table class="table table-flush" id="datatable-search">
                            <thead class="thead-light">
                            <tr>
                                <#list keys as key>
                                    <th id="th_${key.name}"><@t key="admin_${route}_${key.name}" /></th>
                                </#list>
                            </tr>
                            </thead>
                            <tbody>
                            <#list items as item>
                                <tr>
                                    <#list keys as key>
                                        <@cell item key />
                                    </#list>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@3.0.2/dist/umd/simple-datatables.js"></script>
    <script>
        const dataTableSearch = new simpleDatatables.DataTable("#datatable-search", {
            searchable: true,
            fixedHeight: false,
            perPageSelect: false
        });
    </script>
</@template.page>

<#macro cell item key>
    <td class="font-weight-bold">
        <#switch key.type>
            <#case "ID">
                <span class="my-2 text-xs">
                    <a href="/admin/${route}/${item[key.name]}">${item[key.name]}</a>
                </span>
                <#break>
            <#default>
                <span class="my-2 text-xs">${item[key.name]}</span>
        </#switch>
    </td>
</#macro>
