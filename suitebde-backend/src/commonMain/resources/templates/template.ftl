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
        <link href="/css/public.css" rel="stylesheet">
    </head>
    <body x-data="{ 'darkMode': true, 'stickyMenu': false, 'navigationOpen': false, 'scrollTop': false }"
          x-init="
         darkMode = JSON.parse(localStorage.getItem('darkMode'));
         $watch('darkMode', value => localStorage.setItem('darkMode', JSON.stringify(value)))"
          :class="{'dark bg-black': darkMode === true}">
    <!-- ===== Header Start ===== -->
    <header class="fixed left-0 top-0 w-full z-99999 py-7"
            :class="{ 'bg-white dark:bg-black shadow !py-4 transition duration-100' : stickyMenu }"
            @scroll.window="stickyMenu = (window.pageYOffset > 20) ? true : false">
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0 lg:flex items-center justify-between relative">
            <div class="w-full lg:w-1/4 flex items-center justify-between">
                <a href="/${locale}"><img src="/img/logo_rounded.png" alt="Logo"></a>

                <!-- Hamburger Toggle BTN -->
                <button class="lg:hidden block" @click="navigationOpen = !navigationOpen">
                    <span class="block relative cursor-pointer w-5.5 h-5.5">
                        <span class="du-block absolute right-0 w-full h-full">
                            <span class="block relative top-0 left-0 bg-black dark:bg-white rounded-sm w-0 h-0.5 my-1 ease-in-out duration-200 delay-[0]"
                                  :class="{ '!w-full delay-300': !navigationOpen }"></span>
                            <span class="block relative top-0 left-0 bg-black dark:bg-white rounded-sm w-0 h-0.5 my-1 ease-in-out duration-200 delay-150"
                                  :class="{ '!w-full delay-400': !navigationOpen }"></span>
                            <span class="block relative top-0 left-0 bg-black dark:bg-white rounded-sm w-0 h-0.5 my-1 ease-in-out duration-200 delay-200"
                                  :class="{ '!w-full delay-500': !navigationOpen }"></span>
                        </span>
                        <span class="du-block absolute right-0 w-full h-full rotate-45">
                            <span class="block bg-black dark:bg-white rounded-sm ease-in-out duration-200 delay-300 absolute left-2.5 top-0 w-0.5 h-full"
                                  :class="{ 'h-0 delay-[0]': !navigationOpen }"></span>
                            <span class="block bg-black dark:bg-white rounded-sm ease-in-out duration-200 delay-400 absolute left-0 top-2.5 w-full h-0.5"
                                  :class="{ 'h-0 dealy-200': !navigationOpen }"></span>
                        </span>
                    </span>
                </button>
                <!-- Hamburger Toggle BTN -->
            </div>

            <div class="w-full lg:w-3/4 h-0 lg:h-auto invisible lg:visible lg:flex items-center justify-between"
                 :class="{ '!visible bg-white dark:bg-blacksection shadow-solid-5 !h-auto max-h-[400px] overflow-y-scroll rounded-md mt-4 p-7.5': navigationOpen }">
                <nav>
                    <ul class="flex lg:items-center flex-col lg:flex-row gap-5 lg:gap-10">
                        <#list menu as item>
                            <li class="group relative" x-data="{ dropdown: false }">
                                <a href="${item.url}" class="hover:text-primary">
                                    ${item.title}
                                    <#if item.children?has_content>
                                        <svg :class="{ 'fill-primary': dropdown }"
                                             class="fill-body group-hover:fill-primary w-3 h-3 cursor-pointer"
                                             xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                            <path d="M233.4 406.6c12.5 12.5 32.8 12.5 45.3 0l192-192c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L256 338.7 86.6 169.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3l192 192z"/>
                                        </svg>
                                    </#if>
                                </a>

                                <#if item.children?has_content>
                                    <!-- Dropdown Start -->
                                    <ul class="dropdown" :class="{ 'flex': dropdown }">
                                        <#list item.children as child>
                                            <li>
                                                <a href="${child.url}" class="hover:text-primary">${child.title}</a>
                                            </li>
                                        </#list>
                                    </ul>
                                    <!-- Dropdown End -->
                                </#if>
                            </li>
                        </#list>
                    </ul>
                </nav>

                <div class="flex items-center gap-6 mt-7 lg:mt-0">
                    <div class="mr-1.5 absolute lg:static top-2.5 right-17"
                         :class="navigationOpen ? '!-visible' : '!visible'">
                        <label class="block m-0 relative">
                            <input type="checkbox" :value="darkMode" @change="darkMode = !darkMode"
                                   class="cursor-pointer w-full h-full opacity-0 absolute top-0 z-50 m-0"/>
                            <!-- Icon Sun -->
                            <svg class="fill-body dark:hidden" width="25" height="25" viewBox="0 0 25 25" fill="none"
                                 xmlns="http://www.w3.org/2000/svg">
                                <path d="M12.0908 18.6363C10.3549 18.6363 8.69 17.9467 7.46249 16.7192C6.23497 15.4916 5.54537 13.8268 5.54537 12.0908C5.54537 10.3549 6.23497 8.69 7.46249 7.46249C8.69 6.23497 10.3549 5.54537 12.0908 5.54537C13.8268 5.54537 15.4916 6.23497 16.7192 7.46249C17.9467 8.69 18.6363 10.3549 18.6363 12.0908C18.6363 13.8268 17.9467 15.4916 16.7192 16.7192C15.4916 17.9467 13.8268 18.6363 12.0908 18.6363ZM12.0908 16.4545C13.2481 16.4545 14.358 15.9947 15.1764 15.1764C15.9947 14.358 16.4545 13.2481 16.4545 12.0908C16.4545 10.9335 15.9947 9.8236 15.1764 9.00526C14.358 8.18692 13.2481 7.72718 12.0908 7.72718C10.9335 7.72718 9.8236 8.18692 9.00526 9.00526C8.18692 9.8236 7.72718 10.9335 7.72718 12.0908C7.72718 13.2481 8.18692 14.358 9.00526 15.1764C9.8236 15.9947 10.9335 16.4545 12.0908 16.4545ZM10.9999 0.0908203H13.1817V3.36355H10.9999V0.0908203ZM10.9999 20.8181H13.1817V24.0908H10.9999V20.8181ZM2.83446 4.377L4.377 2.83446L6.69082 5.14828L5.14828 6.69082L2.83446 4.37809V4.377ZM17.4908 19.0334L19.0334 17.4908L21.3472 19.8046L19.8046 21.3472L17.4908 19.0334ZM19.8046 2.83337L21.3472 4.377L19.0334 6.69082L17.4908 5.14828L19.8046 2.83446V2.83337ZM5.14828 17.4908L6.69082 19.0334L4.377 21.3472L2.83446 19.8046L5.14828 17.4908ZM24.0908 10.9999V13.1817H20.8181V10.9999H24.0908ZM3.36355 10.9999V13.1817H0.0908203V10.9999H3.36355Z"
                                      fill=""/>
                            </svg>
                            <!-- Icon Sun -->
                            <svg class="hidden dark:block" width="21" height="21" viewBox="0 0 21 21" fill="none"
                                 xmlns="http://www.w3.org/2000/svg">
                                <g clip-path="url(#clip0_512_11103)">
                                    <path d="M8.83697 5.88205C8.8368 7.05058 9.18468 8.19267 9.83625 9.16268C10.4878 10.1327 11.4135 10.8866 12.4953 11.3284C13.5772 11.7701 14.766 11.8796 15.9103 11.6429C17.0546 11.4062 18.1025 10.8341 18.9203 9.99941V10.0834C18.9203 14.7243 15.1584 18.4862 10.5175 18.4862C5.87667 18.4862 2.11475 14.7243 2.11475 10.0834C2.11475 5.44259 5.87667 1.68066 10.5175 1.68066H10.6016C10.042 2.22779 9.59754 2.88139 9.29448 3.60295C8.99143 4.32451 8.83587 5.09943 8.83697 5.88205ZM3.7953 10.0834C3.79469 11.5833 4.29571 13.0403 5.21864 14.2226C6.14157 15.4049 7.4334 16.2446 8.88857 16.608C10.3437 16.9715 11.8787 16.8379 13.2491 16.2284C14.6196 15.6189 15.7469 14.5686 16.4516 13.2446C15.1974 13.54 13.8885 13.5102 12.6492 13.1578C11.4098 12.8054 10.281 12.1422 9.36988 11.2311C8.45877 10.32 7.79557 9.19119 7.44318 7.95181C7.0908 6.71243 7.06093 5.40357 7.3564 4.1494C6.28049 4.72259 5.38073 5.57759 4.75343 6.62288C4.12614 7.66817 3.79495 8.86438 3.7953 10.0834Z"
                                          fill="white"/>
                                </g>
                                <defs>
                                    <clipPath id="clip0_512_11103">
                                        <rect width="20.1667" height="20.1667" fill="white"
                                              transform="translate(0.434204)"/>
                                    </clipPath>
                                </defs>
                            </svg>
                        </label>
                    </div>

                    <#if user??>
                        <span class="text-right">
                            <span class="block text-sm font-medium text-black dark:text-white">${user.firstName} ${user.lastName}</span>
                            <span class="block text-xs font-medium">${user.email}</span>
                        </span>

                        <span class="h-12 w-12 rounded-full">
                            <img src="src/images/user/user-01.png" alt="User"/>
                        </span>
                    <#else>
                        <a href="/${locale}/auth/login"
                           :class="{ '!text-white hover:text-white' : page === 'home', '!text-body' : page === 'home' && stickyMenu }"
                           class="font-medium text-body hover:text-primary"><@t key="auth_field_login" /></a>
                        <a href="/${locale}/auth/register"
                           :class="{ 'bg-white/[0.15]' : page === 'home', '!bg-primary' : page === 'home' && stickyMenu }"
                           class="text-white bg-primary text-regular rounded-full flex items-center justify-center hover:shadow-1 py-3 px-7.5"><@t key="auth_field_join" /></a>
                    </#if>
                </div>
            </div>
        </div>
    </header>
    <!-- ===== Header End ===== -->

    <main>
        <#nested>
    </main>

    <!-- ===== Footer Start ===== -->
    <footer>
        <div class="mx-auto max-w-1390 px-4 md:px-8 2xl:px-0">
            <!-- Footer Top -->
            <div class="py-20 lg:py-25">
                <div class="flex flex-wrap lg:justify-between gap-8 lg:gap-0">
                    <div class="animate_top w-1/2 lg:w-1/4">
                        <a href="/${locale}"><img src="/img/logo_rounded.png" alt="Logo"></a>

                        <p class="mt-5 mb-10"><@t key="admin_developed_with_love" args=[
                            "<i class=\"fa fa-heart text-danger\"></i>",
                            "<a href=\"https://kotlinlang.org\" class=\"text-dark font-weight-bold\">Kotlin</a>",
                            "<a href=\"https://nathanfallet.me\" class=\"text-dark font-weight-bold\">Nathan Fallet</a>",
                            "<a href=\"https://toastcie.dev\" class=\"text-dark font-weight-bold\">Toast.cie</a>"
                            ] /></p>
                    </div>

                    <div class="w-full lg:w-[70%] flex flex-col md:flex-row md:justify-between gap-8 md:gap-0">
                        <div class="animate_top">
                            <h4 class="text-black dark:text-white text-2xl mb-9">Quick Links</h4>
                            <ul>
                                <li><a href="/" class="inline-block hover:text-primary mb-3">Home</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Footer Top -->

            <!-- Footer Bottom -->
            <div class="border-t border-strokedark dark:border-stroke flex flex-wrap flex-col lg:flex-row items-center justify-center lg:justify-between gap-5 lg:gap-0 py-7.5">
                <div class="animate_top">
                    <ul class="flex items-center gap-8">
                        <li><a href="#" class="hover:text-primary">Privacy Policy</a></li>
                    </ul>
                </div>

                <div class="animate_top">
                    <p>&copy; 2024 Suite BDE</p>
                </div>
            </div>
            <!-- Footer Bottom -->
        </div>
    </footer>

    <!-- ===== Footer End ===== -->

    <!-- ====== Back To Top Start ===== -->
    <button class="hidden items-center justify-center w-10 h-10 rounded-[4px] shadow-solid-5 bg-primary hover:bg-primaryho fixed bottom-8 right-8 z-999"
            @click="window.scrollTo({top: 0, behavior: 'smooth'})"
            @scroll.window="scrollTop = (window.pageYOffset > 50) ? true : false"
            :class="{ '!flex' : scrollTop }">
        <svg class="fill-white w-5 h-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
            <path d="M233.4 105.4c12.5-12.5 32.8-12.5 45.3 0l192 192c12.5 12.5 12.5 32.8 0 45.3s-32.8 12.5-45.3 0L256 173.3 86.6 342.6c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3l192-192z"/>
        </svg>
    </button>
    <!-- ====== Back To Top End ===== -->
    <script defer src="/js/public.js"></script>
    </body>
    </html>
</#macro>
