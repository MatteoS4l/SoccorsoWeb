<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccorsoWeb - Servizio Emergenza</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <!-- Font Awesome per icone -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <!-- NAVBAR -->
    <header class="landing-header" id="mainNavbar">
        <a href="${contextPath}/" class="logo">
            <i class="fas fa-hand-holding-medical"></i> SoccorsoWeb
        </a>
        <nav class="nav-links">
            <a href="#home">Home</a>
            <a href="#servizi">Servizi</a>
            <a href="#chi-siamo">Chi Siamo</a>
            <a href="${contextPath}/richiesta">Richiesta</a>
            <a href="${contextPath}/login" class="btn btn-primary btn-small" style="margin-left: 20px; display: flex; align-items: center; gap: 8px;">
                <i class="fas fa-sign-in-alt"></i> Login
            </a>
        </nav>
    </header>

    <!-- HERO SECTION -->
    <section class="hero-section" id="home">
        <div class="hero-content">
            <h1>SoccorsoWeb</h1>
            <p>Efficienza, Rapidità, Sicurezza. Al servizio della comunità, sempre.</p>
            <div style="display: flex; gap: 20px; justify-content: center;">
                <a href="${contextPath}/richiesta" class="btn btn-primary">
                    <i class="fas fa-bullhorn"></i> Invia una Richiesta
                </a>
                <a href="${contextPath}/login" class="btn" style="background: rgba(255,255,255,0.1); color: white; border: 1px solid rgba(255,255,255,0.3);">
                    <i class="fas fa-user-shield"></i> Area Operatori
                </a>
            </div>
        </div>
    </section>

    <!-- SEZIONE SERVIZI (SPLIT FOTO) -->
    <section class="split-section" id="servizi">
        <div class="split-box" style="background-image: url('${contextPath}/images/request-bg.jpg');">
            <h2>Richiesta Soccorso</h2>
            <p>Segnala un'emergenza in tempo reale. Il nostro sistema ti metterà in contatto immediato con i soccorsi.</p>
            <a href="${contextPath}/richiesta" class="btn btn-primary">Invia Segnalazione</a>
        </div>
        <div class="split-box" style="background-image: url('${contextPath}/images/operator-bg.jpg');">
            <h2>Pannello Operativo</h2>
            <p>Riservato al personale autorizzato per la gestione delle missioni, dei mezzi e delle risorse sul campo.</p>
            <a href="${contextPath}/login" class="btn btn-primary">Accedi al Portale</a>
        </div>
    </section>

    <!-- SEZIONE CHI SIAMO -->
    <section class="about-parallax-reveal" id="chi-siamo">
        <div class="about-image" style="background-image: url('${contextPath}/images/about-bg.jpg');"></div>
        <div class="about-content">
            <h2>Il Nostro Impegno</h2>
            <p>SoccorsoWeb nasce con l'obiettivo di digitalizzare e ottimizzare la catena del soccorso. Crediamo che la tecnologia possa fare la differenza tra un intervento tempestivo e un ritardo fatale.</p>
            <p>Collaboriamo con enti locali, ospedali e forze dell'ordine per garantire una risposta coordinata e professionale ad ogni tipo di emergenza, 24 ore su 24, 7 giorni su 7.</p>
            <div style="margin-top: 30px; padding: 20px; background: rgba(255,255,255,0.1); border-left: 5px solid var(--primary-color); backdrop-filter: blur(5px);">
                <p><strong>Contatto diretto:</strong> <a href="mailto:matteo.salvatore@student.univaq.it" style="color: var(--primary-light); text-decoration: none; font-weight: bold;">matteo.salvatore@student.univaq.it</a></p>
            </div>
        </div>
    </section>

    <footer style="background: var(--text-dark); color: white; padding: 60px 0; text-align: center; border-top: 5px solid var(--primary-color);">
        <div class="container">
            <h2 style="margin-bottom: 20px;">SoccorsoWeb</h2>
            <p style="margin-bottom: 30px; opacity: 0.7;">Sistema Avanzato di Gestione Emergenze Territoriali</p>
            <p style="font-size: 0.9rem; opacity: 0.5;">&copy; 2026 SoccorsoWeb - Tutti i diritti riservati.</p>
        </div>
    </footer>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
