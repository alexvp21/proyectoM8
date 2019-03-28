package formulario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Form")
public class Form extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Form() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/JSP/form.jsp").forward(request, resp);
//		hola mundo
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
