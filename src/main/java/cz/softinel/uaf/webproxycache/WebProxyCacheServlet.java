package cz.softinel.uaf.webproxycache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.softinel.uaf.util.StreamHelper;

public class WebProxyCacheServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getParameter("url");
		WebProxyCache webProxyCache = WebProxyCache.getInstance();
		Content content = webProxyCache.getContent(url);
		File file = content.getContent();
		long length = StreamHelper.copy(new FileInputStream(file), response.getOutputStream());
		response.setContentLength((int)length);
	}
}
