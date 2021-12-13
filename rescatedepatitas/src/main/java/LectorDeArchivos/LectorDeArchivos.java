package LectorDeArchivos;
import java.io.*;


public class LectorDeArchivos {
	private File file;
	private BufferedReader bufferedReader;
	private String path;
	
	public LectorDeArchivos(String path) {
		this.path = path;
	}
	
	public Boolean pathArchivoValido() {
		File test = new File(path);
		return test.exists();
	}

	private FileReader abrirArchivo(String path) throws FileNotFoundException {
		this.file = new File(path).getAbsoluteFile();
		return new FileReader(file);
	}
	
	public Boolean existeEnArchivo(String cadena) throws IOException{
		String linea;
		FileReader fileReader = abrirArchivo(this.path);
		this.bufferedReader = new BufferedReader(fileReader);
		while((linea = bufferedReader.readLine()) != null) {
			if(linea.equals(cadena))
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


}
