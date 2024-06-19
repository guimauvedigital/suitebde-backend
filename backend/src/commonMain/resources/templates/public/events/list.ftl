<#import "../../template.ftl" as template>
<#import "../../components/events.ftl" as events>
<@template.page>
    <section class="py-20 lg:py-25 xl:py-30">
        <div class="mx-auto max-w-1280 px-4 md:px-8 xl:px-0 mt-12.5 lg:mt-17.5">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-7.5 xl:gap-10">
                <!-- Event Card -->
                <#list items as event>
                    <@events.card event />
                </#list>
            </div>
        </div>
    </section>
</@template.page>
