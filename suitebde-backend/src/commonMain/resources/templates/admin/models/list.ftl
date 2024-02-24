<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white">
            <@t key="admin_menu_" + route />
        </h2>

        <nav>
            <ol class="flex items-center gap-2">
                <a href="${route}/create"
                   class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray"
                   id="admin_create">
                    <@t key="admin_" + route + "_create" />
                </a>
            </ol>
        </nav>
    </div>
    <!-- Breadcrumb End -->

    <div class="flex flex-col gap-5 md:gap-7 2xl:gap-10">
        <!-- ====== Data Table Two Start -->
        <div class="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
            <div class="data-table-common data-table-one max-w-full overflow-x-auto">
                <table class="table w-full table-auto" id="dataTableTwo">
                    <thead>
                    <tr>
                        <#list keys as key>
                            <th id="th_${key.key}">
                                <div class="flex items-center justify-between gap-1.5">
                                    <p><@t key="admin_${route}_${key.key}" /></p>
                                    <div class="inline-flex flex-col space-y-[2px]">
                                            <span class="inline-block">
                                                <svg class="fill-current" width="10" height="5" viewBox="0 0 10 5"
                                                     fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M5 0L0 5H10L5 0Z" fill=""/>
                                                </svg>
                                            </span>
                                        <span class="inline-block">
                                                <svg class="fill-current" width="10" height="5" viewBox="0 0 10 5"
                                                     fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M5 5L10 0L-4.37114e-07 8.74228e-07L5 5Z" fill=""/>
                                                </svg>
                                            </span>
                                    </div>
                                </div>
                            </th>
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
        <!-- ====== Data Table Two End -->
    </div>
</@template.page>

<#macro cell item key>
    <td>
        <#switch key.type>
            <#case "id">
                <a href="${route}/${item[key.key]}/update">${item[key.key]}</a>
                <#break>
            <#case "password">
                ********
                <#break>
            <#case "boolean">
                <div class="text-xs d-flex align-items-center">
                    <button class="btn btn-icon-only btn-rounded btn-outline-<#if item[key.key]>success<#else>danger</#if> mb-0 me-2 btn-sm d-flex align-items-center justify-content-center">
                        <i class="fas fa-<#if item[key.key]>check<#else>times</#if>" aria-hidden="true"></i>
                    </button>
                    <span>
                        <#if item[key.key]>
                            <@t key="boolean_true" />
                        <#else>
                            <@t key="boolean_false" />
                        </#if>
                    </span>
                </div>
                <#break>
            <#case "url_webpages">
                <a href="/pages/${item[key.key]}">/pages/${item[key.key]}</a>
                <#break>
            <#default>
                ${item[key.key]}
        </#switch>
    </td>
</#macro>
