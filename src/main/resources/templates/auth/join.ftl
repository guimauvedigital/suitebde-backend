<#import "template.ftl" as template>
<@template.form>
    <h1 class="h3 mb-4 fw-normal">Créer un BDE</h1>

    <#if success??>
        <div id="alert-success" class="alert alert-success" role="alert">${success}</div>
    <#elseif error??>
        <div id="alert-error" class="alert alert-danger" role="alert">${error}</div>
    <#else>
        <#if code??>

        <#else>
            <div class="alert alert-info">
                Entrez votre adresse email pour créer un BDE. Un lien de confirmation vous sera envoyé, et vous pourrez
                ensuite créer votre BDE. Si vous avez déjà créé un BDE, vous pouvez <a href="/auth/login">vous
                    connecter</a>.
            </div>
        </#if>
    </#if>

    <#if code??>
        <div class="form-floating">
            <input type="text" class="form-control" id="name" name="name" required>
            <label for="name">Nom du BDE</label>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="school" name="school" required>
                    <label for="school">Nom de l'école</label>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="city" name="city" required>
                    <label for="city">Ville</label>
                </div>
            </div>
        </div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" value="${code.email}" disabled>
            <label for="email">Adresse email</label>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="last_name" name="last_name" required>
                    <label for="last_name">Nom</label>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-floating">
                    <input type="text" class="form-control" id="first_name" name="first_name" required>
                    <label for="first_name">Prénom</label>
                </div>
            </div>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="password" name="password" required>
            <label for="password">Mot de passe</label>
        </div>
    <#else>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" required>
            <label for="email">Adresse email</label>
        </div>
    </#if>

    <button class="w-100 btn btn-lg btn-danger" type="submit">Rejoindre</button>
</@template.form>