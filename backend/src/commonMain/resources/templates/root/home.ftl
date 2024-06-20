<#import "../template.ftl" as template>
<@template.page>
    <!-- ===== Hero Start ===== -->
    <section class="pt-35 md:pt-40 xl:pt-52 pb-20 lg:pb-30 xl:pb-59 relative overflow-hidden">
        <!-- Hero Images -->
        <div class="hidden md:block w-1/2 2xl:w-187.5 h-auto 2xl:h-171.5 absolute right-0 top-0">
            <img src="/img/hero.png" alt="Hero" class="absolute right-0 top-0 z-1"/>
        </div>

        <!-- Hero Content -->
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <div class="flex lg:items-center">
                <div class="animate_left md:w-1/2">
                    <h1 class="font-semibold text-3xl lg:text-4xl xl:text-title-xxl text-black dark:text-white mb-6">
                        <@t key="hero_title"/>
                    </h1>
                    <p class="xl:w-[79%]">
                        <@t key="hero_description"/>
                    </p>

                    <div class="flex flex-col-reverse lg:flex-row gap-7.5 mt-10">
                        <a href="/${locale}/auth/join"
                           class="font-medium leading-7 text-white bg-primary py-3 px-7.5 rounded-full ease-in-out duration-300 inline-flex w-fit hover:shadow-1">
                            <@t key="auth_field_join"/>
                        </a>

                        <span class="flex flex-col">
                            <a href="mailto:hey@suitebde.com"
                               class="inline-block font-medium text-lg text-black dark:text-white">
                                <@t key="hero_email" args=["hey@suitebde.com"]/>
                            </a>
                            <span class="inline-block">
                                <@t key="hero_email_description"/>
                            </span>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- ===== Hero End ===== -->

    <!-- ===== Small Features Start ===== -->
    <section id="features">
        <div class="mx-auto max-w-1390 px-4 md:px-7.5 2xl:px-12.5">
            <div class="flex flex-wrap lg:flex-nowrap justify-center lg:justify-between gap-7.5 lg:gap-5 xl:gap-22.5">
                <!-- Small Features Item -->
                <div class="animate_top md:w-[45%] lg:w-1/3 flex gap-5 xl:gap-7.5">
                    <div class="flex items-center justify-center shrink-0 w-21 h-21 rounded-full bg-primary">
                        <img src="/img/icon-01.svg" alt="Icon"/>
                    </div>
                    <div>
                        <h4 class="font-medium text-xl md:text-title-sm text-black dark:text-white mb-2.5">
                            <@t key="features_one_title"/>
                        </h4>
                        <p><@t key="features_one_description"/></p>
                    </div>
                </div>

                <!-- Small Features Item -->
                <div class="animate_top md:w-[45%] lg:w-1/3 flex gap-5 xl:gap-7.5">
                    <div class="flex items-center justify-center shrink-0 w-21 h-21 rounded-full bg-primary">
                        <img src="/img/icon-02.svg" alt="Icon"/>
                    </div>
                    <div>
                        <h4 class="font-medium text-xl md:text-title-sm text-black dark:text-white mb-2.5">
                            <@t key="features_two_title"/>
                        </h4>
                        <p><@t key="features_two_description"/></p>
                    </div>
                </div>

                <!-- Small Features Item -->
                <div class="animate_top md:w-[45%] lg:w-1/3 flex gap-5 xl:gap-7.5">
                    <div class="flex items-center justify-center shrink-0 w-21 h-21 rounded-full bg-primary">
                        <img src="/img/icon-03.svg" alt="Icon"/>
                    </div>
                    <div>
                        <h4 class="font-medium text-xl md:text-title-sm text-black dark:text-white mb-2.5">
                            <@t key="features_three_title"/>
                        </h4>
                        <p><@t key="features_three_description"/></p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- ===== Small Features End ===== -->
</@template.page>
