<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white">
            <@t key="admin_menu_roles_permissions" />
        </h2>
    </div>
    <!-- Breadcrumb End -->

    <div class="flex flex-col gap-5 md:gap-7 2xl:gap-10">
        <div class="rounded-sm border border-stroke bg-white py-6 px-7.5 shadow-default dark:border-strokedark dark:bg-boxdark">
            <form method="post" id="form">
                <div class="mb-4.5 grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 xl:grid-cols-4 2xl:gap-7.5">
                    <#list permissions as permission>
                        <div x-data="{ checkboxToggle: ${items?seq_contains(permission)?string("true", "false")} }"
                             class="flex-col">
                            <label for="checkbox_${permission}" class="flex cursor-pointer select-none items-center">
                                <div class="relative">
                                    <input type="checkbox" id="checkbox_${permission}" name="${permission}"
                                           class="sr-only" @change="checkboxToggle = !checkboxToggle"
                                           <#if items?seq_contains(permission)>checked</#if>/>
                                    <div :class="checkboxToggle && 'border-primary bg-gray dark:bg-transparent'"
                                         class="mr-4 flex h-5 w-5 items-center justify-center rounded border">
                                        <span :class="checkboxToggle && 'bg-primary'"
                                              class="h-2.5 w-2.5 rounded-sm"></span>
                                    </div>
                                </div>
                                ${permission}
                            </label>
                        </div>
                    </#list>
                </div>
                <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
                    <a class="flex w-full justify-center rounded bg-black p-3 font-medium text-gray" href="update">
                        <@t key="admin_cancel" />
                    </a>
                    <input type="submit"
                           class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray"
                           value="<@t key="admin_save" />"/>
                </div>
            </form>
        </div>
    </div>
</@template.page>
