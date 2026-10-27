<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accedi - SoccorsoWeb</title>
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
            background: rgba(0, 0, 0, 0.7); /* Sfondo più scuro e meno rosso */
            z-index: -1;
        }

        .login-wrapper {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .login-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 25px 50px rgba(0,0,0,0.5), 0 0 30px rgba(198, 40, 40, 0.1);
            width: 100%;
            max-width: 420px;
            text-align: center;
            border: 1px solid rgba(255, 255, 255, 0.3);
            animation: cardSlideUp 0.5s ease-out;
        }

        .login-logo {
            font-size: 3rem;
            color: var(--primary-color);
            margin-bottom: 15px;
        }
    </style>
</head>
<body>

    <!-- NAVBAR MINIMALE -->
    <header class="landing-header" style="background: rgba(0,0,0,0.2); backdrop-filter: blur(5px);">
        <a href="${contextPath}/" class="logo">
             <i class="fas fa-hand-holding-medical"></i> SoccorsoWeb
        </a>
        <nav class="nav-links">
            <a href="${contextPath}/"><i class="fas fa-home"></i> Home</a>
            <a href="${contextPath}/richiesta"><i class="fas fa-first-aid"></i> Richiesta</a>
        </nav>
    </header>

    <div class="login-wrapper">
        <main class="login-card">
            <div class="login-logo">
                <i class="fas fa-user-shield"></i>
            </div>
            <h1 style="font-size: 2rem; margin-bottom: 10px;">Area Riservata</h1>
            <p style="color: #666; margin-bottom: 30px;">Accesso protetto per il personale.</p>

            <#if loginError??>
                <div class="alert alert-danger" style="margin-bottom: 25px; text-align: left; font-size: 0.9rem;">
                    <i class="fas fa-exclamation-triangle"></i> ${loginError}
                </div>
            </#if>

            <form action="login" method="post">
                <div class="form-group" style="text-align: left;">
                    <label>Indirizzo Email</label>
                    <div style="position: relative;">
                        <i class="fas fa-envelope" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                        <input type="email" name="email" required placeholder="Inserisci la tua email" 
                               style="padding-left: 45px; width: 100%;">
                    </div>
                </div>

                <div class="form-group" style="text-align: left;">
                    <label>Password</label>
                    <div style="position: relative;">
                        <i class="fas fa-lock" style="position: absolute; left: 15px; top: 14px; color: var(--primary-color); z-index: 5;"></i>
                        <input type="password" name="password" required placeholder="Inserisci la password" 
                               style="padding-left: 45px; width: 100%;">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-full" style="margin-top: 10px; padding: 15px;">
                    Accedi <i class="fas fa-sign-in-alt" style="margin-left: 10px;"></i>
                </button>
            </form>

            <div style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
                <p style="font-size: 0.85rem; color: #888;">
                    Monitoraggio accessi attivo.
                </p>
            </div>
        </main>
    </div>

    <footer style="padding: 20px; text-align: center; color: white; opacity: 0.7; font-size: 0.8rem;">
        &copy; 2026 SoccorsoWeb - Area Protetta
    </footer>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
