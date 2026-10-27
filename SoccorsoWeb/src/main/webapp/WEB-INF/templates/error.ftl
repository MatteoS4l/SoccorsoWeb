<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore di Sistema - SoccorsoWeb</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #f5f5f5 0%, #e0e0e0 100%);
        }
        .error-container {
            max-width: 600px;
            width: 90%;
            text-align: center;
            background: white;
            padding: 50px;
            border-radius: 12px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.1);
            border-top: 5px solid var(--primary-color);
            animation: cardSlideUp 0.6s ease-out;
        }
        .error-icon {
            font-size: 80px;
            color: var(--primary-color);
            margin-bottom: 30px;
            animation: pulse 2s infinite ease-in-out;
        }
        .error-code {
            font-family: var(--font-titles);
            font-size: 1.5rem;
            color: var(--secondary-color);
            margin-bottom: 10px;
            text-transform: uppercase;
        }
        h1 {
            font-size: 2.5rem;
            margin-bottom: 20px;
            color: var(--text-dark);
        }
        .error-message {
            font-size: 1.2rem;
            color: #666;
            margin-bottom: 40px;
            line-height: 1.6;
            background: #fff5f5;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid var(--primary-color);
        }
        .actions {
            display: flex;
            gap: 20px;
            justify-content: center;
        }
        .btn-outline {
            background: transparent;
            border: 2px solid var(--primary-color);
            color: var(--primary-color);
        }
        .btn-outline:hover {
            background: var(--primary-color);
            color: white;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-exclamation-triangle"></i>
        </div>
        <div class="error-code">Segnalazione di Errore</div>
        <h1>${errorTitle! "Sistema in Anomalia"}</h1>
        
        <div class="error-message">
            ${errorMessage! "Si è verificato un errore inaspettato durante l'elaborazione della richiesta."}
        </div>

        <div class="actions">
            <a href="${contextPath}/" class="btn btn-primary">
                <i class="fas fa-home"></i> Torna alla Home
            </a>
            <a href="javascript:history.back()" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Indietro
            </a>
        </div>
    </div>
</body>
</html>