package hw.Networking.HTTP;

import com.sun.net.httpserver.*;
import hw.DB.DBProcessor;
import hw.Shop.Product;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;


public class SimpleHttpServer {
    private static final Key JWT_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    static DBProcessor DB = new DBProcessor();

    public static void Response(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    public static String GetRequestBody(HttpExchange exchange) throws IOException {
        InputStream in = exchange.getRequestBody();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        while (br.ready()) {
            sb.append(br.readLine());
        }

        return String.valueOf(sb);
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8765), 0);

        HttpContext context = server.createContext("/api/good/", new GoodHandler());
        //HttpContext login = server.createContext("/login", new AuthHandler() );

        context.setAuthenticator(new Auth());
        //login.setAuthenticator(new Auth());


        server.setExecutor(null);
        server.start();
    }

    static class GoodHandler implements HttpHandler {
        public static String ROUTE = "/api/good/";

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String URL = exchange.getRequestURI().toString();
            System.out.println(URL);

            if (URL.length() > ROUTE.length()) {
                int id = Integer.parseInt(exchange.getRequestURI().toString().substring(ROUTE.length()));
                Product product = DB.Get(id);

                if(product == null)
                {
                    Response(exchange, 404, "Product " + id + " Not found");
                    return;
                }

                if ("get".equalsIgnoreCase(exchange.getRequestMethod())) {
                    JSONObject json = new JSONObject(product);
                    Response(exchange, 200, "Ok\n" + json.toString(1));
                }
                else if("post".equalsIgnoreCase(exchange.getRequestMethod()))
                {
                    InputStream in = exchange.getRequestBody();
                    JSONParser jsonParser = new JSONParser(new InputStreamReader(in, StandardCharsets.UTF_8));
                    JSONObject req = null;
                    try {
                        req = (JSONObject) jsonParser.parse();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    DB.Update(new Product(req));

                    Response(exchange, 204, "No Content");

                }
                else if("delete".equalsIgnoreCase(exchange.getRequestMethod()))
                {
                    DB.Delete(id);
                    Response(exchange, 204, "No Content");
                }
            }
            else
            {
                if ("put".equalsIgnoreCase(exchange.getRequestMethod())) {
                    String strings = GetRequestBody(exchange);
                    JSONObject json = new JSONObject(strings);

                    DB.Create(new Product(json));
                    Response(exchange, 200, "Ok\n" + json.toString(1));
                }
            }
        }
    }


    static class AuthHandler implements HttpHandler {
        public static String ROUTE = "/login/";
        private static final String LOGIN = "qwerty";
        private static final String PASSWD = "killmepls";

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = GetRequestBody(exchange);
                JSONObject json = new JSONObject(body);

                // TODO : MD5
                String login = json.getString("login");
                String passwd = json.getString("password");

                if (login.equals(LOGIN) && passwd.equals(PASSWD)) {
                    String token = GenerateToken();

                    JSONObject response = new JSONObject();
                    response.put("token", token);

                    Response(exchange, 200, response.toString());
                    return;
                }
            }

            Response(exchange, 404, "Unauthorized");
        }

        public static String GenerateToken() {
            String id = UUID.randomUUID().toString().replace("-", "");

            String token = Jwts.builder()
                    .setIssuer(id)
                    .setSubject(LOGIN)
                    .setExpiration(new Date(System.currentTimeMillis() + 1488 * 6666))
                    .setNotBefore(new Date())
                    .setIssuedAt(new Date())
                    .signWith(JWT_KEY).compact();

            return token;
        }


    }

    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            if ("/login".equals(httpExchange.getRequestURI().toString())) {
                if (httpExchange.getRequestHeaders().getFirst("qwerty").equals("killmepls"))
                    return new Success(new HttpPrincipal("cOnst", "realm"));
            }
            return new Failure(401);
        }
    }
}
