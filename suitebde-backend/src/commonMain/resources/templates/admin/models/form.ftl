<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <#if item??>
            <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_update">
                <@t key="admin_" + route + "_update" />
            </h2>
        <#else>
            <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_create">
                <@t key="admin_" + route + "_create" />
            </h2>
        </#if>
    </div>
    <!-- Breadcrumb End -->

    <!-- ====== Form Layout Section Start -->
    <div class="grid grid-cols-1 gap-9">
        <div class="flex flex-col gap-9">
            <!-- Contact Form -->
            <div class="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
                <form method="post" id="form">
                    <div class="p-6.5 grid grid-flow-col gap-6">
                        <div class="mb-4.5 grid xl:grid-cols-12 grid-cols-6 gap-6">
                            <#list keys as key>
                                <@field key />
                            </#list>
                        </div>

                        <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
                            <a class="flex w-full justify-center rounded bg-black p-3 font-medium text-gray"
                               href="../<#if item??>../</#if>${route}">
                                <@t key="admin_cancel" />
                            </a>
                            <#if item??>
                                <a class="flex w-full justify-center rounded bg-danger p-3 font-medium text-gray"
                                   href="delete">
                                    <@t key="admin_delete" />
                                </a>
                            </#if>
                            <input type="submit"
                                   class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray"
                                   value="<@t key="admin_save" />"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- ====== Form Layout Section End -->

    <#if flatpickr?? && flatpickr>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script>
            if (document.querySelector('.datetimepicker')) {
                flatpickr('.datetimepicker', {
                    allowInput: true,
                    enableTime: true,
                    dateFormat: "Z",
                    altInput: true,
                    altFormat: "d/m/Y à H:i"
                });
            }
        </script>
    </#if>
</@template.page>

<#macro field key>
    <#if key.type == "id">
        <#return />
    </#if>
    <div class="mb-4.5 col-span-<#if key.style != "">${key.style}<#else>12</#if>">
        <label for="${key.key}" class="mb-2.5 block text-black dark:text-white">
            <@t key="admin_${route}_${key.key}" />
        </label>
        <#switch key.type>
            <#case "password">
                <input type="password"
                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                       name="${key.key}" id="${key.key}"
                       <#if !key.editable>disabled</#if>>
                <#break>
            <#case "boolean">
                <div class="form-check form-switch ms-1">
                    <input class="form-check-input" type="checkbox"
                           name="${key.key}" id="${key.key}"
                           <#if item?? && item[key.key]>checked</#if>>
                    <label class="form-check-label" for="${key.key}"></label>
                </div>
                <#break>
            <#case "integer">
                <input type="number"
                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                       name="${key.key}" id="${key.key}"
                       <#if item??>value="${item[key.key]}"</#if>
                        <#if !key.editable>disabled</#if>>
                <#break>
            <#case "date">
                <input type="text"
                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary datetimepicker"
                       name="${key.key}" id="${key.key}"
                       <#if item??>value="${item[key.key]}"</#if>
                        <#if !key.editable>disabled</#if>>
                <#break>
            <#case "url_webpages">
                <div class="input-group mb-3">
                    <span class="input-group-text" id="${key.key}-prefix">/pages/</span>
                    <input type="text" class="form-control"
                           name="${key.key}" id="${key.key}"
                           aria-describedby="${key.key}-prefix" <#if item??>value="${item[key.key]}"</#if>
                            <#if !key.editable>disabled</#if>>
                </div>
                <#break>
            <#default>
                <input type="text"
                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                       name="${key.key}" id="${key.key}"
                       <#if item?? && item[key.key]??>value="${item[key.key]}"</#if>
                        <#if !key.editable>disabled</#if>>
        </#switch>
    </div>
</#macro>
