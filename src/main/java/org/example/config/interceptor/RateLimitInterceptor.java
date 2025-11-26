package org.example.config.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Almacén en memoria: Mapa de IP -> Bucket (Cubo de tokens)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. Obtenemos la IP del cliente
        String ip = request.getRemoteAddr();

        // 2. Buscamos (o creamos) el Bucket para esa IP
        Bucket tokenBucket = cache.computeIfAbsent(ip, this::createNewBucket);

        // 3. Intentamos consumir 1 token
        if (tokenBucket.tryConsume(1)) {
            // Si hay tokens, dejamos pasar la petición (return true)
            return true;
        } else {
            // Si NO hay tokens, rechazamos
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Has excedido el limite de peticiones (10 por minuto).");
            return false;
        }
    }

    // Configuración del Bucket: 10 tokens, recarga cada 1 minuto
    private Bucket createNewBucket(String key) {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}