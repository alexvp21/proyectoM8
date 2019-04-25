package formulario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Properties;

@WebServlet("/login")
public class Login extends HttpServlet {
	
	private Statement stmt;
	private ResultSet rs;
	private Connection con;
	  PreparedStatement pstmt;

	
	public Login() {
		super();
	}
	
	private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String usuario = request.getParameter("user");
		String pass = request.getParameter("password");
		String email = request.getParameter("email");
		
		if (comprobarCampos(usuario, pass, email)) {
			//Comprueba posibles errores
			LOGGER.info("Definición de variables");
			HashMap<String, String> params = getConection();
			stmt = null;
	        rs = null;
			
            if (userExist(usuario, params) == Boolean.TRUE && stmt != null) {			            	
	            try {
	            	LOGGER.warning("Insert y commit en BBDD");
					stmt.executeQuery("INSERT INTO USUARIOS(\"USUARIO\", \"CONTRASENYA\", \"EMAIL\") VALUES('"+usuario+"', '"+pass+"', '"+email+"')");
					con.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, Constantes.ERRORSQL, e);
				}
	            request.setAttribute("infoForm", "Si es valido");
	            request.setAttribute("user", "usuario: "+usuario);
            } else {
            	request.setAttribute("user", "usuario ya registrado en la base de datos");
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
		Matcher mPass = pPass.matcher(pass);
		
		Pattern pEmail = Pattern.compile("^[A-Za-z0-9]*((\\.)?[a-z0-9])*@[a-z]*(-)?[a-z](\\.com|\\.es|\\.org|.)+$");
		Matcher mEmail = pEmail.matcher(email);
		
		return (mUser.matches() && mPass.matches() && mEmail.matches())? Boolean.TRUE : Boolean.FALSE;
	}
	
	private HashMap<String, String> getConection() {
		Properties prop = new Properties();
		HashMap<String, String> connect = new HashMap<>();
		try {
			LOGGER.info("Se configura la conexión a BBDD");
			InputStream input = new FileInputStream("../../config/database.properties");
			prop.load(input);
			
			connect.put(Constantes.BBDD, prop.getProperty(Constantes.BBDD));
			connect.put(Constantes.BDUSER, prop.getProperty(Constantes.BDUSER));
			connect.put(Constantes.BDPASS, prop.getProperty(Constantes.BDPASS));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERRORSQL, e);
		}
		return connect;
	}
	
	private boolean userExist(String usuario, HashMap<String, String> params) {
		boolean inDataBase = false;
		try {
			con = DriverManager.getConnection(params.get(Constantes.BBDD), params.get(Constantes.BDUSER), params.get(Constantes.BDPASS));
			LOGGER.info("Se comienza la transacción");
			inDataBase = initConnection(usuario);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERROR, e);
		}
		
		return inDataBase;
	}
	
	private boolean findUser(String usuario) throws SQLException {
		boolean inDataBase = false;
		try {
        	LOGGER.warning("Se lanza el select de usuarios");
        	if (stmt != null) {
        		String query = "SELECT * FROM Usuarios WHERE USUARIO = ?";
        	    pstmt = con.prepareStatement(query);
        	    pstmt.setString(1, usuario);
        	    rs = pstmt.executeQuery();
            	while(rs.next()) {
	                String usuarioBD = rs.getString("USUARIO");
	                if (usuarioBD.equalsIgnoreCase(usuario)) {
	                	inDataBase = true;
	                }
	            }
        	}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERRORSQL, e);
		} finally {
			if (rs != null) {
				rs.close();								
			}
		}
		return inDataBase;
	}
	
	private boolean initConnection(String usuario) throws SQLException {
		boolean inDataBase = false;
		try {
			LOGGER.info("Se crea la conexion");
			stmt = con.createStatement();
			inDataBase = findUser(usuario);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERROR, e);
		} finally {
			if (stmt != null) {
				stmt.close();								
			}
		}
		return inDataBase;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
