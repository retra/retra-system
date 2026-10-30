# HBR-5: Přihlášení přes Keycloak (OIDC) v app-retra

## 1. Cíl a omezení

Přidat do aplikace app-retra možnost přihlášení přes **Keycloak pomocí OIDC (Authorization Code flow + PKCE)**, bez zásahu do současného legacy stacku (Spring 2.5 XML konfigurace, `MultiActionController`, Hibernate 3, Servlet 2.4 na Tomcatu, Java 11). Klasické přihlášení formulářem (heslo v DB / LDAP) zůstává zachováno jako fallback.

**Mapování uživatelů (jednoduché, podle zadání):** `preferred_username` z Keycloaku odpovídá hodnotě sloupce `sis11login.sis11ldaplogin` (existující pole `Login.ldapLogin`). Žádná změna DB schématu není potřeba.

**Rozhodnutí a omezení (odsouhlaseno):**

- Knihovna `lib-oauth` a její závislosti (`cz.hb.lib` / `cz.cmhb.lib` mimo již používané `lib-jira`, Nimbus SDK) se v app-retra **nepoužijí**. Místo toho vznikne **zjednodušená jednoúčelová náhrada přímo v projektu** (viz kapitola 3) — pouze JDK 11 (`java.net.http.HttpClient`) + Jackson 1.9.2, který je už v aplikaci přes `jersey-json`. **Žádná nová závislost v `pom.xml`.**
- In-memory úložiště login transakcí je v pořádku — aplikace běží pouze na jednom node.
- **Client secret přímo v `retra.properties`** je dostatečné a v souladu se současným řešením (soubor je per-environment, mimo gitu).
- **Keycloak slouží pouze pro ověření identity.** Role a oprávnění zůstávají řízena v Retře (stejně jako u LDAP) — z tokenu se bere pouze identita uživatele.

---

## 2. Jak dnes probíhá přihlašování (aktuální stav)

### 2.1 Tok požadavku

1. `web.xml` mapuje všechny `*.do` a `*.screen` přes filtry; klíčový je **`SecurityWebFilter`** (`cz.softinel.sis.security.web.SecurityWebFilter`, mapováno na `*.do`, `*.screen`):
   - zavolá `securityLogic.initContext(req)` — do HTTP session uloží `SecurityContext` pod klíčem `"securityContext"` a do thread-local úložiště (`WebSecurityLogicImpl.storage`);
   - pokud `isUserLoggedIn()` → pokračuje; jinak pokud jde o *unsecured page* (`WebSecurityLogicImpl.initUnsecuredPages()`: `index.jsp`, `Login.do`, `LoginAction.do`, `Logout.do`) → pokračuje;
   - jinak zkusí přihlášení přes permanentní cookie (`RETRA_PERMANENT_COOKIE_NAME` → `securityLogic.login(permanentPassword)`);
   - jinak uloží `originalUrl` do requestu a forwardne na `Login.do?fkprm=true`.
2. `Login.do` → bean v `action-servlet.xml` → `SystemController.showLogin()` → view `Login.screen` (`WEB-INF/jsp/system/Login.jsp`). Formulář postuje na `LoginAction.do?fkprm=true&originalUrl=...`.
3. `LoginAction.do` → `SystemController.doLogin()` → `securityLogic.login(loginName, password)`:
   - `SecurityLogicImpl.login(name, password)` → `LoginLogicImpl.checkLogin(name, password)`
   - buď heslo v DB (`loginDao.getByName(name)` + MD5 hash přes `LoginHelper.hashPassword`), nebo LDAP (`loginDao.getByLdapLogin(name)` + `LdapUserDetailsService.authenticateUser`, zapnuto přes `retra.ldapAuth.use` / `retra.ldapAuth.only` v `retra.properties`);
   - po úspěchu `RoleHelper.prepareUserPermissions(login.getUser())` a `securityContext.login(user)`.
