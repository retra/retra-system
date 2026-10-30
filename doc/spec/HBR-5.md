# HBR-5: Přihlášení přes Keycloak (OIDC) v app-retra

## 1. Cíl

Přidat do aplikace app-retra možnost přihlášení přes **Keycloak pomocí OIDC (Authorization Code flow)**, a to bez zásahu do současného legacy stacku (Spring 2.5 XML konfigurace, `MultiActionController`, Hibernate 3, Servlet 2.4 na Tomcatu, Java 11). Klasické přihlášení formulářem (heslo v DB / LDAP) zůstává zachováno jako fallback.

**Mapování uživatelů (jednoduché, podle zadání):** `preferred_username` z Keycloaku odpovídá hodnotě sloupce `sis11login.sis11ldaplogin` (existující pole `Login.ldapLogin`). Žádná změna DB schématu není potřeba.

Inspirace: projekt **app-reman**, kde je totéž již implementováno přes knihovnu `cz.hb.lib:lib-oauth` (viz kapitola 3).

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

## 3. Rozhodnutí: knihovna `lib-oauth` (stejně jako v app-reman)

- Oficiální Keycloak Java adaptery (vč. servlet-filter adapteru) byly **odstraněny v Keycloaku 25** — pro tento stack už nejsou použitelné. Spring Security by znamenal migraci celé aplikace (konflikt se Spring 2.5).
- V **app-reman** je přihlášení přes Keycloak již hotové přes knihovnu **`cz.hb.lib:lib-oauth:1.002.0`** (používá Nimbus `oauth2-oidc-sdk`):
  - `UserLoginPageInit` — vytvoří login transakci a přesměruje na Keycloak;
  - `UserLoginCallbackPageInit` — zpracuje callback, uloží přihlášení do session;
  - konfigurace v `app-reman/web/conf/reman-config.txt` (klíče `oauth.*`, `vault.keycloak.client.*`);
  - bean `OAuthService` (`OAuthServiceImpl`) v `RemanAppConfig`.

### 3.1 API knihovny `cz.hb.lib.oauth.OAuthService`

```java
OAuthConfig       getConfig(String clientName);                    // null když oauth.login.active=false
OAuthLoginTx      createLoginTx(OAuthConfig config, String callbackUrl); // callbackUrl může obsahovat "{loginTx.id}"
URI               getAuthorizationRequest(OAuthLoginTxCore tx);    // URL Keycloak auth endpointu (state, scope, redirect_uri)
OAuthResponse     handleAuthorizationResponse(OAuthLoginTxCore tx, String responseUri); // ověří state, vymění code za token, zavolá userinfo
```

- `OAuthResponse.isOk()` / `getError()` (`step` = ERR_STATE/ERR_AUTH/ERR_TOKEN/ERR_UINFO, `code`, `message`).
- `OAuthResponse.getUser()` → `OAuthResponseUser`: **`getLogin()` = `preferred_username`** z userinfo, `getId()` = `sub`, `getEmail()`, `getName()`.
- `OAuthLoginStorage` — in-memory úložiště transakcí s expirací (`putRecord`/`getRecord`).
- `OAuthServiceImpl` má `@Autowired` pole `oAuthProperties` (`PropertyProvider`) a `configVault` (`ConfigVault`); setter jen `setOAuthProperties`.

### 3.2 Konfigurační klíče knihovny (z `reman-config.txt` a bytecode knihovny)

```properties
oauth.login.active = true            # master switch; getConfig() vrací null když false
oauth.login.scopes = openid profile email
oauth.login.limitMinutes = 5         # platnost login transakce (1..20)

oauth.realm.<realm-id> = https://keycloak.example.com/realms/<realm>   # URL realmu

oauth.client.retra = <keycloak-client-id>        # clientName "retra" -> clientId
oauth.client.retra.realm = <realm-id>            # odkaz na realm výše

# client secret pro token endpoint — čte se přes ConfigVault pod klíčem
# "keycloak.client.<clientId>", v praxi přes property (viz app-reman):
vault.keycloak.client.<keycloak-client-id>.name = <keycloak-client-id>
vault.keycloak.client.<keycloak-client-id>.secret = <client-secret>
```

### 3.3 Závislosti

`lib-oauth:1.002.0` táhne transitivně `cz.cmhb.lib:lib-config`, `cz.cmhb.lib:lib-util`, `cz.hb.lib:lib-auth`, `com.nimbusds:oauth2-oidc-sdk` (+ `nimbus-jose-jwt`, `content-type`, `lang-tag`) — vše kompatibilní s Java 11. Skupina `cz.hb.lib` se v `app-retra/pom.xml` už používá (`lib-jira`).

---

## 4. Implementační plán

### Krok 1 — `pom.xml`: přidat závislost

```xml
<lib-oauth.version>1.002.0</lib-oauth.version>
...
<dependency>
    <groupId>cz.hb.lib</groupId>
    <artifactId>lib-oauth</artifactId>
    <version>${lib-oauth.version}</version>
</dependency>
```

### Krok 2 — konfigurace `retra.properties`

