package net.rossillo.spring.web.mvc.test;

import static java.util.concurrent.TimeUnit.MINUTES;
import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;
import org.springframework.stereotype.Controller;

@CacheControl(policy = CachePolicy.PRIVATE)
@Controller
final class CacheControlAnnotatedTestController {
	
	@CacheControl(policy = CachePolicy.PUBLIC, maxAge = 60, timeunit = MINUTES)
	public String handlePubliclyCachedPageRequest() {
		return null;
	}
	
	@CacheControl(policy = { CachePolicy.MUST_REVALIDATE }, maxAge = 300)
	public String handlePubliclyCachedPageAndRevalidatedRequest() {
		return null;
	}
	
	@CacheControl(policy = { CachePolicy.PUBLIC, CachePolicy.PROXY_REVALIDATE }, maxAge = 60)
	public String handlePubliclyCachedPageAndProxyRevalidatedRequest() {
		return null;
	}
	
	@CacheControl(policy = CachePolicy.PRIVATE, maxAge = 360)
	public String handlePrivatelyCachedPageRequest() {
		return null;
	}

	@CacheControl(policy = CachePolicy.PRIVATE, maxAge = 360, vary = "Accept")
	public String handlePrivatelyCachedPageRequestWithVary() {
		return null;
	}
	
	public String handleWithDefaultPolicy() {
		return null;
	}
	
}