4. `MiraSecurityLogicImpl` (bean `securityLogic` v `securityContext.xml`) dědí `WebSecurityLogicImpl`; `getLoggedEmployee()` lazy načte `Employee` z `loggedUser`.
5. Odhlášení: `Logout.do` → `doLogout()` → `securityLogic.logout()` (zruší permanentní heslo + `securityContext.logout()`), redirect na `Login.do`.

### 2.2 Klíčové soubory

| Soubor | Role |
|---|---|
| `src/main/webapp/WEB-INF/web.xml` | filtry (`SecurityWebFilter` na `*.do`/`*.screen`), servlety `action`/`screen` (`*.do`/`*.screen`) |
| `src/main/java/cz/softinel/sis/security/web/SecurityWebFilter.java` | vstupní kontrola přihlášení, forward na `Login.do` |
| `src/main/java/cz/softinel/sis/security/web/WebSecurityLogicImpl.java` | session `SecurityContext`, seznam unsecured pages |
| `src/main/java/cz/softinel/retra/security/blo/MiraSecurityLogicImpl.java` | bean `securityLogic`, `login(...)`, `getLoggedEmployee()` |
| `src/main/java/cz/softinel/retra/core/system/web/SystemController.java` | `showLogin`/`doLogin`/`doLogout` |
| `src/main/webapp/WEB-INF/action-servlet.xml` | beany `/Login.do`, `/LoginAction.do`, `/Logout.do` (parent `SystemController` → `DispatchController` s `securityLogic`) |
| `src/main/webapp/WEB-INF/jsp/system/Login.jsp` | přihlašovací formulář |
| `src/main/java/cz/softinel/sis/login/blo/LoginLogicImpl.java` | `checkLogin(name, password)` (DB/LDAP), `checkLogin(permanentPassword)` |
| `src/main/java/cz/softinel/sis/login/dao/HibernateLoginDao.java` | `getByName`, **`getByLdapLogin`**, `getByPermanentPassword` |
| `src/main/resources/cz/softinel/sis/login/Login.hbm.xml` | tabulka `sis11login`, sloupec `sis11ldaplogin` (unique), named query `Login.getByLdapLogin` |
| `src/main/webapp/WEB-INF/config/securityContext.xml` | `securityLogic`, `loginLogic` (`useLdapAuth`), LDAP beany, `PropertyPlaceholderConfigurer` (`${retra.config.location}retra.properties`) |
| `src/main/resources/defaultconf/retra.properties` (+ `etc/conf/retra.properties` v deployment) | konfigurace (`retra.ldapAuth.*`, `jira.*`, …) |

**Důležité pro integraci:** `SecurityLogic.login(User)` je na rozhraní `SecurityLogic` (a tedy i `MiraSecurityLogic`) — stačí zavolat `getSecurityLogic().login(login.getUser())` a celý zbytek aplikace (role, oprávnění, employee, session) funguje beze změny.

---

## 3. Jednoúčelová náhrada za lib-oauth (v projektu, bez nových závislostí)

Místo `cz.hb.lib:lib-oauth` vznikne malá vlastní implementace OIDC Authorization Code flow v balíčku **`cz.softinel.retra.security.keycloak`**:

| Třída | Účel |
|---|---|
| `KeycloakConfig` | POJO naplněné přes Spring XML z `${retra.keycloak.*}` placeholderů: `enabled`, `realmUrl`, `clientId`, `clientSecret`, `scopes`, `loginLimitMinutes`. Odvozuje endpointy: `{realmUrl}/protocol/openid-connect/{auth,token,userinfo,logout}`. |
| `KeycloakLoginTx` | jedna login transakce: `id`, `state`, `codeVerifier`, `callbackUrl`, `created`, `expires`. |
| `KeycloakLoginStorage` | in-memory `Map<String, KeycloakLoginTx>` s expirací a úklidem při přístupu (jednoduché, single-node — viz omezení). Bean `keycloakLoginStorage`. |
| `KeycloakLoginResult` | výsledek callbacku: `ok`, `username` (= `preferred_username`), `email`, `name`, `sub`, nebo `errorStep`/`errorCode`/`errorMessage` (`STATE`, `AUTH`, `TOKEN`, `UINFO`, `USER`). |
| `KeycloakOidcService` | jádro: `createLoginTx(callbackUrl)`, `buildAuthorizationRequest(tx)` → `URI`, `handleAuthorizationResponse(tx, responseUri)` → `KeycloakLoginResult`. |

