<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_delete">
            <@t key="admin_settings" />
        </h2>
    </div>
    <!-- Breadcrumb End -->

    <!-- ===== External Links Start ===== -->
    <div class="col-span-12 rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark xl:col-span-5">
        <div class="flex items-start justify-between border-b border-stroke py-5 px-6 dark:border-strokedark">
            <div>
                <h2 class="text-title-md2 font-bold text-black dark:text-white"><@t key="admin_settings_stripe"/></h2>
            </div>
        </div>

        <div class="px-3 pb-5">
            <#if stripe?has_content>
                <#list stripe as account>
                    <div class="group flex items-center justify-between rounded-md p-4.5 hover:bg-gray-2 dark:hover:bg-graydark">
                        <div class="flex items-center gap-4">
                            <h4 class="font-medium text-black group-hover:text-primary dark:text-white dark:group-hover:text-primary">
                                #${account.accountId}
                            </h4>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="group flex items-center justify-between rounded-md p-4.5 hover:bg-gray-2 dark:hover:bg-graydark">
                    <div class="flex items-center gap-4">
                        <a href="/admin/settings/stripe">
                            <h4 class="font-medium text-black group-hover:text-primary dark:text-white dark:group-hover:text-primary">
                                <@t key="admin_settings_stripe_link"/>
                            </h4>
                        </a>
                    </div>

                    <a href="/admin/settings/stripe" class="inline-block">
                        <svg class="fill-current" width="16" height="16" viewBox="0 0 16 16" fill="none"
                             xmlns="http://www.w3.org/2000/svg">
                            <g clip-path="url(#clip0_552_42876)">
                                <path d="M14.4339 9.44989C14.4339 9.03453 14.7719 8.69729 15.1883 8.69729C15.6046 8.69729 15.9426 9.03453 15.9426 9.44989V13.7715C15.9426 14.3848 15.692 14.9421 15.2874 15.3457C14.8829 15.7494 14.3243 15.9994 13.7096 15.9994H2.23295C1.61827 15.9994 1.05971 15.7494 0.655139 15.3457C0.250571 14.9421 0 14.3848 0 13.7715V2.23765C0 1.62437 0.250571 1.06708 0.655139 0.663433C1.05971 0.259787 1.61827 0.00978681 2.23295 0.00978681H6.53703C6.95334 0.00978681 7.29135 0.347026 7.29135 0.762391C7.29135 1.17776 6.95334 1.515 6.53703 1.515H2.23295C2.03458 1.515 1.85449 1.59703 1.72268 1.72723C1.59086 1.85875 1.50995 2.03843 1.50995 2.23635V13.7702C1.50995 13.9681 1.59217 14.1478 1.72268 14.2793C1.85449 14.4108 2.03458 14.4916 2.23295 14.4916H13.7109C13.9093 14.4916 14.0894 14.4095 14.2212 14.2793C14.353 14.1491 14.4339 13.9681 14.4339 13.7702V9.44989ZM14.6715 2.27281L7.04861 9.97333C6.75759 10.2689 6.27993 10.2728 5.98369 9.98244C5.68744 9.69208 5.68352 9.21552 5.97455 8.91994L13.3038 1.515H10.2525C9.83622 1.515 9.49821 1.17776 9.49821 0.762391C9.49821 0.347026 9.83622 0.00978681 10.2525 0.00978681H13.7109C14.3791 0.00978681 15.2405 -0.103494 15.7533 0.413433C16.077 0.740256 16.0052 3.34572 15.9635 4.85484C15.953 5.24546 15.9439 5.54364 15.9439 5.75718C15.9439 6.17255 15.6059 6.50979 15.1896 6.50979C14.7732 6.50979 14.4352 6.17255 14.4352 5.75718C14.4352 5.71682 14.4457 5.32489 14.46 4.81447C14.4809 4.02802 14.6075 2.98635 14.6715 2.27281Z"
                                      fill=""/>
                            </g>
                            <defs>
                                <clipPath id="clip0_552_42876">
                                    <rect width="16" height="16" fill="white"/>
                                </clipPath>
                            </defs>
                        </svg>
                    </a>
                </div>
            </#if>

        </div>
    </div>
    <!-- ===== External Links End ===== -->
</@template.page>