Do `src/main/resources/defaultconf/retra.properties` (výchozí hodnoty, `oauth.login.active=false`) a do deployment-specific `etc/conf/retra.properties` blok podle kapitoly 3.2, např.:

```properties
## Keycloak (OIDC) login — viz doc/spec/HBR-5.md
oauth.login.active=true
oauth.login.scopes=openid profile email
oauth.login.limitMinutes=5
oauth.realm.hb-entra-ldap=https://keycloak.example.com/realms/HB
oauth.client.retra=APP_Retra
oauth.client.retra.realm=hb-entra-ldap
vault.keycloak.client.APP_Retra.name=APP_Retra
vault.keycloak.client.APP_Retra.secret=<client-secret>
```

### Krok 3 — Spring beany (`WEB-INF/config/securityContext.xml`)

Knihovna používá `@Autowired` na privátních polích, proto je potřeba zapnout annotation-config a definovat beany:

```xml
<beans xmlns="..." xmlns:context="http://www.springframework.org/schema/context" ...>
    <context:annotation-config/>

    <!-- properties pro lib-oauth (čte přímo retra.properties) -->
    <bean id="oAuthProperties" class="cz.cmhb.lib.config.DynamicProperties">
        <constructor-arg value="${retra.config.location}retra.properties"/>
    </bean>
    <bean id="vaultConfigProperties" class="cz.cmhb.lib.config.DynamicProperties">
        <constructor-arg value="${retra.config.location}retra.properties"/>
    </bean>
    <!-- property-based varianta ConfigVault (secret z vault.keycloak.client.*.secret) -->
    <bean id="configVault" class="cz.cmhb.lib.config.vault.ConfigVaultImpl"/>
    <bean id="vaultSecretGetter" class="cz.softinel.retra.security.oauth.RetraVaultSecretGetter"/>

    <bean id="oAuthService" class="cz.hb.lib.oauth.OAuthServiceImpl">
        <property name="oAuthProperties" ref="oAuthProperties"/>
        <!-- configVault se doinjektuje přes @Autowired -->
    </bean>

    <bean id="oAuthLoginStorage" class="cz.hb.lib.oauth.OAuthLoginStorage"/>
    ...
</beans>
```

`RetraVaultSecretGetter` — malá třída implementující `cz.cmhb.lib.config.vault.VaultSecretGetter` (vrací `null` / hází `UnsupportedOperationException`; použije se jen property fallback `vault.keycloak.client.<id>.secret` z `retra.properties`).

### Krok 4 — `LoginLogic`: přihlášení podle `ldapLogin` bez hesla

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

### Krok 5 — úložiště login transakcí

`cz.softinel.retra.security.oauth.RetraOAuthStorage` — tenký wrapper nad beanem `OAuthLoginStorage` (stejně jako `RemanOAuthStorage` v app-reman):

```java
public class RetraOAuthStorage {
    private static OAuthLoginStorage storage; // nebo instanční přes Spring
    public static void put(OAuthLoginTx tx) { storage.putRecord(tx); }
    public static OAuthLoginTx get(String loginTxId) { return storage.getRecord(loginTxId); }
}
```

### Krok 6 — nový kontroler `KeycloakController`

`cz.softinel.retra.core.system.web.KeycloakController extends DispatchController` (dostane `securityLogic` z parent `DispatchController`). Metody s podpisem `(HttpServletRequest, HttpServletResponse)` — `CommonDispatchController` nejprve hledá `(Model, RequestContext)`, když nenajde, spadne na `MultiActionController`, který tento podpis podporuje:

```java
public ModelAndView keycloakLogin(HttpServletRequest request, HttpServletResponse response) {
    OAuthConfig config = oAuthService.getConfig("retra");      // clientName
    if (config == null) {
        return new ModelAndView("redirect:Login.do?fkprm=true"); // Keycloak vypnutý
    }
    // callback URL s placeholder — knihovna dosadí {loginTx.id}
    StringBuffer url = request.getRequestURL();
    String base = url.substring(0, url.indexOf(request.getServletPath()));
    String callbackUrl = base + "/KeycloakCallback.do?loginTxId={loginTx.id}";
    OAuthLoginTx tx = oAuthService.createLoginTx(config, callbackUrl);
    RetraOAuthStorage.put(tx);
    // zapamatovat originalUrl pro redirect po přihlášení
    String originalUrl = request.getParameter("originalUrl");
    if (originalUrl != null) {
        request.getSession().setAttribute("keycloakOriginalUrl", originalUrl);
    }
    URI authRequest = oAuthService.getAuthorizationRequest(tx);
    return new ModelAndView("redirect:" + authRequest.toString());
}

public ModelAndView keycloakCallback(HttpServletRequest request, HttpServletResponse response) {
    String loginTxId = request.getParameter("loginTxId");
    OAuthLoginTx tx = RetraOAuthStorage.get(loginTxId);
    if (tx == null) {
        return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=tx");
    }
    String qs = request.getQueryString();
    String responseUri = request.getRequestURL() + (qs != null ? "?" + qs : "");
    OAuthResponse resp = oAuthService.handleAuthorizationResponse(tx, responseUri);
    if (resp.isOk()) {
        String username = resp.getUser().getLogin();            // preferred_username z Keycloaku
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
    OAuthResponseError err = resp.getError();
    log.warn("Keycloak callback error: {} / {} - {}", err.getStep(), err.getCode(), err.getMessage());
    return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=" + err.getStep());
}
```

