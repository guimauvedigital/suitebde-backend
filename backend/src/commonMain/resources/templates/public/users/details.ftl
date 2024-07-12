<#import "../../template.ftl" as template>
<@template.page>
    <section class="pt-35 lg:pt-45 xl:pt-50 pb-20 lg:pb-25 xl:pb-30">
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <div class="flex flex-col lg:flex-row gap-7.5 xl:gap-17.5">
                <div class="lg:w-[70%]">
                    <div class="animate_top rounded-md shadow-solid-13">
                        <img class="w-full aspect-21/9 object-cover"
                             src="<#if item.logo??>${item.logo}<#else>/img/default_event_image.jpg</#if>"
                             alt="${item.firstName} ${item.lastName}"/>

                        <h2 id="webpages_title"
                            class="font-medium text-3xl 2xl:text-title-lg text-black dark:text-white mt-11 mb-5">
                            ${item.firstName} ${item.lastName}
                        </h2>
                    </div>
                </div>

                <div class="md:w-1/2 lg:w-[27.7%]">
                    <div class="animate_top mb-10">
                        <#if user?? && user.id == item.id>
                            <h4 class="text-2xl text-black dark:text-white mb-7.5"><@t key="users_profile_actions" /></h4>

                            <div class="flex flex-col gap-5">
                                <!--
                                <a href="/${locale}/users/${item.id}/update"
                                   class="flex items-center justify-center w-full h-12 bg-primary text-white rounded-md">
                                    <@t key="users_profile_update" />
                                </a>
                                -->

                                <a href="/${locale}/users/${item.id}/delete"
                                   class="flex items-center justify-center w-full h-12 bg-[#C01020] text-white rounded-md">
                                    <@t key="users_profile_delete" />
                                </a>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </section>
</@template.page>
