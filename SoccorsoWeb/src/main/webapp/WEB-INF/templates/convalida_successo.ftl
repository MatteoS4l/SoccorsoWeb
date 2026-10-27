<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Richiesta Convalidata - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body" style="display: flex; justify-content: center; align-items: center; min-height: 100vh; background-image: url('${contextPath}/images/hero-bg.jpg'); background-size: cover; background-position: center; position: relative;">
    
    <div style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); z-index: 1;"></div>

    <div class="card" style="max-width: 500px; text-align: center; padding: 50px; position: relative; z-index: 2; border-radius: 15px;">
        <i class="fas fa-check-circle" style="font-size: 5rem; color: var(--success); margin-bottom: 25px;"></i>
        <h1 style="color: var(--primary-dark); margin-bottom: 15px;">Richiesta Attivata!</h1>
        <p style="font-size: 1.1rem; color: #555; line-height: 1.6; margin-bottom: 30px;">
            La tua segnalazione è stata convalidata con successo ed è ora visibile alla nostra Centrale Operativa. 
            Gli operatori valuteranno la priorità e invieranno i soccorsi il prima possibile.
        </p>
        <a href="${contextPath}/" class="btn btn-primary btn-full">
            Torna alla Home
        </a>
    </div>

</body>
</html>
