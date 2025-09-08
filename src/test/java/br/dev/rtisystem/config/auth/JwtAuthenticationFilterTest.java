package br.dev.rtisystem.config.auth;

import br.dev.rtisystem.service.interfaces.auth.JwtService;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {JwtAuthenticationFilter.class, UserDetailsService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class JwtAuthenticationFilterTest {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ $.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName("Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ $")
    @Tag("ContributionFromDiffblue")
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$$() throws ServletException, IOException {
        // Arrange
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");
        when(request.getRequestURI()).thenReturn("https://example.org/example");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ $ then calls {@link JwtService#isTokenValid(String, UserDetails)}.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ $ then calls isTokenValid(String, UserDetails)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$$thenCallsIsTokenValid() throws ServletException, IOException {
        // Arrange
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any()))
                .thenReturn(false);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        when(request.getRequestURI()).thenReturn("https://example.org/example");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername("");
        verify(jwtService).isTokenValid(eq(""), isNull());
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ $ then throw {@link ServletException}.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ $ then throw ServletException")
    @Tag("ContributionFromDiffblue")
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$$thenThrowServletException() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doThrow(new ServletException("An error occurred"))
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act and Assert
        assertThrows(
                ServletException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ given {@link JwtService} {@link JwtService#extractUsername(String)} return
     * {@code null} $.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ given JwtService extractUsername(String) return 'null' $")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$givenJwtServiceExtractUsernameReturnNull$()
            throws ServletException, IOException {
        // Arrange
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn(null);
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        when(request.getRequestURI()).thenReturn("https://example.org/example");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername("");
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ given {@link JwtService} when {@link
     * MockHttpServletRequest#MockHttpServletRequest()} $ then calls {@link
     * FilterChain#doFilter(ServletRequest, ServletResponse)}.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ given JwtService when MockHttpServletRequest() $ then calls doFilter(ServletRequest, ServletResponse)")
    @Tag("ContributionFromDiffblue")
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$givenJwtServiceWhenMockHttpServletRequest$thenCallsDoFilter()
            throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ given {@code Parameter} $ then calls {@link
     * HttpServletRequestWrapper#getParameter(String)}.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ given 'Parameter' $ then calls getParameter(String)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$givenParameter$thenCallsGetParameter()
            throws ServletException, IOException {
        // Arrange
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getParameter(Mockito.<String>any())).thenReturn("Parameter");
        when(request.getRequestURI()).thenReturn("/stream");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getParameter("token");
        verify(request).getRequestURI();
    }

    /**
     * Test {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse,
     * FilterChain)} $ when {@link HttpServletRequestWrapper} {@link
     * HttpServletRequestWrapper#getParameter(String)} return {@code null} $.
     *
     * <p>Method under test: {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)}
     */
    @Test
    @DisplayName(
            "Test doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) $ when HttpServletRequestWrapper getParameter(String) return 'null' $")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "void JwtAuthenticationFilter.doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)"
    })
    void testDoFilterInternal$whenHttpServletRequestWrapperGetParameterReturnNull$()
            throws ServletException, IOException {
        // Arrange
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any()))
                .thenReturn(false);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getParameter(Mockito.<String>any())).thenReturn(null);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        when(request.getRequestURI()).thenReturn("/stream");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing()
                .when(filterChain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername("");
        verify(jwtService).isTokenValid(eq(""), isNull());
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getParameter("token");
        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
    }
}
