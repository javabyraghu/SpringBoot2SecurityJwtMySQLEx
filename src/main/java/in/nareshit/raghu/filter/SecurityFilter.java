package in.nareshit.raghu.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.nareshit.raghu.util.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");
		if(token!=null) {
			//read username from token
			String username = jwtUtil.getUserName(token);
			//user name exist, user did not login before
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				//load current user from Database 
				UserDetails user = userDetailsService.loadUserByUsername(username);
				//Authentication Impl object
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(
								user.getUsername(), 
								user.getPassword(),
								user.getAuthorities());
				//link with request
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//Link Authentication into SecurityContext 
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
