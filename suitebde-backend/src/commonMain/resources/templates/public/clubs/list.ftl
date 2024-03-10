<#import "../../template.ftl" as template>
<#import "../../components/clubs.ftl" as clubs>
<@template.page>
    <section class="py-20 lg:py-25 xl:py-30">
        <div class="mx-auto max-w-1280 px-4 md:px-8 xl:px-0 mt-12.5 lg:mt-17.5">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-7.5 xl:gap-10">
                <!-- Club Card -->
                <#list items as club>
                    <@clubs.card club />
                </#list>
            </div>

            <!-- Pagination -->
            <div class="mt-10 lg:mt-15 xl:mt-20 relative z-1">
                <nav>
                    <ul class="flex items-center justify-center gap-3">
                        <li>
                            <a class="group flex items-center justify-center w-7.5 md:w-10 h-7.5 md:h-10 rounded-full text-xs md:text-base shadow-6 dark:shadow-none ease-in-out duration-300 bg-white dark:bg-blacksection hover:bg-primary dark:hover:bg-primary hover:text-white dark:hover:text-white"
                               href="#">
                                <svg class="fill-body group-hover:fill-white ease-in-out duration-300" width="8"
                                     height="14" viewBox="0 0 8 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M2.93884 6.99999L7.88884 11.95L6.47484 13.364L0.11084 6.99999L6.47484 0.635986L7.88884 2.04999L2.93884 6.99999Z"/>
                                </svg>
                            </a>
                        </li>
                        <li>
                            <a class="group flex items-center justify-center w-7.5 md:w-10 h-7.5 md:h-10 rounded-full text-xs md:text-base shadow-6 dark:shadow-none ease-in-out duration-300 bg-white dark:bg-blacksection hover:bg-primary dark:hover:bg-primary hover:text-white dark:hover:text-white"
                               href="#">
                                2
                            </a>
                        </li>
                        <li>
                            <a class="group flex items-center justify-center w-7.5 md:w-10 h-7.5 md:h-10 rounded-full text-xs md:text-base shadow-6 dark:shadow-none ease-in-out duration-300 bg-white dark:bg-blacksection hover:bg-primary dark:hover:bg-primary hover:text-white dark:hover:text-white"
                               href="#">
                                3
                            </a>
                        </li>
                        <li>
                            <a class="group flex items-center justify-center w-7.5 md:w-10 h-7.5 md:h-10 rounded-full text-xs md:text-base shadow-6 dark:shadow-none ease-in-out duration-300 bg-white dark:bg-blacksection hover:bg-primary dark:hover:bg-primary hover:text-white dark:hover:text-white"
                               href="#">
                                <svg class="fill-body group-hover:fill-white ease-in-out duration-300" width="8"
                                     height="14" viewBox="0 0 8 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M5.06067 7.00001L0.110671 2.05001L1.52467 0.636014L7.88867 7.00001L1.52467 13.364L0.110672 11.95L5.06067 7.00001Z"
                                          fill="#fefdfo"/>
                                </svg>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            <!-- Pagination -->
        </div>
    </section>
</@template.page>
