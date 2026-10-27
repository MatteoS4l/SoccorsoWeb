<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Convalida Richiesta - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f7f6;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .email-simulator {
            max-width: 600px;
            background: white;
            border: 1px solid #ddd;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            border-radius: 8px;
            overflow: hidden;
        }
        .email-header {
            background: #444;
            color: white;
            padding: 15px 25px;
            font-size: 0.9rem;
        }
        .email-body {
            padding: 40px;
            line-height: 1.6;
            color: #333;
        }
        .confirmation-box {
            background: #fff5f5;
            border: 2px dashed var(--primary-color);
            padding: 25px;
            text-align: center;
            margin-top: 30px;
            border-radius: 8px;
        }
    </style>
</head>
<body>

    <div class="email-simulator">
        <div class="email-header">
            <strong>Da:</strong> centrale-operativa@soccorsoweb.it<br>
            <strong>A:</strong> ${richiesta.contattoEmail}<br>
            <strong>Oggetto:</strong> CONFERMA: Richiesta di Soccorso #${richiesta.id}
        </div>
        
        <div class="email-body">
            <p>Gentile <strong>${richiesta.nomeRichiedente}</strong>,</p>
            <p>Abbiamo ricevuto la tua segnalazione di emergenza per il seguente luogo:</p>
            <p style="background: #f9f9f9; padding: 10px; border-left: 4px solid #ccc;">
                <em>${richiesta.indirizzo}</em>
            </p>
            
            <p>Per attivare la richiesta e permettere alla nostra centrale di gestire l'emergenza, <strong>è necessario confermare la tua identità</strong> cliccando sul pulsante qui sotto:</p>
            
            <div class="confirmation-box">
                <a href="${contextPath}/convalida?token=${richiesta.codiceConvalida}" class="btn btn-primary" style="padding: 15px 30px; font-size: 1.1rem;">
                    CONFERMA E ATTIVA RICHIESTA
                </a>
                <p style="font-size: 0.8rem; margin-top: 15px; color: #888;">
                    Se non hai inviato tu questa richiesta, ignora questa email.
                </p>
            </div>

            <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">
            <p style="font-size: 0.85rem; color: #999; text-align: center;">
                SoccorsoWeb - Centrale Operativa Digitale
            </p>
        </div>
    </div>

</body>
</html>
