package net.rossillo.spring.web.mvc.test;

import net.rossillo.spring.web.mvc.CacheControlHandlerInterceptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

/**
 * Provides cache control interceptor tests.
 * 
 * @author Scott Rossillo
 *
 */
@SuppressWarnings("DataFlowIssue")
public final class CacheControlHandlerInterceptorTest {
	
	private CacheControlHandlerInterceptor interceptor;
	
	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;
	
	private final CacheControlAnnotatedTestController controller = new CacheControlAnnotatedTestController();
	
	@BeforeEach
	public void setUp() {
		interceptor = new CacheControlHandlerInterceptor();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void testCacheControlPublic() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePubliclyCachedPageRequest"));
		
		interceptor.preHandle(request, response, handler);
		
		System.err.println("CC: " + response.getHeader("Cache-Control"));
		
		assertNotNull(response.getHeader("Cache-Control"));
		assertTrue(response.getHeader("Cache-Control").contains("public"));
		assertFalse(response.getHeader("Cache-Control").contains("private"));
		assertTrue(response.getHeader("Cache-Control").contains("max-age=3600"));
	}
	
	@Test
	public void testCacheControlPublicProxyMustRevalidate() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePubliclyCachedPageAndProxyRevalidatedRequest"));
		
		interceptor.preHandle(request, response, handler);
		
		System.err.println("CC: " + response.getHeader("Cache-Control"));
		
		assertNotNull(response.getHeader("Cache-Control"));
		assertTrue(response.getHeader("Cache-Control").contains("public"));
		assertTrue(response.getHeader("Cache-Control").contains("proxy-revalidate"));
		assertFalse(response.getHeader("Cache-Control").contains("private"));
	}
	
	@Test
	public void testCacheControlMustRevalidate() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePubliclyCachedPageAndRevalidatedRequest"));
		
		interceptor.preHandle(request, response, handler);
		
		System.err.println("CC: " + response.getHeader("Cache-Control"));
		
		assertNotNull(response.getHeader("Cache-Control"));
		assertTrue(response.getHeader("Cache-Control").contains("must-revalidate"));
		assertFalse(response.getHeader("Cache-Control").contains("private"));
	}
	
	@Test
	public void testCacheControlPrivate() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePrivatelyCachedPageRequest"));
		
		interceptor.preHandle(request, response, handler);
		
		System.err.println("CC: " + response.getHeader("Cache-Control"));
		
		assertNotNull(response.getHeader("Cache-Control"));
		assertTrue(response.getHeader("Cache-Control").contains("private"));
		assertFalse(response.getHeader("Cache-Control").contains("public"));
	}

	@Test
	public void testCacheControlPrivateWithVary() throws Exception {

		final HandlerMethod handler = new HandlerMethod(
				controller,
				controller.getClass().getMethod("handlePrivatelyCachedPageRequestWithVary"));

		interceptor.preHandle(request, response, handler);

		System.err.println("Vary: " + response.getHeader("Vary"));

		assertNotNull(response.getHeader("Cache-Control"));
		assertTrue(response.getHeader("Cache-Control").contains("private"));
		assertFalse(response.getHeader("Cache-Control").contains("public"));
		assertTrue(response.getHeader("Vary").contains("Accept"));

	}

	@Test
	public void testExpires() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePrivatelyCachedPageRequest"));
		
		interceptor.preHandle(request, response, handler);
		assertNotNull(response.getHeader("Expires"));
	}
	
	@Test
	public void testNoExpires() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handlePrivatelyCachedPageRequest"));
		
		interceptor.setUseExpiresHeader(false);
		interceptor.preHandle(request, response, handler);
		assertFalse(response.containsHeader("Expires"));
	}
	
	@Test
	public void testHandleWithDefaultPolicy() throws Exception {
		
		final HandlerMethod handler = new HandlerMethod(
				controller, 
				controller.getClass().getMethod("handleWithDefaultPolicy"));
		
		assertNotNull(interceptor.getCacheControl(null, null, handler));
	}
}
