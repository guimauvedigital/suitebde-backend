<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-4 fw-normal">Connexion</h1>

    <#if error??>
        <div class="alert alert-danger" role="alert">${error}</div>
    </#if>

    <div class="form-floating">
        <input type="text" class="form-control" id="email" name="email">
        <label for="username">Adresse email</label>
    </div>
    <div class="form-floating">
        <input type="password" class="form-control" id="password" name="password">
        <label for="password">Mot de passe</label>
    </div>

    <button class="w-100 btn btn-lg btn-danger" type="submit">Connexion</button>

    <p class="mt-4 mb-0">
        Pas encore de compte ? <a href="/auth/register" class="text-danger">Inscription</a>
    </p>
    <p class="mt-2 mb-0">
        Mot de passe oublié ? <a href="/auth/reset" class="text-danger">Réinitialiser</a>
    </p>
</@template.form>