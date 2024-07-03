<#import "../../template.ftl" as template>
<@template.page>
    <section class="pt-35 lg:pt-45 xl:pt-50 pb-20 lg:pb-25 xl:pb-30">
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <div class="flex flex-col lg:flex-row gap-7.5 xl:gap-17.5">
                <div class="animate_top rounded-md shadow-solid-13">
                    <h2 id="webpages_title"
                        class="font-medium text-3xl 2xl:text-title-lg text-black dark:text-white mt-11 mb-5">
                        ${item.title}
                    </h2>

                    ${content}
                </div>
            </div>
        </div>
    </section>
</@template.page>