### 3.1 Implementační detaily `KeycloakOidcService`

- **HTTP:** `java.net.http.HttpClient` (JDK 11, žádná nová závislost) s connect/read timeoutem.
- **JSON:** Jackson 1.9.2 `org.codehaus.jackson.map.ObjectMapper` — už je v aplikaci přes přímou závislost `com.sun.jersey:jersey-json` (v WARu `jackson-mapper-asl-1.9.2.jar`).
- **PKCE:** `code_verifier` = 64 znaků z `SecureRandom` (base64url), `code_challenge` = base64url(SHA-256(verifier)), `code_challenge_method=S256`.
- **Authorization request:** `{authEndpoint}?response_type=code&client_id=...&redirect_uri=<callback s dosazeným id tx>&scope=...&state=...&code_challenge=...&code_challenge_method=S256`. V callback URL se placeholder `{loginTxId}` nahradí za id transakce.
- **Token exchange:** POST `application/x-www-form-urlencoded` na token endpoint: `grant_type=authorization_code`, `code`, `redirect_uri`, `code_verifier`, autentizace klienta přes hlavičku `Authorization: Basic base64(clientId:clientSecret)`.
- **Identity:** GET userinfo s hlavičkou `Bearer <access_token>` → `preferred_username`, `email`, `name`, `sub`. Doplňkově dekódovat payload `id_token` (plain base64url + Jackson) a zkontrolovat `iss`, `aud`, `exp` — ověření podpisu se vědomě přeskočí, protože tokeny přicházejí přímo z token endpointu přes TLS (v souladu s OIDC Core pro tento flow); identita se pak ještě potvrzuje přímým voláním userinfo přes TLS.
- **Chyby:** `error` parametr v callbacku, neplatný `state`, ne-200 odpověď token/userinfo endpointu → `KeycloakLoginResult` s odpovídajícím `errorStep`.

### 3.2 Konfigurační klíče (`retra.properties`)

Styl konzistentní se zbytkem `retra.properties` (prefix `retra.`), secret přímo zde (dle rozhodnutí výše):

```properties
## Keycloak (OIDC) login — viz doc/spec/HBR-5.md
retra.keycloak.enabled=true
retra.keycloak.realmUrl=https://keycloak.example.com/realms/HB
retra.keycloak.clientId=APP_Retra
retra.keycloak.clientSecret=<client-secret>
retra.keycloak.scopes=openid profile email
retra.keycloak.loginLimitMinutes=5
```

V `src/main/resources/defaultconf/retra.properties` výchozí hodnoty (`enabled=false`, prázdný `realmUrl`/`clientId`/`clientSecret`, `scopes=openid`, `loginLimitMinutes=5`) — všechny klíče musí existovat, aby placeholdery šly resolvovat. Deployment-specific hodnoty do `etc/conf/retra.properties`.

---

## 4. Implementační plán

### Krok 1 — `pom.xml`

**Beze změny** — `java.net.http.HttpClient` je součástí JDK 11, Jackson 1.9.2 už je v aplikaci přes `jersey-json`. Žádná nová závislost.

### Krok 2 — konfigurace `retra.properties`

Přidat blok `retra.keycloak.*` podle kapitoly 3.2 do `defaultconf/retra.properties` (vypnuto) a `etc/conf/retra.properties` (zapnuto, reálné hodnoty vč. secretu).

### Krok 3 — nové třídy v `cz.softinel.retra.security.keycloak`

`KeycloakConfig`, `KeycloakLoginTx`, `KeycloakLoginStorage`, `KeycloakLoginResult`, `KeycloakOidcService` podle kapitoly 3.

