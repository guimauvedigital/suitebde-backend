<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-3 fw-normal"><@t key="auth_associations_title" /></h1>

    <div class="list-group">
        <#list item as association>
            <a class="list-group-item list-group-item-action" href="register?associationId=${association.id}">
                ${association.name}
            </a>
        </#list>
    </div>
</@template.form>
