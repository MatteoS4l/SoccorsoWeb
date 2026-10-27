<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <#include "includes/header.ftl">

    <div class="container">
        <div style="margin-bottom: 30px;">
            <h1 style="color: var(--primary-dark); font-size: 2.5rem;"><i class="fas fa-user-cog"></i> Mio Profilo</h1>
            <p style="color: #666;">Gestisci le tue informazioni personali, le tue competenze e le credenziali di accesso.</p>
        </div>

        <#if successMessage??>
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${successMessage}
            </div>
        </#if>

        <#if errorMessage??>
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
            </div>
        </#if>

        <form action="${contextPath}/profilo" method="POST">
            <div class="grid-2">
                <!-- Colonna Sinistra: Dati Anagrafici e Password -->
                <div class="card">
                    <h2 style="margin-bottom: 25px; border-bottom: 2px solid var(--primary-color); padding-bottom: 10px;">
                        <i class="fas fa-id-card"></i> Informazioni Personali
                    </h2>
                    
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" value="${profileUser.nome}" required>
                    </div>
                    <div class="form-group">
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" value="${profileUser.cognome}" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${profileUser.email}" required>
                    </div>

                    <div style="margin-top: 30px; background: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px solid #eee;">
                        <h3 style="font-size: 1rem; margin-bottom: 15px; color: var(--secondary-color);">
                            <i class="fas fa-key"></i> Sicurezza Account
                        </h3>
                        <p style="font-size: 0.85rem; color: #777; margin-bottom: 15px;">Lascia i campi password vuoti se non vuoi cambiarla.</p>
                        
                        <div class="form-group">
                            <label for="password">Nuova Password</label>
                            <input type="password" id="password" name="password" placeholder="Min. 8 caratteri">
                        </div>
                        <div class="form-group">
                            <label for="password_confirm">Conferma Nuova Password</label>
                            <input type="password" id="password_confirm" name="password_confirm">
                        </div>
                    </div>
                </div>

                <!-- Colonna Destra: Competenze e Salvataggio -->
                <div class="card">
                    <h2 style="margin-bottom: 25px; border-bottom: 2px solid var(--primary-dark); padding-bottom: 10px;">
                        <i class="fas fa-award"></i> Competenze e Abilitazioni
                    </h2>

                    <div class="form-group">
                        <label><i class="fas fa-id-badge"></i> Patenti di Guida</label>
                        <div class="checkbox-list">
                            <#list tutteLePatenti as patente>
                                <#assign posseduta = false>
                                <#list profileUser.patenti as p_user>
                                    <#if p_user.id == patente.id><#assign posseduta = true></#if>
                                </#list>
                                <div class="checkbox-item">
                                    <label for="p_${patente.id}">${patente.tipologia}</label>
                                    <input type="checkbox" id="p_${patente.id}" name="patentiIds" value="${patente.id}" ${(posseduta)?string('checked', '')}>
                                </div>
                            </#list>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 25px;">
                        <label><i class="fas fa-bolt"></i> Specializzazioni e Skill</label>
                        <div class="checkbox-list">
                            <#list tutteLeSkill as skill>
                                <#assign posseduta = false>
                                <#list profileUser.skills as s_user>
                                    <#if s_user.id == skill.id><#assign posseduta = true></#if>
                                </#list>
                                <div class="checkbox-item">
                                    <label for="s_${skill.id}">${skill.descrizione}</label>
                                    <input type="checkbox" id="s_${skill.id}" name="skillsIds" value="${skill.id}" ${(posseduta)?string('checked', '')}>
                                </div>
                            </#list>
                        </div>
                    </div>

                    <div style="margin-top: 40px;">
                        <button type="submit" class="btn btn-primary btn-full btn-large" style="padding: 15px;">
                            <i class="fas fa-save"></i> AGGIORNA TUTTI I DATI
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>