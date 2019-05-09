package formulario;

public class Constantes{

	private Constantes() {
	  throw new UnsupportedOperationException();
	}
	
	// Errores	
	public static final String ERROR = "Error en la aplicación";
	public static final String ERRORCRITICO = "Error critico";
	public static final String ERRORSQL = "Error SQL";
	
	// BBDD
	public static final String BBDD = "database";
	public static final String BDPASS = "dbpass";
	public static final String BDUSER = "dbuser";
	
	// Regex
	public static final String USEREXP = "([A-Za-z]*[\\d]*){0,9}";
	public static final String PASSEXP = "([A-Za-z]*[\\d]*){8,}";
	public static final String EMAILEXP = "^[A-Za-z0-9]*((\\.)?[a-z0-9])*@[a-z]*(-)?[a-z](\\.com|\\.es|\\.org|.)+$";
}