### Krok 4 — Spring beany (`WEB-INF/config/securityContext.xml`)

`PropertyPlaceholderConfigurer` už načítá `${retra.config.location}retra.properties`, takže stačí klasické XML wiring (žádné `@Autowired`, žádné nové knihovny):

```xml
<bean id="keycloakConfig" class="cz.softinel.retra.security.keycloak.KeycloakConfig">
    <property name="enabled" value="${retra.keycloak.enabled}"/>
    <property name="realmUrl" value="${retra.keycloak.realmUrl}"/>
    <property name="clientId" value="${retra.keycloak.clientId}"/>
    <property name="clientSecret" value="${retra.keycloak.clientSecret}"/>
    <property name="scopes" value="${retra.keycloak.scopes}"/>
    <property name="loginLimitMinutes" value="${retra.keycloak.loginLimitMinutes}"/>
</bean>

<bean id="keycloakLoginStorage" class="cz.softinel.retra.security.keycloak.KeycloakLoginStorage">
    <property name="config" ref="keycloakConfig"/>
</bean>

<bean id="keycloakOidcService" class="cz.softinel.retra.security.keycloak.KeycloakOidcService">
    <property name="config" ref="keycloakConfig"/>
</bean>
```

### Krok 5 — `LoginLogic`: přihlášení podle `ldapLogin` bez hesla

Nová metoda v `cz.softinel.sis.login.blo.LoginLogic` + `LoginLogicImpl` (obdobně jako existující `checkLogin`):

```java
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public Login checkLoginByLdapLogin(String ldapLogin) {
    if (!StringUtils.hasLength(ldapLogin)) {
        return null;
    }
    Login login = loginDao.getByLdapLogin(ldapLogin);   // named query "Login.getByLdapLogin" už existuje
    if (login == null || !login.isActive()) {
        return null;
    }
    RoleHelper.prepareUserPermissions(login.getUser()); // lazy role/permission, stejně jako v checkLogin
    return login;
}
```

### Krok 6 — nový kontroler `KeycloakController`

`cz.softinel.retra.core.system.web.KeycloakController extends DispatchController` (dostane `securityLogic` z parent `DispatchController`; `oAuthService`/`loginLogic` analogicky jako `keycloakOidcService`/`loginLogic`). Metody s podpisem `(HttpServletRequest, HttpServletResponse)` — `CommonDispatchController` nejprve hledá `(Model, RequestContext)`, když nenajde, spadne na `MultiActionController`, který tento podpis podporuje:

```java
public ModelAndView keycloakLogin(HttpServletRequest request, HttpServletResponse response) {
    if (!keycloakConfig.isEnabled()) {
        return new ModelAndView("redirect:Login.do?fkprm=true");
    }
    // callback URL s placeholder — service dosadí {loginTxId}
    StringBuffer url = request.getRequestURL();
    String base = url.substring(0, url.indexOf(request.getServletPath()));
    String callbackUrl = base + "/KeycloakCallback.do?loginTxId={loginTxId}";
    KeycloakLoginTx tx = keycloakOidcService.createLoginTx(callbackUrl);
    keycloakLoginStorage.put(tx);
    // zapamatovat originalUrl pro redirect po přihlášení
    String originalUrl = request.getParameter("originalUrl");
    if (originalUrl != null) {
        request.getSession().setAttribute("keycloakOriginalUrl", originalUrl);
    }
    URI authRequest = keycloakOidcService.buildAuthorizationRequest(tx);
    return new ModelAndView("redirect:" + authRequest.toString());
}

public ModelAndView keycloakCallback(HttpServletRequest request, HttpServletResponse response) {
    String loginTxId = request.getParameter("loginTxId");
    KeycloakLoginTx tx = keycloakLoginStorage.get(loginTxId);
    if (tx == null) {
        return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=tx");
    }
    String qs = request.getQueryString();
    String responseUri = request.getRequestURL() + (qs != null ? "?" + qs : "");
    KeycloakLoginResult result = keycloakOidcService.handleAuthorizationResponse(tx, responseUri);
    if (result.isOk()) {
        String username = result.getUsername();                 // preferred_username z Keycloaku
        Login login = loginLogic.checkLoginByLdapLogin(username); // mapování na sis11ldaplogin
        if (login != null) {
            // session fixation — regenerovat session a přenést security context
            SecurityContext ctx = getSecurityLogic().getSecurityContext();
            getSecurityLogic().login(login.getUser());
            HttpSession old = request.getSession(false);
            if (old != null) old.invalidate();
            request.getSession(true).setAttribute("securityContext", ctx);
            // redirect
            String originalUrl = (String) request.getSession().getAttribute("keycloakOriginalUrl");
            request.getSession().removeAttribute("keycloakOriginalUrl");
            if (originalUrl != null && originalUrl.length() > 0) {
                return new ModelAndView("redirect:" + originalUrl);
            }
            return new ModelAndView("redirect:Newsboard.do?fkprm=true");
        }
        return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=unknownUser");
    }
    log.warn("Keycloak callback error: {} / {} - {}", result.getErrorStep(), result.getErrorCode(), result.getErrorMessage());
    return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=" + result.getErrorStep());
}
```

