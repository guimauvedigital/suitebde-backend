<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-4 fw-normal" id="auth_join_title"><@t key="auth_join_title" /></h1>

    <#if success??>
        <div id="alert-success" class="alert alert-success" role="alert"><@t key=success /></div>
    <#elseif error??>
        <div id="alert-error" class="alert alert-danger" role="alert"><@t key=error /></div>
    <#else>
        <#if code??>

        <#else>
            <div class="alert alert-info"><@t key="auth_join_info" /></div>
        </#if>
    </#if>

    <#if item??>
        <div class="form-floating">
            <input type="text" class="form-control" id="name" name="name" required>
            <label for="name"><@t key="auth_field_name" /></label>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="school" name="school" required>
                    <label for="school"><@t key="auth_field_school" /></label>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="city" name="city" required>
                    <label for="city"><@t key="auth_field_city" /></label>
                </div>
            </div>
        </div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" value="${item.email}" disabled>
            <label for="email"><@t key="auth_field_email" /></label>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="lastName" name="lastName" required>
                    <label for="lastName"><@t key="auth_field_last_name" /></label>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="firstName" name="firstName" required>
                    <label for="firstName"><@t key="auth_field_first_name" /></label>
                </div>
            </div>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="password" name="password" required>
            <label for="password"><@t key="auth_field_password" /></label>
        </div>
    <#else>
        <!--
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" required>
            <label for="email"><@t key="auth_field_email" /></label>
        </div>
        -->
        <div class="alert alert-info"><@t key="auth_join_unavailable" /></div>
    </#if>

    <button class="w-100 btn btn-lg btn-danger" type="submit"><@t key="auth_field_join" /></button>
</@template.form>
