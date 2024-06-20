<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_delete">
            <@t key="admin_" + route + "_delete" />
        </h2>
    </div>
    <!-- Breadcrumb End -->

    <!-- ====== Form Layout Section Start -->
    <div class="grid grid-cols-1 gap-9">
        <div class="flex flex-col gap-9">
            <!-- Contact Form -->
            <div class="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
                <form method="post" id="form">
                    <div class="p-6.5 grid grid-flow-col gap-6">
                        <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
                            <a class="flex w-full justify-center rounded bg-black p-3 font-medium text-gray"
                               href="../../${route}">
                                <@t key="admin_cancel" />
                            </a>
                            <input type="submit"
                                   class="flex w-full justify-center rounded bg-danger p-3 font-medium text-gray"
                                   value="<@t key="admin_delete" />"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- ====== Form Layout Section End -->
</@template.page>
