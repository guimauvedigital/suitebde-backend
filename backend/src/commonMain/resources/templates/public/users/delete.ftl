<#import "../../template.ftl" as template>
<@template.page>
    <section class="pt-35 lg:pt-45 xl:pt-50 pb-20 lg:pb-25 xl:pb-30">
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <div class="flex flex-col gap-7.5 xl:gap-17.5">
                <h2 id="webpages_title"
                    class="font-medium text-3xl 2xl:text-title-lg text-black dark:text-white mt-11 mb-5">
                    <@t key="users_profile_delete_confirm" />
                </h2>

                <div class="flex flex-col md:flew-row">
                    <form method="post" id="form" class="gap-4">
                        <a class="flex items-center justify-center w-full h-12 bg-primary text-white rounded-md"
                           href="/auth/profile">
                            <@t key="admin_cancel" />
                        </a>
                        <input type="submit"
                               class="flex items-center justify-center w-full h-12 bg-[#C01020] text-white rounded-md"
                               value="<@t key="admin_delete" />"/>
                    </form>
                </div>
            </div>
        </div>
    </section>
</@template.page>