Kontroler potřebuje property `oAuthService` a `loginLogic` — přidat jako setter property v XML (stejně jako ostatní kontrolery).

### Krok 7 — registrace URL v `action-servlet.xml`

```xml
<bean id="KeycloakController" class="cz.softinel.retra.core.system.web.KeycloakController"
      abstract="true" parent="DispatchController">
    <property name="oAuthService" ref="oAuthService"/>
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

- `SystemController.showLogin()`: `model.put("keycloakEnabled", oAuthService.getConfig("retra") != null);` (potřebuje property `oAuthService` na abstract `SystemController` beanu).
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

### Krok 10 — odhlášení (volitelně, fáze 2)

`doLogout` může přesměrovat na Keycloak `end_session_endpoint` (`{realmUrl}/protocol/openid-connect/logout?id_token_hint=...&post_logout_redirect_uri=<app>/Login.do`). ID token není aktuálně nikam ukládán — vyžadovalo by uložení do session při callbacku. Doporučeno ponechat zatím jen lokální logout.

---

## 5. Konfigurace na straně Keycloaku

1. Realm (např. `HB`), klient `APP_Retra`:
   - typ **confidential**, **Standard flow** (authorization code);
   - *Valid redirect URIs*: `https://<host>/retra/KeycloakCallback.do*` (přesně odpovídající callbacku);
   - *Web origins*: `https://<host>`;
   - client secret odpovídá `vault.keycloak.client.APP_Retra.secret`.
2. Scope `openid profile email` — `preferred_username` je ve userinfo automaticky.
3. **Uživatelé v Keycloaku musí mít `username` rovný hodnotě `sis11ldaplogin`** příslušného řádku v `sis11login` (sloupec je unique). Uživatelé bez párového účtu v Retře se nepřihlásí (chyba `unknownUser` → `Login.do`).
4. V produkci jen HTTPS redirect URI.

---

## 6. Bezpečnostní poznámky

- `state` a expirace transakce (limitMinutes) řeší knihovna; callback bez platné tx → chyba.
- Token exchange a userinfo probíhají server-to-server přes HTTPS s client secretem (Nimbus SDK, `ClientSecretBasic`).
- Session fixation: při úspěšném callbacku session invalidovat a znovu vytvořit, `securityContext` přenést (viz krok 6).
- `originalUrl` redirect — používá se jen hodnota, kterou do `Login.do` vložil `SecurityWebFilter` (url-encoded); nekontroluje se proti open redirect, protože pochází z interního forwardingu, ne od uživatele.
- Produkce za reverse proxy: callback URL se skládá z `request.getRequestURL()` — při terminaci TLS na proxy zajistit `X-Forwarded-*` (Tomcat `RemoteIpValve`/`ProxyValve`), jinak Keycloak dostane špatné redirect URI.

---

## 7. Testovací scénáře

1. `oauth.login.active=false` → `Login.jsp` nezobrazí tlačítko Keycloak, `/KeycloakLogin.do` redirectuje na `Login.do`; formulářový login funguje beze změny.
2. Zapnutý Keycloak, platný uživatel (`preferred_username` = `ldapLogin`): klik na tlačítko → Keycloak → callback → přihlášen, redirect na `Newsboard.do`; role/oprávnění načtena (worklog viditelný).
3. Deep link: ne přihlášený uživatel otevře `/WorklogOverview.do` → forward na `Login.do` s `originalUrl` → Keycloak login → redirect zpět na původní URL.
4. Neznámý uživatel v Keycloaku (bez `sis11login` řádku): chyba, redirect na `Login.do?keycloakError=unknownUser`.
5. Neplatný `state` / expirovaná transakce (přes 5 min): `keycloakError=ERR_STATE` / `tx`.
6. Blokovaný uživatel v Retře (`sis11state != ACTIVE`): jako neznámý uživatel.
7. Logout → `Login.do`; formulářový login stále funkční (fallback).

---

## 8. Rizika a otevřené body

- `OAuthLoginStorage` je in-memory — při více uzlech za LB by transakce nebyly sdílené (zatím single-node, OK; případně řešit později).
- Verze `lib-oauth` vázaná na to, co používá app-reman (1.002.0); případný upgrade řešit společně.
- `@Autowired` na polích knihovny vyžaduje `<context:annotation-config/>` v XML kontextu — přidáno v kroku 3.
- Secret v `retra.properties` (soubor je per-environment a mimo gitu — viz `.gitignore`); varianta přes CyberArk vault jako v app-reman možná později.
- Keycloak role se nepromítají do retra rolí — oprávnění zůstávají řízena v Retře (stejně jako u LDAP).
