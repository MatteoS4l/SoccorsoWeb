<!-- TOP NAVIGATION (CENTRALIZED) -->
<header class="dashboard-header">
    <a href="${(user.ruolo == 'ADMIN')?string(contextPath + '/dashboard', contextPath + '/missioni')}" class="logo">
        <i class="fas fa-hand-holding-medical"></i> SoccorsoWeb
    </a>
    <nav class="dashboard-nav">
        <#if user.ruolo == "ADMIN">
            <a href="${contextPath}/dashboard" class="${(activePage == 'dashboard')?string('active', '')}">Dashboard</a>
        </#if>
        <a href="${contextPath}/missioni" class="${(activePage == 'missioni')?string('active', '')}">Missioni</a>
        <#if user.ruolo == "ADMIN">
            <a href="${contextPath}/gestione-risorse" class="${(activePage == 'risorse')?string('active', '')}">Risorse</a>
        </#if>
    </nav>
    <div class="user-info" style="display: flex; align-items: center;">
        <a href="${contextPath}/profilo" style="text-decoration: none; color: inherit; transition: opacity 0.3s;" onmouseover="this.style.opacity='0.7'" onmouseout="this.style.opacity='1'">
            <div style="text-align: right; margin-right: 20px; line-height: 1.2;">
                <div style="font-size: 1.1rem; font-weight: 700; color: var(--text-dark); font-family: var(--font-titles);">
                    <i class="fas fa-user-circle" style="color: var(--primary-color); margin-right: 5px;"></i> ${user.nome?upper_case}
                </div>
                <div style="font-size: 0.75rem; font-weight: 600; color: #888; text-transform: uppercase; letter-spacing: 1px;">
                    ${user.ruolo}
                </div>
            </div>
        </a>
        <a href="${contextPath}/logout" class="btn btn-small btn-secondary" style="display: flex; align-items: center; gap: 8px;">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>
    </div>
</header>