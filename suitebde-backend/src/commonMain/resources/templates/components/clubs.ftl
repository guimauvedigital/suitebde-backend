<#macro card club>
    <div class="animate_top rounded-lg shadow-3 dark:bg-blacksection dark:shadow-none flex flex-wrap">
        <div class="group block relative w-[100px] z-1 overflow-hidden">
            <img class="aspect-square"
                 src="<#if club.logo??>${club.logo}<#else>/img/default_event_image.jpg</#if>"
                 alt="${club.name} Logo"/>
        </div>

        <div class="p-5">
            <span class="font-medium text-2xl ease-in-out duration-300 text-black dark:text-white hover:text-primary xl:w-[90%]">
                <a href="clubs/${club.id}">${club.name}</a>
            </span>
            <div class="flex flex-wrap items-center gap-2 xl:gap-5">
                <p>
                    <#if club.usersCount != 1>
                        <@t key="clubs_members" args=[club.usersCount] />
                    <#else>
                        <@t key="clubs_member" args=[club.usersCount] />
                    </#if>
                </p>
            </div>
        </div>
    </div>
</#macro>
