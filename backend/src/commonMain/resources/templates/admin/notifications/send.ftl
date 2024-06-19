<#import "../template.ftl" as template>
<@template.page>
    <!-- Breadcrumb Start -->
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 class="text-title-md2 font-bold text-black dark:text-white" id="admin_delete">
            <@t key="admin_menu_notifications" />
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
                        <div class="mb-4.5 grid xl:grid-cols-12 grid-cols-6 gap-6">
                            <div class="mb-4.5 col-span-12">
                                <label for="topic" class="mb-2.5 block text-black dark:text-white">
                                    <@t key="admin_notifications_topic" />
                                </label>
                                <select class="relative z-20 w-full appearance-none rounded border border-stroke bg-transparent py-3 px-5 outline-none transition focus:border-primary active:border-primary dark:border-form-strokedark dark:bg-form-input"
                                        name="topic" id="topic">
                                    <#list topics.topics as key, name>
                                        <option value="${key}">${name}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="mb-4.5 col-span-12">
                                <label for="title" class="mb-2.5 block text-black dark:text-white">
                                    <@t key="admin_notifications_title" />
                                </label>
                                <input type="text"
                                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                                       name="title" id="title">
                            </div>
                            <div class="mb-4.5 col-span-12">
                                <label for="body" class="mb-2.5 block text-black dark:text-white">
                                    <@t key="admin_notifications_body" />
                                </label>
                                <input type="text"
                                       class="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                                       name="body" id="body">
                            </div>
                        </div>

                        <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
                            <input type="submit"
                                   class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray"
                                   value="<@t key="admin_send" />"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- ====== Form Layout Section End -->
</@template.page>
