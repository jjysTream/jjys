package com.yc.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	private boolean needLogin(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.endsWith("login")) {
			return false;
		}
		if (uri.contains("static")) {
			return false;
		}
		return true;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		String url = request.getRequestURI();
		if (url.contains("/management/") || url.contains("/personnel/") ) {
			if (session.getAttribute("loginPersonnle") == null && needLogin(request)) {
				HttpServletResponse response = (HttpServletResponse) rsp;
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}
		}
		chain.doFilter(req, rsp);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}