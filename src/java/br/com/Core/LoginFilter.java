//package br.com.Core;
//
//import br.com.Model.Usuario;
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//
//
//public class LoginFilter implements Filter {
//
//	public void destroy() {
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		Usuario usuario = null;
//		HttpSession sess = ((HttpServletRequest) request).getSession(false);
//		if (sess != null) {
//			usuario = (Usuario) sess.getAttribute("usuarioLogado");
//		}
//		if (usuario == null || usuario.getId() == 0) {
//			String contextPath = ((HttpServletRequest) request).getContextPath();
//			((HttpServletResponse) response).sendRedirect(contextPath + "/index.xhtml");
//		} else {
//			chain.doFilter(request, response);
//		}
//	}
//
//	public void init(FilterConfig arg0) throws ServletException {
//	}
//}
