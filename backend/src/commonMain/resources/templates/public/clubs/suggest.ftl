<#import "../../template.ftl" as template>
<@template.page>
    <section class="py-20 lg:py-25 xl:py-30">
        <div class="animate_top w-full p-7.5 xl:p-14">
            <form method="post">
                <h2 id="webpages_title"
                    class="font-medium text-3xl 2xl:text-title-lg text-black dark:text-white mb-5">
                    <@t key="club_suggest_title" />
                </h2>

                <div class="mb-7.5">
                    <label class="block mb-4" for="name"><@t key="club_name" /></label>
                    <input type="text" name="name" id="name"
                           class="w-full bg-transparent rounded-lg shadow-4 dark:shadow-none border border-strokedark dark:border-stroke focus-visible:outline-none focus:border-primary dark:focus:border-primary focus:shadow-5 placeholder:text-body/50 py-3.5 px-6"/>
                </div>

                <div class="mb-10">
                    <label class="block mb-4" for="description"><@t key="club_description" /></label>
                    <textarea rows="4" name="description" id="description"
                              class="w-full bg-transparent rounded-lg shadow-4 dark:shadow-none border border-strokedark dark:border-stroke focus-visible:outline-none focus:border-primary dark:focus:border-primary focus:shadow-5 placeholder:text-body/50 p-6"></textarea>
                </div>

                <div class="flex flex-col lg:flex-row lg:justify-between gap-7.5 lg:gap-10 mb-7.5">
                    <div class="w-full lg:w-1/2">
                        <label class="block mb-4" for="memberRoleName"><@t key="club_role_member" /></label>
                        <input type="text" name="memberRoleName" id="memberRoleName"
                               class="w-full bg-transparent rounded-lg shadow-4 dark:shadow-none border border-strokedark dark:border-stroke focus-visible:outline-none focus:border-primary dark:focus:border-primary focus:shadow-5 placeholder:text-body/50 py-3.5 px-6"/>
                    </div>

                    <div class="w-full lg:w-1/2">
                        <label class="block mb-4" for="adminRoleName"><@t key="club_role_admin" /></label>
                        <input type="text" name="adminRoleName" id="adminRoleName"
                               class="w-full bg-transparent rounded-lg shadow-4 dark:shadow-none border border-strokedark dark:border-stroke focus-visible:outline-none focus:border-primary dark:focus:border-primary focus:shadow-5 placeholder:text-body/50 py-3.5 px-6"/>
                    </div>
                </div>

                <div class="flex justify-end">
                    <input type="submit" value="<@t key="club_submit" />"
                           class="inline-flex rounded-full text-white bg-primary ease-in-out duration-300 px-7.5 py-3 hover:shadow-1"/>
                </div>
            </form>
        </div>
    </section>
</@template.page>
