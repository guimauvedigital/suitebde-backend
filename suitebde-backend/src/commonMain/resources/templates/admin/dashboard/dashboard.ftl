<#import "../template.ftl" as template>
<#import "../../components/dashboard.ftl" as dashboard>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_delete">
            <@t key="admin_menu_dashboard" />
        </h2>
    </div>
    <!-- Breadcrumb End -->

    <#if !stripe>
        <@dashboard.stripeAlert />
    </#if>
</@template.page>
