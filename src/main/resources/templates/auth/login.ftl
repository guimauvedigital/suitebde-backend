<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-4 fw-normal"><@t key="auth_login_title" /></h1>

    <#if error??>
        <div id="alert-error" class="alert alert-danger" role="alert">${error}</div>
    </#if>

    <div class="form-floating">
        <input type="email" class="form-control" id="email" name="email" required>
        <label for="email"><@t key="auth_field_email" /></label>
    </div>
    <div class="form-floating">
        <input type="password" class="form-control" id="password" name="password" required>
        <label for="password"><@t key="auth_field_password" /></label>
    </div>

    <button class="w-100 btn btn-lg btn-danger" type="submit"><@t key="auth_field_login" /></button>

    <p class="mt-4 mb-0">
        <@t key="auth_hint_no_account" /> <a href="/auth/register"
                                             class="text-danger"><@t key="auth_register_title" /></a>
    </p>
    <p class="mt-2 mb-0">
        <@t key="auth_hint_password_forgotten" /> <a href="/auth/reset"
                                                     class="text-danger"><@t key="auth_reset_title" /></a>
    </p>
</@template.form>