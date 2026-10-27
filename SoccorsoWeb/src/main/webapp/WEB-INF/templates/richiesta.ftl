<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invia Richiesta di Soccorso - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background-image: url('${contextPath}/images/hero-bg.jpg');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        
        body::before {
            content: '';
            position: fixed;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.6);
            z-index: -1;
        }

        .richiesta-container {
            max-width: 800px;
            margin: 40px auto;
            width: 90%;
            z-index: 1;
        }

        .glass-card {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
        }
    </style>
</head>
<body>

    <!-- NAVBAR MINIMALE -->
    <header class="landing-header" style="background: rgba(0,0,0,0.3); backdrop-filter: blur(5px);">
        <a href="${contextPath}/" class="logo">
            <i class="fas fa-hand-holding-medical"></i> SoccorsoWeb
        </a>
        <nav class="nav-links">
            <a href="${contextPath}/"><i class="fas fa-home"></i> Home</a>
            <a href="${contextPath}/login" class="btn btn-primary btn-small" style="margin-left: 20px; display: flex; align-items: center; gap: 8px;">
                <i class="fas fa-sign-in-alt"></i> Login
            </a>
        </nav>
    </header>

    <div class="richiesta-container" style="margin-top: 100px;">
        <main class="glass-card fade-in">
            <div style="text-align: center; margin-bottom: 30px;">
                <h1 style="color: var(--primary-color); font-size: 2.2rem; margin-bottom: 10px;">
                    <i class="fas fa-first-aid"></i> Richiesta di Soccorso
                </h1>
                <p style="color: #666; font-size: 1.1rem;">Compila il modulo per inviare una segnalazione immediata alla nostra centrale.</p>
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

            <form action="${contextPath}/richiesta" method="POST" enctype="multipart/form-data">
                <div class="grid-2">
                    <div class="form-group">
                        <label for="nome_richiedente">Nome e Cognome</label>
                        <div style="position: relative;">
                            <i class="fas fa-user" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                            <input type="text" id="nome_richiedente" name="nome_richiedente" required 
                                   placeholder="Nome Cognome" style="padding-left: 45px; width: 100%;">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email">Indirizzo Email (per la convalida)</label>
                        <div style="position: relative;">
                            <i class="fas fa-envelope" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                            <input type="email" id="email" name="email" required 
                                   placeholder="tua@email.com" style="padding-left: 45px; width: 100%;">
                        </div>
                    </div>
                </div>

                <div class="grid-2">
                    <div class="form-group">
                        <label for="indirizzo">Luogo dell'emergenza</label>
                        <div style="position: relative;">
                            <i class="fas fa-map-marker-alt" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                            <input type="text" id="indirizzo" name="indirizzo" required 
                                   placeholder="Inserisci via e città" style="padding-left: 45px; width: 100%;">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="foto">Foto opzionale (max 10MB)</label>
                        <div style="position: relative;">
                            <i class="fas fa-camera" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                            <input type="file" id="foto" name="foto" accept="image/*" style="padding-left: 45px; width: 100%; padding-top: 10px;">
                        </div>
                    </div>
                </div>

                <div class="grid-2">
                    <div class="form-group">
                        <label for="tipology">Tipo di emergenza</label>
                        <select id="tipologia" name="tipologia" required>
                                <option value="">-- Seleziona --</option>
                                <#if tipologie??>
                                    <#list tipologie as t>
                                        <option value="${t}">${t?replace("_"," ")}</option>
                                    </#list>

                                </#if>
                            </select>
                    </div>

                    <div class="form-group">
                        <label for="priorita">Priorità percepita</label>
                        <select id="priorita" name="priorita" required>
                            <option value="">-- Seleziona --</option>
                            <#if priorita??>
                                <#list priorita as p>
                                    <option value="${p}">${p?replace("_"," ")}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="descrizione">Descrizione dettagliata dell'evento</label>
                    <textarea id="descrizione" name="descrizione" rows="4" required 
                              placeholder="Fornisci più dettagli possibili per aiutarci a intervenire meglio..."></textarea>
                </div>

                <!-- SEZIONE CAPTCHA -->
                <div class="form-group" style="background: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px solid #ddd; margin-top: 10px;">
                    <label style="color: var(--primary-dark);"><i class="fas fa-shield-alt"></i> Controllo di Sicurezza</label>
                    <p style="font-size: 0.9rem; margin-bottom: 15px; color: #666;">Per favore, risolvi questa semplice operazione per dimostrare che non sei un robot:</p>
                    <div style="display: flex; align-items: center; gap: 15px;">
                        <span style="font-size: 1.2rem; font-weight: bold; background: #fff; padding: 10px 20px; border-radius: 4px; border: 1px solid #ccc;">
                            ${n1} + ${n2} = ?
                        </span>
                        <input type="number" name="captcha_result" required placeholder="Risultato" style="width: 120px; text-align: center;">
                    </div>
                </div>

                <div style="margin-top: 30px; text-align: center;">
                    <button type="submit" class="btn btn-primary btn-large btn-full" style="font-size: 1.2rem; padding: 15px;">
                        <i class="fas fa-paper-plane"></i> &nbsp; INVIA SEGNALAZIONE
                    </button>
                </div>
            </form>
        </main>
    </div>

    <footer style="margin-top: auto; padding: 30px; text-align: center; color: rgba(255,255,255,0.7);">
        <p>&copy; 2026 SoccorsoWeb - Sistema di Emergenza Pubblica</p>
    </footer>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
