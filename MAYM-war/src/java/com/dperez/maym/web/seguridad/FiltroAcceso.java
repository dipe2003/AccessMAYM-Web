package com.dperez.maym.web.seguridad;


import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/Views/Configuraciones/*", 
    "/Views/Perfil/*", 
    "/Views/Acciones/General/SeguimientoAccion.xhtml",
    "/Views/Acciones/General/ActividadesAccion.xhtml",
    "/Views/Acciones/General/CrearAccion.xhtml",
    "/Views/Acciones/General/EditarAccion.xhtml",
    "/Views/Acciones/General/ProductoInvolucrado.xhtml",
    "/Views/Acciones/Fortalezas/CrearFortaleza.xhtml",
    "/Views/Acciones/Fortalezas/EditarFortaleza.xhtml",
})
public class FiltroAcceso implements Filter{

    @Override
    public void init(FilterConfig fc) throws ServletException {
       
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sr;
        HttpServletResponse resp = (HttpServletResponse) sr1;
        Usuario session = (Usuario) req.getSession().getAttribute("Usuario");
          
         if (session != null) {
             fc.doFilter(req, resp);
         } else {     
             HttpServletResponse res = (HttpServletResponse) resp;
             res.sendRedirect(req.getContextPath() + "/Views/Errores/Error_500.xhtml");
        }
    }

    @Override
    public void destroy() {
       
    }
    
}

