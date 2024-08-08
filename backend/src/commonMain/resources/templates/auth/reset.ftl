<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-4 fw-normal" id="auth_reset_title"><@t key="auth_reset_title" /></h1>

    <#if success??>
        <div id="alert-success" class="alert alert-success" role="alert"><@t key=success /></div>
    <#elseif error??>
        <div id="alert-error" class="alert alert-danger" role="alert"><@t key=error /></div>
    </#if>

    <#if item??>
        <div class="form-floating">
            <input type="password" class="form-control" id="password" name="password" required>
            <label for="password"><@t key="auth_field_new_password" /></label>
        </div>
    <#else>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" required>
            <label for="email"><@t key="auth_field_email" /></label>
        </div>
    </#if>

    <button class="w-100 btn btn-lg btn-danger" type="submit"><@t key="auth_field_reset" /></button>

    <p class="mt-4 mb-0">
        <@t key="auth_hint_already_have_account" /> <a href="/auth/login"
                                                       class="text-danger"><@t key="auth_login_title" /></a>
    </p>
</@template.form>