### Krok 7 — registrace URL v `action-servlet.xml`

```xml
<bean id="KeycloakController" class="cz.softinel.retra.core.system.web.KeycloakController"
      abstract="true" parent="DispatchController">
    <property name="keycloakConfig" ref="keycloakConfig"/>
    <property name="keycloakOidcService" ref="keycloakOidcService"/>
    <property name="keycloakLoginStorage" ref="keycloakLoginStorage"/>
    <property name="loginLogic" ref="loginLogic"/>
</bean>

<bean name="/KeycloakLogin.do" parent="KeycloakController">
    <property name="method" value="keycloakLogin"/>
</bean>
<bean name="/KeycloakCallback.do" parent="KeycloakController">
    <property name="method" value="keycloakCallback"/>
</bean>
```

### Krok 8 — unsecured pages v `WebSecurityLogicImpl.initUnsecuredPages()`

```java
unsecuredPages.put(contextPath + "/KeycloakLogin.do", "");
unsecuredPages.put(contextPath + "/KeycloakCallback.do", "");
```

(Bez toho by `SecurityWebFilter` zacyklil požadavek zpět na `Login.do`.)

### Krok 9 — tlačítko na přihlašovací stránce

- `SystemController.showLogin()`: `model.put("keycloakEnabled", keycloakConfig.isEnabled());` (property `keycloakConfig` na abstract beanu `SystemController`).
- `Login.jsp` — pod formulář:

```jsp
<c:if test="${keycloakEnabled}">
    <p>
        <a class="button" href="KeycloakLogin.do?fkprm=true&originalUrl=${originalUrl}">
            <fmt:message key="button.login.keycloak"/>
        </a>
    </p>
    <c:if test="${param.keycloakError != null}">
        <p class="error"><fmt:message key="error.login.keycloak"/></p>
    </c:if>
</c:if>
```

- Nové klíče do `WEB-INF/resources/messages.properties`, `messages_cs.properties`, `messages_en.properties` (`button.login.keycloak`, `error.login.keycloak`).

### Krok 10 — odhlášení z Keycloaku (volitelně, fáze 2)

`doLogout` může přesměrovat na `{realmUrl}/protocol/openid-connect/logout?id_token_hint=...&post_logout_redirect_uri=<app>/Login.do`. Vyžaduje uložení `id_token` do session při callbacku. Zatím ponechat jen lokální logout.

---

## 5. Konfigurace na straně Keycloaku

1. Realm (např. `HB`), klient `APP_Retra`:
   - typ **confidential**, **Standard flow** (authorization code);
   - *Valid redirect URIs*: `https://<host>/retra/KeycloakCallback.do*` (přesně odpovídající callbacku);
   - *Web origins*: `https://<host>`;
   - client secret odpovídá `retra.keycloak.clientSecret`.
