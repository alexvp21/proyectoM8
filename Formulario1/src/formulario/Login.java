package formulario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class Login extends HttpServlet {
	public Login() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String usuario = request.getParameter("user");
		String pass = request.getParameter("password");
		String email = request.getParameter("email");
		
		if (comprobarCampos(usuario, pass, email)) {
			//Comprueba posibles errores			
			try {
					Class.forName("org.hsqldb.jdbcDriver");
					String url = "jdbc:hsqldb:hsql://localhost/Tienda";
			        String user = "SA";
			        String password = "";
			        Statement stmt = null;
			        ResultSet rs = null;
					try (Connection con = DriverManager.getConnection(url, user, password)) {
						boolean inDataBase = false;
						try {
							stmt = con.createStatement();
						} catch (Exception e) {
							System.err.println("FALLO!");
						} finally {
							if (stmt != null) {
								stmt.close();								
							}
						}
			            try {
			            	if (stmt != null) {
				            	rs = stmt.executeQuery("SELECT * FROM Usuarios WHERE USUARIO='"+usuario+"'");
				            	while(rs.next()) {
					                String usuarioBD = rs.getString("USUARIO");
					                if (usuarioBD.equalsIgnoreCase(usuario)) {
					                	inDataBase = true;
					                }
					            }
			            	}
						} catch (Exception e) {
							System.err.println("FALLO!");
						} finally {
							if (rs != null) {
								rs.close();								
							}
						}
			            if (inDataBase != true & stmt != null) {			            	
				            stmt.executeQuery("INSERT INTO USUARIOS(\"USUARIO\", \"CONTRASENYA\", \"EMAIL\") VALUES('"+usuario+"', '"+pass+"', '"+email+"')");
				            con.commit();
				            request.setAttribute("infoForm", "Si es valido");
				            request.setAttribute("user", "usuario: "+usuario);
			            } else {
			            	request.setAttribute("user", "usuario ya registrado en la base de datos");
			            }
			        } catch (SQLException e) {
						System.err.println("Otro FALLO!");
					}
			} catch (ClassNotFoundException e) {
				System.err.println("FALLO!");
			}
		} else {
			request.setAttribute("infoForm", "No es valido");
		}
		getServletContext().getRequestDispatcher("/JSP/info.jsp").forward(request, resp);
	}
	
	private Boolean comprobarCampos(String user, String pass, String email) {
		Pattern pUser = Pattern.compile("([A-Za-z]*[\\d]*){0,9}");
		Matcher mUser = pUser.matcher(user);
		
		Pattern pPass = Pattern.compile("([A-Za-z]*[\\d]*){8,}");
		Matcher mPass = pPass.matcher(user);
		
		Pattern pEmail = Pattern.compile("^[A-Za-z0-9]*((\\.)?[a-z0-9])*@[a-z]*(-)?[a-z](\\.com|\\.es|\\.org|.)+$");
		Matcher mEmail = pEmail.matcher(email);
		
		return (mUser.matches() & mPass.matches() & mEmail.matches())? true : false;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
