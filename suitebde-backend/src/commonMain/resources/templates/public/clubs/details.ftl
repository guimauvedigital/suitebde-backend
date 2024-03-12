<#import "../../template.ftl" as template>
<@template.page>
    <section class="pt-35 lg:pt-45 xl:pt-50 pb-20 lg:pb-25 xl:pb-30">
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <div class="flex flex-col lg:flex-row gap-7.5 xl:gap-17.5">
                <div class="lg:w-[70%]">
                    <div class="animate_top rounded-md shadow-solid-13">
                        <img class="w-full aspect-21/9 object-cover"
                             src="<#if item.logo??>${item.logo}<#else>/img/default_event_image.jpg</#if>"
                             alt="${item.name}"/>

                        <h2 id="webpages_title"
                            class="font-medium text-3xl 2xl:text-title-lg text-black dark:text-white mt-11 mb-5">
                            ${item.name}
                        </h2>

                        <p>
                            ${item.description}
                        </p>
                    </div>
                </div>

                <div class="md:w-1/2 lg:w-[27.7%]">
                    <div class="animate_top mb-10">
                        <h4 class="text-2xl text-black dark:text-white mb-7.5"><@t key="clubs_members_title" /></h4>

                        <#list users as user>
                            <div class="flex gap-4 2xl:gap-6 mb-7.5">
                                <div class="w-[44px] rounded overflow-hidden">
                                    <img src="" class="aspect-square" alt="User avatar"/>
                                </div>
                                <div>
                                    <p class="text-black dark:text-white hover:text-primary dark:hover:text-primary ease-in-out duration-300">
                                        <a href="/users/${user.userId}">${user.user.firstName} ${user.user.lastName}</a>
                                    </p>
                                    <p>${user.role.name}</p>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </section>
</@template.page>
