module spring.mvc.cache.control.test {
    requires org.junit.jupiter.api;
    requires spring.context;
    requires spring.mvc.cache.control;
    requires spring.test;
    requires spring.web;
    
    opens net.rossillo.spring.web.mvc.test to spring.core;
    
    exports net.rossillo.spring.web.mvc.test to org.junit.platform.commons;
}