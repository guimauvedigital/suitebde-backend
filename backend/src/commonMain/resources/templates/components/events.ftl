<#macro card event>
    <div class="animate_top rounded-lg shadow-3 dark:bg-blacksection dark:shadow-none">
        <div class="group block relative z-1 overflow-hidden">
            <img class="w-full aspect-video object-cover"
                 src="<#if event.image??>${event.image}<#else>/img/default_event_image.jpg</#if>"
                 alt="${event.name} Image"/>
        </div>

        <div class="p-7.5">
            <span class="font-medium text-2xl ease-in-out duration-300 text-black dark:text-white hover:text-primary xl:w-[90%]">
                <a href="events/${event.id}">${event.name}</a>
            </span>
            <div class="flex flex-wrap items-center gap-2 xl:gap-5">
                <p>Date</p>
                <p>Time</p>
            </div>
        </div>
    </div>
</#macro>
