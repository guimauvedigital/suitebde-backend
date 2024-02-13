<#import "../../template.ftl" as template>
<@template.page>
    <div class="container-fluid py-4">
        <div class="card card-body">
            <h1 class="mb-4" id="webpages_title">${item.title}</h1>
            ${item.content}
        </div>
    </div>
</@template.page>
