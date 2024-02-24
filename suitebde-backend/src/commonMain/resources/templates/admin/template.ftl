<#macro page>
    <!DOCTYPE html>
    <html lang="${locale}">
    <head>
        <meta charset="UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title><#if title??>${title} - </#if>Suite BDE</title>
        <link rel="apple-touch-icon" sizes="76x76" href="/img/logo_rounded.png">
        <link rel="icon" type="image/png" href="/img/logo_rounded.png">
        <link href="/css/admin.css" rel="stylesheet">
    </head>
    <body x-data="{ 'loaded': true, 'darkMode': true, 'stickyMenu': false, 'sidebarToggle': false, 'scrollTop': false }"
          x-init="
          darkMode = JSON.parse(localStorage.getItem('darkMode'));
          $watch('darkMode', value => localStorage.setItem('darkMode', JSON.stringify(value)))"
          :class="{'dark text-bodydark bg-boxdark-2': darkMode === true}">
    <!-- ===== Preloader Start ===== -->
    <div x-show="loaded"
         x-init="window.addEventListener('DOMContentLoaded', () => {setTimeout(() => loaded = false, 500)})"
         class="fixed left-0 top-0 z-999999 flex h-screen w-screen items-center justify-center bg-white dark:bg-black">
        <div class="h-16 w-16 animate-spin rounded-full border-4 border-solid border-primary border-t-transparent"></div>
    </div>

    <!-- ===== Preloader End ===== -->

    <!-- ===== Page Wrapper Start ===== -->
    <div class="flex h-screen overflow-hidden">
        <!-- ===== Sidebar Start ===== -->
        <aside :class="sidebarToggle ? 'translate-x-0' : '-translate-x-full'"
               class="absolute left-0 top-0 z-9999 flex h-screen w-72.5 flex-col overflow-y-hidden bg-black duration-300 ease-linear dark:bg-boxdark lg:static lg:translate-x-0"
               @click.outside="sidebarToggle = false">
            <!-- SIDEBAR HEADER -->
            <div class="flex items-center justify-between gap-2 px-6 py-5.5 lg:py-6.5">
                <a href="/${locale}"><img src="/img/logo_rounded.png" alt="Logo"></a>

                <button class="block lg:hidden" @click.stop="sidebarToggle = !sidebarToggle">
                    <svg class="fill-current"
                         width="20"
                         height="18"
                         viewBox="0 0 20 18"
                         fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                        <path d="M19 8.175H2.98748L9.36248 1.6875C9.69998 1.35 9.69998 0.825 9.36248 0.4875C9.02498 0.15 8.49998 0.15 8.16248 0.4875L0.399976 8.3625C0.0624756 8.7 0.0624756 9.225 0.399976 9.5625L8.16248 17.4375C8.31248 17.5875 8.53748 17.7 8.76248 17.7C8.98748 17.7 9.17498 17.625 9.36248 17.475C9.69998 17.1375 9.69998 16.6125 9.36248 16.275L3.02498 9.8625H19C19.45 9.8625 19.825 9.4875 19.825 9.0375C19.825 8.55 19.45 8.175 19 8.175Z"
                              fill=""/>
                    </svg>
                </button>
            </div>
            <!-- SIDEBAR HEADER -->

            <div class="no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear">
                <!-- Sidebar Menu -->
                <nav class="mt-5 py-4 px-4 lg:mt-9 lg:px-6" x-data="{selected: $persist('Dashboard')}">
                    <!-- Menu Group -->
                    <div>
                        <h3 class="mb-4 ml-4 text-sm font-medium text-bodydark2">MENU</h3>

                        <ul class="mb-6 flex flex-col gap-1.5">
                            <#list menu as item>
                                <li>
                                    <a class="group relative flex items-center gap-2.5 rounded-sm py-2 px-4 font-medium text-bodydark1 duration-300 ease-in-out hover:bg-graydark dark:hover:bg-meta-4"
                                       href="${item.url}"
                                       @click="selected = (selected === '${item.title}' ? '':'${item.title}')"
                                       :class="{ 'bg-graydark dark:bg-meta-4': (selected === '${item.title}') && (page === '${item.id}') }">
                                        <svg class="fill-current"
                                             width="18"
                                             height="18"
                                             viewBox="0 0 18 18"
                                             fill="none"
                                             xmlns="http://www.w3.org/2000/svg">
                                            <path d="M15.7499 2.9812H14.2874V2.36245C14.2874 2.02495 14.0062 1.71558 13.6405 1.71558C13.2749 1.71558 12.9937 1.99683 12.9937 2.36245V2.9812H4.97803V2.36245C4.97803 2.02495 4.69678 1.71558 4.33115 1.71558C3.96553 1.71558 3.68428 1.99683 3.68428 2.36245V2.9812H2.2499C1.29365 2.9812 0.478027 3.7687 0.478027 4.75308V14.5406C0.478027 15.4968 1.26553 16.3125 2.2499 16.3125H15.7499C16.7062 16.3125 17.5218 15.525 17.5218 14.5406V4.72495C17.5218 3.7687 16.7062 2.9812 15.7499 2.9812ZM1.77178 8.21245H4.1624V10.9968H1.77178V8.21245ZM5.42803 8.21245H8.38115V10.9968H5.42803V8.21245ZM8.38115 12.2625V15.0187H5.42803V12.2625H8.38115ZM9.64678 12.2625H12.5999V15.0187H9.64678V12.2625ZM9.64678 10.9968V8.21245H12.5999V10.9968H9.64678ZM13.8374 8.21245H16.228V10.9968H13.8374V8.21245ZM2.2499 4.24683H3.7124V4.83745C3.7124 5.17495 3.99365 5.48433 4.35928 5.48433C4.7249 5.48433 5.00615 5.20308 5.00615 4.83745V4.24683H13.0499V4.83745C13.0499 5.17495 13.3312 5.48433 13.6968 5.48433C14.0624 5.48433 14.3437 5.20308 14.3437 4.83745V4.24683H15.7499C16.0312 4.24683 16.2562 4.47183 16.2562 4.75308V6.94683H1.77178V4.75308C1.77178 4.47183 1.96865 4.24683 2.2499 4.24683ZM1.77178 14.5125V12.2343H4.1624V14.9906H2.2499C1.96865 15.0187 1.77178 14.7937 1.77178 14.5125ZM15.7499 15.0187H13.8374V12.2625H16.228V14.5406C16.2562 14.7937 16.0312 15.0187 15.7499 15.0187Z"
                                                  fill=""/>
                                        </svg>

                                        ${item.title}

                                        <#if item.children?has_content>
                                            <svg class="absolute right-4 top-1/2 -translate-y-1/2 fill-current"
                                                 :class="{ 'rotate-180': (selected === '${item.title}') }"
                                                 width="20"
                                                 height="20"
                                                 viewBox="0 0 20 20"
                                                 fill="none"
                                                 xmlns="http://www.w3.org/2000/svg">
                                                <path fill-rule="evenodd"
                                                      clip-rule="evenodd"
                                                      d="M4.41107 6.9107C4.73651 6.58527 5.26414 6.58527 5.58958 6.9107L10.0003 11.3214L14.4111 6.91071C14.7365 6.58527 15.2641 6.58527 15.5896 6.91071C15.915 7.23614 15.915 7.76378 15.5896 8.08922L10.5896 13.0892C10.2641 13.4147 9.73651 13.4147 9.41107 13.0892L4.41107 8.08922C4.08563 7.76378 4.08563 7.23614 4.41107 6.9107Z"
                                                      fill=""/>
                                            </svg>
                                        </#if>
                                    </a>

                                    <#if item.children?has_content>
                                        <!-- Dropdown Menu Start -->
                                        <div class="translate transform overflow-hidden"
                                             :class="(selected === 'Dashboard') ? 'block' :'hidden'">
                                            <ul class="mt-4 mb-5.5 flex flex-col gap-2.5 pl-6">
                                                <#list item.children as child>
                                                    <li>
                                                        <a class="group relative flex items-center gap-2.5 rounded-md px-4 font-medium text-bodydark2 duration-300 ease-in-out hover:text-white"
                                                           href="${child.url}"
                                                           :class="page === '${child.id}' && '!text-white'">
                                                            ${child.title}
                                                        </a>
                                                    </li>
                                                </#list>
                                            </ul>
                                        </div>
                                        <!-- Dropdown Menu End -->
                                    </#if>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </nav>
                <!-- Sidebar Menu -->

                <!-- Promo Box -->
                <div class="mx-auto mb-10 w-full max-w-60 rounded-sm border border-strokedark bg-boxdark py-6 px-4 text-center shadow-default">

                    <h3 class="mb-1 font-semibold text-white"><@t key="admin_need_help" /></h3>
                    <p class="mb-3 text-xs"><@t key="admin_contact_us" /></p>

                    <a href="mailto:contact@suitebde.com"
                       class="flex items-center justify-center rounded-md bg-primary p-2 text-white hover:bg-opacity-95 mb-3">
                        hey@suitebde.com
                    </a>

                    <p class="mb-3 text-xs">On est aussi sur mobile !</p>
                    <a href="https://apps.apple.com/app/suite-bde/id6476325543"
                       class="flex items-center justify-center rounded-md bg-black p-2 text-white hover:bg-opacity-95 mb-3">
                        App Store
                    </a>
                    <a href="https://play.google.com/store/apps/details?id=me.nathanfallet.suitebde"
                       class="flex items-center justify-center rounded-md bg-black p-2 text-white hover:bg-opacity-95 mb-3">
                        Play Store
                    </a>

                    <p class="mb-3 text-xs">&copy; 2024 <a href="https://suitebde.com">Suite BDE</a></p>
                    <p class="mb-3 text-xs">
                        <@t key="admin_developed_with_love" args=[
                        "<i class=\"fa fa-heart text-danger\"></i>",
                        "<a href=\"https://kotlinlang.org\" class=\"text-dark font-weight-bold\">Kotlin</a>",
                        "<a href=\"https://nathanfallet.me\" class=\"text-dark font-weight-bold\">Nathan Fallet</a>",
                        "<a href=\"https://toastcie.dev\" class=\"text-dark font-weight-bold\">Toast.cie</a>"
                        ] />
                    </p>
                </div>
                <!-- Promo Box -->
            </div>
        </aside>

        <!-- ===== Sidebar End ===== -->

        <!-- ===== Content Area Start ===== -->
        <div class="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden">
            <!-- ===== Header Start ===== -->
            <header class="sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none">
                <div class="flex flex-grow items-center justify-between py-4 px-4 shadow-2 md:px-6 2xl:px-11">
                    <div class="flex items-center gap-2 sm:gap-4 lg:hidden">
                        <!-- Hamburger Toggle BTN -->
                        <button class="z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
                                @click.stop="sidebarToggle = !sidebarToggle">
                            <span class="relative block h-5.5 w-5.5 cursor-pointer">
                                <span class="du-block absolute right-0 h-full w-full">
                                    <span class="relative top-0 left-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-[0] duration-200 ease-in-out dark:bg-white"
                                          :class="{ '!w-full delay-300': !sidebarToggle }"></span>
                                    <span class="relative top-0 left-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-150 duration-200 ease-in-out dark:bg-white"
                                          :class="{ '!w-full delay-400': !sidebarToggle }"></span>
                                    <span class="relative top-0 left-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-200 duration-200 ease-in-out dark:bg-white"
                                          :class="{ '!w-full delay-500': !sidebarToggle }"></span>
                                </span>
                                <span class="du-block absolute right-0 h-full w-full rotate-45">
                                    <span class="absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-300 duration-200 ease-in-out dark:bg-white"
                                          :class="{ '!h-0 delay-[0]': !sidebarToggle }"></span>
                                    <span class="delay-400 absolute left-0 top-2.5 block h-0.5 w-full rounded-sm bg-black duration-200 ease-in-out dark:bg-white"
                                          :class="{ '!h-0 dealy-200': !sidebarToggle }"></span>
                                </span>
                            </span>
                        </button>
                        <!-- Hamburger Toggle BTN -->
                        <a class="block flex-shrink-0 lg:hidden" href="index.html">
                            <img src="src/images/logo/logo-icon.svg" alt="Logo"/>
                        </a>
                    </div>
                    <div class="sm:block"></div>

                    <div class="flex items-center gap-3 2xsm:gap-7">
                        <ul class="flex items-center gap-2 2xsm:gap-4">
                            <li>
                                <!-- Dark Mode Toggler -->
                                <label :class="darkMode ? 'bg-primary' : 'bg-stroke'"
                                       class="relative m-0 block h-7.5 w-14 rounded-full">
                                    <input type="checkbox"
                                           :value="darkMode"
                                           @change="darkMode = !darkMode"
                                           class="absolute top-0 z-50 m-0 h-full w-full cursor-pointer opacity-0"/>
                                    <span :class="darkMode && '!right-1 !translate-x-full'"
                                          class="absolute top-1/2 left-1 flex h-6 w-6 -translate-y-1/2 translate-x-0 items-center justify-center rounded-full bg-white shadow-switcher duration-75 ease-linear">
                                        <span class="dark:hidden">
                                            <svg width="16"
                                                 height="16"
                                                 viewBox="0 0 16 16"
                                                 fill="none"
                                                 xmlns="http://www.w3.org/2000/svg">
                                                <path d="M7.99992 12.6666C10.5772 12.6666 12.6666 10.5772 12.6666 7.99992C12.6666 5.42259 10.5772 3.33325 7.99992 3.33325C5.42259 3.33325 3.33325 5.42259 3.33325 7.99992C3.33325 10.5772 5.42259 12.6666 7.99992 12.6666Z"
                                                      fill="#969AA1"/>
                                                <path d="M8.00008 15.3067C7.63341 15.3067 7.33342 15.0334 7.33342 14.6667V14.6134C7.33342 14.2467 7.63341 13.9467 8.00008 13.9467C8.36675 13.9467 8.66675 14.2467 8.66675 14.6134C8.66675 14.9801 8.36675 15.3067 8.00008 15.3067ZM12.7601 13.4267C12.5867 13.4267 12.4201 13.3601 12.2867 13.2334L12.2001 13.1467C11.9401 12.8867 11.9401 12.4667 12.2001 12.2067C12.4601 11.9467 12.8801 11.9467 13.1401 12.2067L13.2267 12.2934C13.4867 12.5534 13.4867 12.9734 13.2267 13.2334C13.1001 13.3601 12.9334 13.4267 12.7601 13.4267ZM3.24008 13.4267C3.06675 13.4267 2.90008 13.3601 2.76675 13.2334C2.50675 12.9734 2.50675 12.5534 2.76675 12.2934L2.85342 12.2067C3.11342 11.9467 3.53341 11.9467 3.79341 12.2067C4.05341 12.4667 4.05341 12.8867 3.79341 13.1467L3.70675 13.2334C3.58008 13.3601 3.40675 13.4267 3.24008 13.4267ZM14.6667 8.66675H14.6134C14.2467 8.66675 13.9467 8.36675 13.9467 8.00008C13.9467 7.63341 14.2467 7.33342 14.6134 7.33342C14.9801 7.33342 15.3067 7.63341 15.3067 8.00008C15.3067 8.36675 15.0334 8.66675 14.6667 8.66675ZM1.38675 8.66675H1.33341C0.966748 8.66675 0.666748 8.36675 0.666748 8.00008C0.666748 7.63341 0.966748 7.33342 1.33341 7.33342C1.70008 7.33342 2.02675 7.63341 2.02675 8.00008C2.02675 8.36675 1.75341 8.66675 1.38675 8.66675ZM12.6734 3.99341C12.5001 3.99341 12.3334 3.92675 12.2001 3.80008C11.9401 3.54008 11.9401 3.12008 12.2001 2.86008L12.2867 2.77341C12.5467 2.51341 12.9667 2.51341 13.2267 2.77341C13.4867 3.03341 13.4867 3.45341 13.2267 3.71341L13.1401 3.80008C13.0134 3.92675 12.8467 3.99341 12.6734 3.99341ZM3.32675 3.99341C3.15341 3.99341 2.98675 3.92675 2.85342 3.80008L2.76675 3.70675C2.50675 3.44675 2.50675 3.02675 2.76675 2.76675C3.02675 2.50675 3.44675 2.50675 3.70675 2.76675L3.79341 2.85342C4.05341 3.11342 4.05341 3.53341 3.79341 3.79341C3.66675 3.92675 3.49341 3.99341 3.32675 3.99341ZM8.00008 2.02675C7.63341 2.02675 7.33342 1.75341 7.33342 1.38675V1.33341C7.33342 0.966748 7.63341 0.666748 8.00008 0.666748C8.36675 0.666748 8.66675 0.966748 8.66675 1.33341C8.66675 1.70008 8.36675 2.02675 8.00008 2.02675Z"
                                                      fill="#969AA1"/>
                                            </svg>
                                        </span>
                                        <span class="hidden dark:inline-block">
                                            <svg width="16"
                                                 height="16"
                                                 viewBox="0 0 16 16"
                                                 fill="none"
                                                 xmlns="http://www.w3.org/2000/svg">
                                                <path d="M14.3533 10.62C14.2466 10.44 13.9466 10.16 13.1999 10.2933C12.7866 10.3667 12.3666 10.4 11.9466 10.38C10.3933 10.3133 8.98659 9.6 8.00659 8.5C7.13993 7.53333 6.60659 6.27333 6.59993 4.91333C6.59993 4.15333 6.74659 3.42 7.04659 2.72666C7.33993 2.05333 7.13326 1.7 6.98659 1.55333C6.83326 1.4 6.47326 1.18666 5.76659 1.48C3.03993 2.62666 1.35326 5.36 1.55326 8.28666C1.75326 11.04 3.68659 13.3933 6.24659 14.28C6.85993 14.4933 7.50659 14.62 8.17326 14.6467C8.27993 14.6533 8.38659 14.66 8.49326 14.66C10.7266 14.66 12.8199 13.6067 14.1399 11.8133C14.5866 11.1933 14.4666 10.8 14.3533 10.62Z"
                                                      fill="#969AA1"/>
                                            </svg>
                                        </span>
                                    </span>
                                </label>
                                <!-- Dark Mode Toggler -->
                            </li>
                        </ul>

                        <!-- User Area -->
                        <div class="relative flex items-center gap-4">
                            <span class="hidden text-right lg:block">
                                <span class="block text-sm font-medium text-black dark:text-white">${user.firstName} ${user.lastName}</span>
                                <span class="block text-xs font-medium">${user.email}</span>
                            </span>

                            <span class="h-12 w-12 rounded-full">
                                <img src="src/images/user/user-01.png" alt="User"/>
                            </span>
                        </div>
                        <!-- User Area -->
                    </div>
                </div>
            </header>

            <!-- ===== Header End ===== -->

            <!-- ===== Main Content Start ===== -->
            <main>
                <div class="mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10">
                    <#nested>
                </div>
            </main>
            <!-- ===== Main Content End ===== -->
        </div>
        <!-- ===== Content Area End ===== -->
    </div>
    <!-- ===== Page Wrapper End ===== -->
    <script defer src="/js/admin.js"></script>
    </body>
    </html>
</#macro>
