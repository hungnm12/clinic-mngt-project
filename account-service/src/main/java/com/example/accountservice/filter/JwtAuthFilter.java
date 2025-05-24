package com.example.accountservice.filter;


import com.example.accountservice.config.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public JwtAuthFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Bỏ qua filter cho các endpoint công khai (public endpoints)
        // Ví dụ: /api/auth/login, /api/auth/register
        // Bạn cần tự định nghĩa các đường dẫn này hoặc sử dụng Spring Security config để bỏ qua chúng
        if (request.getRequestURI().startsWith("/api/auth/login") || request.getRequestURI().startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // THÊM KIỂM TRA ĐỘ RỖNG/CÓ GIÁ TRỊ CỦA TOKEN
            if (token.isEmpty() || token.isBlank()) {
                logger.warn("Không tìm thấy JWT token sau 'Bearer ' prefix.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization token không hợp lệ hoặc thiếu.");
                return; // Dừng xử lý ở đây
            }

            try {
                username = jwtService.extractUsername(token);
            } catch (ExpiredJwtException e) {
                logger.warn("Token đã hết hạn: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token đã hết hạn.");
                return;
            } catch (MalformedJwtException e) { // Lỗi định dạng JWT không hợp lệ (ví dụ: thiếu dấu chấm)
                logger.error("Lỗi JWT định dạng không hợp lệ: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token định dạng không hợp lệ.");
                return;
            } catch (SignatureException e) { // Lỗi chữ ký không khớp
                logger.error("Lỗi JWT chữ ký không hợp lệ: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token chữ ký không hợp lệ.");
                return;
            } catch (UnsupportedJwtException e) { // Lỗi JWT không được hỗ trợ (ví dụ: thuật toán)
                logger.error("Lỗi JWT không được hỗ trợ: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token không được hỗ trợ.");
                return;
            } catch (IllegalArgumentException e) { // Lỗi đối số không hợp lệ (ví dụ: chuỗi rỗng/null)
                logger.error("Lỗi JWT đối số không hợp lệ (có thể do chuỗi rỗng/null): " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token không hợp lệ (đối số rỗng).");
                return;
            } catch (io.jsonwebtoken.security.WeakKeyException e) { // Lỗi khóa yếu (nếu bạn sử dụng io.jsonwebtoken.security)
                logger.error("Lỗi khóa JWT không an toàn: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Lỗi: Khóa JWT không an toàn. Vui lòng kiểm tra cấu hình.");
                return;
            } catch (JwtException e) { // Bắt tất cả các lỗi JWT còn lại
                logger.error("Lỗi JWT không xác định: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token không hợp lệ.");
                return;
            }
        } else {
            // Nếu không có header Authorization hoặc không bắt đầu bằng "Bearer "
            // Log thông báo hoặc bỏ qua nếu đây là các endpoint không cần xác thực
            // Hoặc trả về lỗi 401 nếu endpoint yêu cầu xác thực
            // logger.warn("Header Authorization không có hoặc không bắt đầu bằng 'Bearer ' cho request: " + request.getRequestURI());
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.getWriter().write("Yêu cầu không được xác thực.");
            // return; // Dừng xử lý nếu bạn muốn bắt buộc xác thực
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token)) { // Đảm bảo rằng validateToken cũng xử lý các ngoại lệ JWT
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("Token không hợp lệ sau khi validate.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token không hợp lệ.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
