package formulario;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

/**
 * @author alex
 *
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	
	private Statement stmt;
	private ResultSet rs;
	private Connection con;
	PreparedStatement pstmt;
	
	/**
	 * Public constructor
	 */
	public Login() {
		super();
	}
	
	/**
	 * Las constantes para poder hacer las comprobaciones Regex y el logger
	 */
	private static final Pattern pUser = Pattern.compile(Constantes.USEREXP);
	private static final Pattern pPass = Pattern.compile(Constantes.PASSEXP);
	private static final Pattern pEmail = Pattern.compile(Constantes.EMAILEXP);
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
			
            if (userExist(usuario, params) == Boolean.FALSE && stmt != null) {			            	
	            try {
	            	LOGGER.warning("Insert y commit en BBDD");
					stmt.executeUpdate("INSERT INTO usuarios VALUES('"+usuario+"', '"+pass+"', '"+email+"')");
					con.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, Constantes.ERRORSQL, e);
				}
	            request.setAttribute("infoForm", "Si es valido");
	            request.setAttribute("user", "usuario: "+usuario);
	            request.setAttribute("status", "ok");
            } else {
            	request.setAttribute("user", "usuario ya registrado en la base de datos");
            	request.setAttribute("status", "fail");
            }
		} else {
			request.setAttribute("infoForm", "No es valido");
			request.setAttribute("status", "fail");
		}
		getServletContext().getRequestDispatcher("/JSP/info.jsp").forward(request, resp);
	}
	
	/**
	 * @param user
	 * @param pass
	 * @param email
	 * @return boolean
	 * 
	 * Comprueba los campos que recibe del formulario y aplica los Regex.
	 */
	private Boolean comprobarCampos(String user, String pass, String email) {
		boolean resp = false;
		try {
			LOGGER.info("Se ejecuta el regex para los campos");
			Properties prop = new Properties();
			InputStream input = new FileInputStream("/home/alex/git/proyectoM8/Formulario1/config/database.properties");
			prop.load(input);
			
			Matcher mUser = pUser.matcher(user);
			
			Matcher mPass = pPass.matcher(pass);
			
			Matcher mEmail = pEmail.matcher(email);
			resp = (mUser.matches() && mPass.matches() && mEmail.matches())? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERROR, e);
			resp = false;
		}
		return resp;
	}
	
	/**
	 * @return HashMap 
	 * Retorna los parametro de conexión a la base de datos 
	 */
	private HashMap<String, String> getConection() {
		Properties prop = new Properties();
		HashMap<String, String> connect = new HashMap<>();
		try {
			LOGGER.info("Se configura la conexión a BBDD");
			InputStream input = new FileInputStream("/home/alex/git/proyectoM8/Formulario1/config/database.properties");
			prop.load(input);
			
			connect.put(Constantes.BBDD, prop.getProperty(Constantes.BBDD));
			connect.put(Constantes.BDUSER, prop.getProperty(Constantes.BDUSER));
			connect.put(Constantes.BDPASS, prop.getProperty(Constantes.BDPASS));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERRORSQL, e);
		}
		return connect;
	}
	
	/**
	 * @param usuario
	 * @param params
	 * @return boolean
	 * Retorna un boolean para saber si un usuario ya está en la base de datos
	 */
	private boolean userExist(String usuario, HashMap<String, String> params) {
		boolean inDataBase = false;
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Properties bdprops = new Properties();
			bdprops.put("user", params.get(Constantes.BDUSER));
			bdprops.put("password", params.get(Constantes.BDPASS));
			con = DriverManager.getConnection(params.get(Constantes.BBDD), bdprops);
			LOGGER.info("Se comienza la transacción");
			inDataBase = initConnection(usuario);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERROR, e);
		}
		
		return inDataBase;
	}
	
	/**
	 * @param usuario
	 * @return boolean
	 * @throws SQLException
	 * Retorna un boolean para saber si el usuario que le pasams
	 * coincide con algun usuario de la base de datos
	 */
	private boolean findUser(String usuario) throws SQLException {
		boolean inDataBase = false;
		try {
        	LOGGER.warning("Se lanza el select de usuarios");
        	if (stmt != null) {
        		String query = "SELECT * FROM usuarios WHERE usuario = ?";
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
			if (rs != null) {
				rs.close();								
			}
		}
		return inDataBase;
	}
	
	/**
	 * @param usuario
	 * @return boolean
	 * @throws SQLException
	 * Inicia la conexión para lanzar la busqueda del usuario
	 * y saber si esta en la base de datos
	 */
	private boolean initConnection(String usuario) throws SQLException {
		boolean inDataBase = false;
		try {
			LOGGER.info("Se crea la conexion");
			stmt = con.createStatement();
			inDataBase = findUser(usuario);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constantes.ERROR, e);
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
