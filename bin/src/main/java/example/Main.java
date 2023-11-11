package example;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import io.helidon.media.jsonp.JsonpSupport;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.ConfigSource;

import io.helidon.security.Security;
import io.helidon.security.SubjectType;
import io.helidon.security.integration.webserver.WebSecurity;
import io.helidon.security.providers.httpauth.HttpBasicAuthProvider;
import io.helidon.security.providers.httpauth.SecureUserStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main{

  private static WebSecurity setSecurity(){
        Config config = Config.create();
        Map<String, MyUser> users = new HashMap<>();
        users.put("user", new MyUser("user", "user".toCharArray(), Collections.singletonList("ROLE_USER")));
        users.put("admin", new MyUser("admin", "admin".toCharArray(), Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
        SecureUserStore store = user -> Optional.ofNullable(users.get(user));

        HttpBasicAuthProvider httpBasicAuthProvider = HttpBasicAuthProvider.builder()
                .realm("myRealm")
                .subjectType(SubjectType.USER)
                .userStore(store)
                .build();

        //1. Using Builder Pattern or Config Pattern
        Security security = Security.builder()
                .addAuthenticationProvider(httpBasicAuthProvider)
                .build();
        //Security security = Security.create(config);

        //2. WebSecurity from Security or from Config
        WebSecurity webSecurity = WebSecurity.create(security).securityDefaults(WebSecurity.authenticate());

        // WebSecurity webSecurity = WebSecurity.create(config);
        return webSecurity;
  }

  private static Routing setRoutes(){
    WebSecurity security = setSecurity();

    Routing routing = Routing.builder()
        .register(security)
      //  .register("/dog", new DogResource())
        .get("/user", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_USER"))
        .get("/admin", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_ADMIN"))
      //  .get("/", (request, response) -> response.send("Hello World !"))
        .build();
    return routing;
  }

  private static int getPort(){      
    ConfigSource configSource = ConfigSources.classpath("application.yml").build();
    
    Config config = Config.builder()
            .disableSystemPropertiesSource()
            .disableEnvironmentVariablesSource()
            .sources(configSource)
            .build();

    return config.get("server.port").asInt().get(); 
  }
  /*

  public static void main(String... args) throws Exception {
      Routing routing = setRoutes();
      int port = getPort();

      WebServer.builder(routing)
        .port(port)
        .addMediaSupport(JsonpSupport.create())
        .addRouting(routing)
        .build()
        .start()
        .thenAccept(ws ->
            System.out.println("Server started at: http://localhost:" + ws.port())
        );
  }
  */
 public static void main(String... args){
          Config config = Config.create();

        Map<String, MyUser> users = new HashMap<>();
        users.put("user", new MyUser("user", "user".toCharArray(), Collections.singletonList("ROLE_USER")));
        users.put("admin", new MyUser("admin", "admin".toCharArray(), Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
        SecureUserStore store = user -> Optional.ofNullable(users.get(user));

        HttpBasicAuthProvider httpBasicAuthProvider = HttpBasicAuthProvider.builder()
                .realm("myRealm")
                .subjectType(SubjectType.USER)
                .userStore(store)
                .build();

        //1. Using Builder Pattern or Config Pattern
        Security security = Security.builder()
                .addAuthenticationProvider(httpBasicAuthProvider)
                .build();
        //Security security = Security.create(config);

        //2. WebSecurity from Security or from Config
         WebSecurity webSecurity = WebSecurity.create(security)
         .securityDefaults(WebSecurity.authenticate());

        //WebSecurity webSecurity = WebSecurity.create(config);

        Routing routing = Routing.builder()
                .register(webSecurity)
                .get("/user", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_USER"))
                .get("/admin", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_ADMIN"))
                .build();

        WebServer webServer = WebServer.create(routing, config.get("server"));

        webServer.start().thenAccept(ws ->
                System.out.println("Server started at: http://localhost:" + ws.port())
        );
 }

}