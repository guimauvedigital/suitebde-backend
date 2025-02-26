<#import "template.ftl" as template>
<@template.form>
    <#if error??>
        <div id="alert-error" class="alert alert-danger" role="alert"><@t key=error /></div>
    </#if>

    <#if item??>
        <h1 class="h3 mb-3 fw-normal"><@t key="auth_authorize_title" args=[item.client.name] /></h1>

        <div class="mb-4">
            ${item.client.description}
        </div>

        <button class="w-100 btn btn-lg btn-danger" type="submit"><@t key="auth_field_authorize" /></button>
        <div class="mt-4">
            <@t key="auth_hint_authorize_connected_as" args=["${item.user.firstName} ${item.user.lastName}"] />
        </div>
        <div class="mt-3">
            <a href="logout?redirect=/auth/authorize?clientId=${item.client.id}" class="text-danger">
                <@t key="auth_hint_authorize_not_you" />
            </a>
        </div>
        <div class="mt-3 small">
            <@t key="auth_hint_authorize_close" />
        </div>
    </#if>
</@template.form>