2. Scope `openid profile email` — `preferred_username` je ve userinfo automaticky.
3. **Uživatelé v Keycloaku musí mít `username` rovný hodnotě `sis11ldaplogin`** příslušného řádku v `sis11login` (sloupec je unique). Uživatelé bez párového účtu v Retře se nepřihlásí (chyba `unknownUser` → `Login.do`).
4. V produkci jen HTTPS redirect URI.

---

## 6. Bezpečnostní poznámky

- `state` + PKCE (S256) + expirace transakce (`loginLimitMinutes`) — řeší vlastní `KeycloakOidcService`/`KeycloakLoginStorage`; callback bez platné tx → chyba.
- Token exchange i userinfo probíhají server-to-server přes HTTPS; klient se autentizuje `ClientSecretBasic`. Podpis `id_token` se vědomě neověřuje (token přišel přímo z token endpointu přes TLS), identita se potvrzuje přímým voláním userinfo; volitelně kontrola `iss`/`aud`/`exp` z payloadu. JWKS validace podpisu možná jako budoucí hardening.
- Session fixation: při úspěšném callbacku session invalidovat a znovu vytvořit, `securityContext` přenést (viz krok 6).
- `originalUrl` redirect — používá se jen hodnota, kterou do `Login.do` vložil `SecurityWebFilter` (url-encoded); nekontroluje se proti open redirect, protože pochází z interního forwardingu, ne od uživatele.
- Produkce za reverse proxy: callback URL se skládá z `request.getRequestURL()` — při terminaci TLS na proxy zajistit `X-Forwarded-*` (Tomcat `RemoteIpValve`/`ProxyValve`), jinak Keycloak dostane špatné redirect URI.
- Keycloak slouží jen jako identity provider — žádná práva/role se z něj nepřebírají; oprávnění řeší Retra (`RoleHelper.prepareUserPermissions`).

---

## 7. Testovací scénáře

1. `retra.keycloak.enabled=false` → `Login.jsp` nezobrazí tlačítko Keycloak, `/KeycloakLogin.do` redirectuje na `Login.do`; formulářový login funguje beze změny.
2. Zapnutý Keycloak, platný uživatel (`preferred_username` = `ldapLogin`): klik na tlačítko → Keycloak → callback → přihlášen, redirect na `Newsboard.do`; role/oprávnění načtena z Retry (worklog viditelný).
3. Deep link: nepřihlášený uživatel otevře `/WorklogOverview.do` → forward na `Login.do` s `originalUrl` → Keycloak login → redirect zpět na původní URL.
4. Neznámý uživatel v Keycloaku (bez `sis11login` řádku): chyba, redirect na `Login.do?keycloakError=unknownUser`.
5. Neplatný `state` / expirovaná transakce (přes `loginLimitMinutes`): `keycloakError=STATE` / `tx`.
6. Blokovaný uživatel v Retře (`sis11state != ACTIVE`): jako neznámý uživatel.
7. Chyba na token/userinfo endpointu (např. špatný secret): `keycloakError=TOKEN`/`UINFO`, nothing přihlásí.
8. Logout → `Login.do`; formulářový login stále funkční (fallback).

---

## 8. Rizika a otevřené body

- `KeycloakLoginStorage` je in-memory — OK, aplikace běží na jednom node (dle rozhodnutí); při budoucím clusteru nutno řešit (sdílená session/Redis).
- Žádné nové závislosti: HTTP přes JDK 11 `java.net.http.HttpClient`, JSON přes Jackson 1.9.2 z `jersey-json`. Verze Javy 11 je v `pom.xml` garantovaná.
- Secret v `retra.properties` (per-environment soubor mimo git) — v souladu se současným řešením; případně později přes externí konfiguraci jako u app-reman (vault).
- Endpointy se odvozují z `retra.keycloak.realmUrl` podle konvence Keycloaku (`/protocol/openid-connect/...`); discovery dokument se nepoužívá (jednoúčelové řešení).
- Keycloak role se nepromítají do retra rolí — oprávnění zůstávají řízena v Retře.
