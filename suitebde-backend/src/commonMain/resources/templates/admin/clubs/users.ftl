<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white">
            <@t key="admin_menu_clubs_users" />
        </h2>

        <nav>
            <ol class="flex items-center gap-2">
                <a href="${route}/create"
                   class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray"
                   id="admin_create">
                    <@t key="admin_clubs_users_add" />
                </a>
            </ol>
        </nav>
    </div>
    <!-- Breadcrumb End -->

    <div class="flex flex-col gap-5 md:gap-7 2xl:gap-10">
        <div class="rounded-sm border border-stroke bg-white py-6 px-7.5 shadow-default dark:border-strokedark dark:bg-boxdark grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 xl:grid-cols-4 2xl:gap-7.5">
            <#list items as user>
                <div class="flex-col">
                    ${user.user.firstName} ${user.user.lastName}<br/>
                    TODO: Make that shit looks great
                </div>
            </#list>
        </div>
    </div>
</@template.page>
